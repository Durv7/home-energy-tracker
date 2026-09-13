package com.dsk.device_service.mapper;


import com.dsk.device_service.dto.DeviceDTO;
import com.dsk.device_service.entity.Device;
import org.apache.catalina.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring" , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeviceMapper {

    @Mapping(target = "createdAt" , expression = "java(java.time.Instant.now())")
    public Device toEntity(DeviceDTO deviceDTO);

    public DeviceDTO toResponse(Device device);

    void updateDevice(DeviceDTO deviceDTO , @MappingTarget Device device);
}
