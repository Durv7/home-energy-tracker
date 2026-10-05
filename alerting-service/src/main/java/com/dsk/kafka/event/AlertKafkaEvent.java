package com.dsk.kafka.event;

public record AlertKafkaEvent(
        Long id,
        String fullName,
        String email,
        boolean alerting,
        Double energyAlertingThreshold,
        Double energyConsumed
) {
}
