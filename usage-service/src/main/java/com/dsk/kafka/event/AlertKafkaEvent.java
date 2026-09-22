package com.dsk.kafka.event;

import lombok.Builder;

@Builder
public record AlertKafkaEvent(
         Long id,
         String fullName,
         String email,
         boolean alerting,
         Double energyAlertingThreshold,
         Double energyConsumed
){

}
