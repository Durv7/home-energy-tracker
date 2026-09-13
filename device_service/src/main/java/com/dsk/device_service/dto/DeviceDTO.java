package com.dsk.device_service.dto;

import com.dsk.device_service.model.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {

    @NotBlank
    @Size(min = 2)
    private String name;
    private String location;
    @NotNull
    private DeviceType deviceType;
    @NotNull
    private long userId;
}
