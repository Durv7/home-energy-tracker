package com.dsk.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private boolean alerting;
    private Double engergyAltertingThreshold;
}
