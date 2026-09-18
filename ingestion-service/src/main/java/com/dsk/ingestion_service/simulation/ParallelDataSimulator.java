package com.dsk.ingestion_service.simulation;

import com.dsk.ingestion_service.DTO.EnergyUsageDTO;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public  class ParallelDataSimulator implements CommandLineRunner {

    private final ExecutorService executorService;
    @Value("${simulator.parallelThreadSize}")
    private int parallelThreadSize;

    @Value("${simulation.requests-per-interval}")
    private int requestPerInterval;

    @Value("${simulation.ingestionEndpoint}")
    private String ingestionEndPoint;
    private final Random random;
    private final RestTemplate restTemplate;

    public ParallelDataSimulator(){
        this.executorService = Executors.newCachedThreadPool();
        this.random = new Random();
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Parallel Simulator Starterd...");

        ((ThreadPoolExecutor)executorService).setCorePoolSize(parallelThreadSize);
    }


    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void sendMockData(){
        int batchSize = requestPerInterval / parallelThreadSize;
        int reminder = requestPerInterval % parallelThreadSize;

        for(int i=0; i<parallelThreadSize; i++){
            int requestForThread = batchSize + (i < reminder ? 1 : 0);
            executorService.submit(()->{
                for(int j=0 ; j<requestPerInterval; j++){
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
            });
        }
    }

    @PreDestroy
    public void shutDown(){
        executorService.shutdown();
    }
}
