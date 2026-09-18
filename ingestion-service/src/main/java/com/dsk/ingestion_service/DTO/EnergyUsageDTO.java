package com.dsk.ingestion_service.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;

@Builder
public record EnergyUsageDTO(
        long deviceId,
        double energyConsumed ,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timeStamp
) {
}
