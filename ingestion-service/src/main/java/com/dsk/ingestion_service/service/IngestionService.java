package com.dsk.ingestion_service.service;

import com.dsk.ingestion_service.DTO.EnergyUsageDTO;
import com.dsk.kafka.event.EnergyUsageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IngestionService {

    private final KafkaTemplate<String , EnergyUsageEvent> kafkaTemplate;

    public IngestionService(KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void ingestEnergyUsage(EnergyUsageDTO energyUsageDTO){

        EnergyUsageEvent event = EnergyUsageEvent
                .builder()
                .deviceId(energyUsageDTO.deviceId())
                .timeStamp(energyUsageDTO.timeStamp())
                .energyConsumed(energyUsageDTO.energyConsumed())
                .build();

        kafkaTemplate.send("energy-usage" , event);
        log.info("Ingested Energy usage {}" , event);
    }
}
