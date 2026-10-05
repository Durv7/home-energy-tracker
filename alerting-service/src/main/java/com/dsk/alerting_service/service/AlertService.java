package com.dsk.alerting_service.service;

import com.dsk.alerting_service.entity.Alert;
import com.dsk.alerting_service.repository.AlertRepository;
import com.dsk.kafka.event.AlertKafkaEvent;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class AlertService {

    private final EmailService emailService;

    private final AlertRepository alertRepository;


    public AlertService(EmailService emailService, AlertRepository alertRepository) {
        this.emailService = emailService;
        this.alertRepository = alertRepository;
    }

    @KafkaListener(topics = "usage-alert" , groupId = "alert-service")
    public void alertUser(AlertKafkaEvent alertKafkaEvent){

        String subject = "Energy Usage Alert – You Have Exceeded Your Set Limit";
        String htmlBody = emailService.getAlertTemplate()
                .replace("{{fullName}}" , alertKafkaEvent.fullName())
                .replace("{{email}}" , alertKafkaEvent.email())
                .replace("{{alerting}}", alertKafkaEvent.alerting()?"ON":"OFF")
                .replace("{{energyAlertingThreshold}}" , String.valueOf(alertKafkaEvent.energyAlertingThreshold()))
                .replace("{{energyConsumed}}" , String.valueOf(alertKafkaEvent.energyConsumed()));
        try{
            emailService.sendMimeMessage(alertKafkaEvent.email(), subject , htmlBody);

            Alert alert = Alert.builder()
                    .userId(alertKafkaEvent.id())
                    .createdAt(Instant.now())
                    .mailContent(htmlBody)
                    .sent(true)
                    .build();

            alertRepository.save(alert);
        }catch (MessagingException messagingException){
            log.info("Error Occured while sending  mail");
            Alert alert = Alert.builder()
                    .userId(alertKafkaEvent.id())
                    .createdAt(Instant.now())
                    .mailContent(htmlBody)
                    .sent(false)
                    .build();

            alertRepository.save(alert);
        }
    }
}
