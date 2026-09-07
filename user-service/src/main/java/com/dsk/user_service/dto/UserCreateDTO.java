package com.dsk.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private String name;
    @NotBlank
    @Size(min = 2)
    private String surname;
    @Email
    private String email;
    @NotBlank
    private String address;
    @Min(0)
    private double energyAlertingThreshold;
}
