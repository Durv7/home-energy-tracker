package com.dsk.usage_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEnergy {

    private long deviceId;
    private long userId;
    private double energyConsumed;
}
