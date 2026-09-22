package com.dsk.usage_service.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        Long id,
        String fullName,
        String email,
        boolean alerting,
        Double energyAlertingThreshold
) {
}
