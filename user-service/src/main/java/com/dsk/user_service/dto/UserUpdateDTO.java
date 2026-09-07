package com.dsk.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private String name;
    private String surname;
    private String address;
    private String energyAlertingThreshold;
}
