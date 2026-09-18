package com.dsk.ingestion_service.simulation;

import com.dsk.ingestion_service.DTO.EnergyUsageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.Random;

@Component
@Slf4j
public class ContinuousDataSimulator implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final Random random ;
    @Value(value = "${simulation.requests-per-interval}")
    private int requestPerInterval;
    @Value(value = "${simulation.ingestionEndpoint}")
    private String ingestionEndPoint;

    public ContinuousDataSimulator() {
        this.restTemplate = new RestTemplate();
        this.random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Continuous Data simulator started...");
    }

//    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void sendMockData(){
        for(int i=0 ; i<requestPerInterval; i++){
            EnergyUsageDTO energyUsageDTO = EnergyUsageDTO.builder()
                    .deviceId(random.nextInt(1 , 9))
                    .energyConsumed(Math.round(random.nextDouble(0.0 , 2.0)*100.0)/100.0)
                    .timeStamp(Instant.now())
                    .build();

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<EnergyUsageDTO> httpEntity = new HttpEntity<>(energyUsageDTO , headers);

                restTemplate.postForEntity(ingestionEndPoint , httpEntity ,Void.class);
                log.info("Sent mock data {}" , energyUsageDTO);
            } catch (RestClientException e) {
                log.error(e.getMessage());
            }
        }

   }

}
