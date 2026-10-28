package com.dsk.usage_service.service;

import com.dsk.kafka.event.AlertKafkaEvent;
import com.dsk.kafka.event.EnergyUsageEvent;
import com.dsk.usage_service.client.DeviceClient;
import com.dsk.usage_service.client.UserClient;
import com.dsk.usage_service.dto.UserDTO;
import com.dsk.usage_service.model.DeviceEnergy;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsageService {

    private InfluxDBClient influxDBClient;
    private DeviceClient deviceClient;
    private UserClient userClient;
    private KafkaTemplate<String , AlertKafkaEvent> kafkaTemplate;

    public UsageService(InfluxDBClient influxDBClient , DeviceClient deviceClient , UserClient userClient , KafkaTemplate kafkaTemplate){
        this.influxDBClient = influxDBClient;
        this.deviceClient =deviceClient;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${influxdb.bucket}")
    private String influxdbBucket;
    @Value("${influxdb.org}")
    private String influxdbOrg;

    @KafkaListener(topics = "energy-usage" , groupId = "usage-service")
    public void getUsageEvents(EnergyUsageEvent energyUsageEvent){

        Point point = Point.measurement("energy_usage")
                .addTag("deviceId" , String.valueOf(energyUsageEvent.deviceId()))
                .addField("energyConsumption" , energyUsageEvent.energyConsumed())
                .time(energyUsageEvent.timeStamp().toEpochMilli(), WritePrecision.MS);

        influxDBClient.getWriteApiBlocking().writePoint(influxdbBucket ,influxdbOrg ,point);
    }

    private void fillUserIds(Map<Long,DeviceEnergy> deviceEnergies){
        for(Long deviceId : deviceEnergies.keySet()){
            long userId = 0;
            DeviceEnergy deviceEnergy = deviceEnergies.get(deviceId);
            try {
                userId = deviceClient.getUserIdByDeviceId(deviceId);
                log.info("DeviceID: {} :: UserID:{}" , deviceId , userId);
            } catch (Exception e) {
                log.info("Exception occured while fetching device user : {}" , e.getMessage());
                continue;
            }
            deviceEnergy.setUserId(userId);
            deviceEnergies.put(deviceId , deviceEnergy);
        }
    }

    private void fillUserDetails(Map<Long, AlertKafkaEvent> alertKafkaEventMap , Map<Long , Double> totalEnergyConsumptions) {
        for(Long userId : totalEnergyConsumptions.keySet()){
            UserDTO user = null;
            try {
                user = userClient.getUser(userId);
                log.info("User Fetched:  {}" , user);
            } catch (Exception e) {
                log.info("Exception Occured while fetching the user : {}",e.getMessage()  );
                continue;
            }
            Double energyConsumed = totalEnergyConsumptions.get(userId);

            AlertKafkaEvent alertKafkaEvent = AlertKafkaEvent
                    .builder()
                    .id(user.id())
                    .fullName(user.fullName())
                    .email(user.email())
                    .alerting(user.alerting())
                    .energyAlertingThreshold(user.energyAlertingThreshold())
                    .energyConsumed(energyConsumed)
                    .build();

            alertKafkaEventMap.put(userId , alertKafkaEvent);
        }
    }

    private void alertUser(Map<Long, AlertKafkaEvent> alertKafkaEventMap) {

        for(Long userId :  alertKafkaEventMap.keySet()){
            AlertKafkaEvent alertEvent = alertKafkaEventMap.get(userId);

            if(!alertEvent.alerting() && alertEvent.energyConsumed() > alertEvent.energyAlertingThreshold()){
                continue;
            }

            log.info("ALERT : User {} Exceeded the threashold {} used {} energy" , alertEvent.fullName() , alertEvent.energyAlertingThreshold() , alertEvent.energyConsumed());
            kafkaTemplate.send("usage-alert" , alertEvent);
        }
    }

    @Scheduled(cron = "*/10 * * * * *")
    private void aggregateEnergyConsumption(){

        Instant now  = Instant.now();
        Instant oneHourAgo = now.minusSeconds(3600);

        String fluxQuery = String.format("""
                from(bucket : "%s")
                  |> range(start: -24d)
                  |> filter(fn: (r)=> r["_measurement"] == "energy_usage")
                  |> filter(fn: (r)=> r["_field"] == "energyConsumption")
                  |> group(columns: ["deviceId"])
                  |> sum(column: "_value")
                """ , influxdbBucket);

        QueryApi queryApi = influxDBClient.getQueryApi();

        List<FluxTable> tables =  queryApi.query(fluxQuery ,influxdbOrg);
        Map<Long , DeviceEnergy> deviceEnergies = new HashMap<>();

        for(FluxTable table : tables){
            for(FluxRecord fluxRecord : table.getRecords()){
                String deviceIdStr = (String)fluxRecord.getValueByKey("deviceId");
                double energyConsumed = fluxRecord.getValueByKey("_value") instanceof  Number ?
                        (Double) fluxRecord.getValueByKey("_value") : 0.0;

                DeviceEnergy deviceEnergy = DeviceEnergy.builder()
                        .deviceId(Long.parseLong(deviceIdStr))
                        .energyConsumed(energyConsumed)
                        .build();

                deviceEnergies.put(deviceEnergy.getDeviceId(), deviceEnergy);
            }
        }
        fillUserIds(deviceEnergies);
        log.info("Filled Device Energies {}" , deviceEnergies);
        Map<Long, List<DeviceEnergy>> userEnergyConsumptionMapping = deviceEnergies.values()
                .stream()
                .collect(Collectors.groupingBy((s)->s.getUserId()));
        Map<Long , Double> totEnergyConsumptions = new HashMap<>();

        for(Long userId : userEnergyConsumptionMapping.keySet()){
            Double  totEnergyUsed = userEnergyConsumptionMapping.get(userId)
                    .stream()
                    .map(e->e.getEnergyConsumed())
                    .mapToDouble(Double::doubleValue)
                    .sum();
            totEnergyConsumptions.put(userId , totEnergyUsed);
        }

        Map<Long , AlertKafkaEvent> alertKafkaEventMap = new HashMap<>();

        fillUserDetails(alertKafkaEventMap ,totEnergyConsumptions);
        log.info("Kafka Events {} " , alertKafkaEventMap);
        alertUser(alertKafkaEventMap);
    }
}
