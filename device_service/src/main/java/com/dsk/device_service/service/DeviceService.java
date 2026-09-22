package com.dsk.device_service.service;

import com.dsk.device_service.dto.DeviceDTO;
import com.dsk.device_service.entity.Device;
import com.dsk.device_service.exception.DeviceNotFoundException;
import com.dsk.device_service.mapper.DeviceMapper;
import com.dsk.device_service.repository.DeviceRepository;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    public DeviceDTO createDevice(DeviceDTO deviceDTO){
        Device device = deviceMapper.toEntity(deviceDTO);

        deviceRepository.save(device);
        return deviceDTO;
    }

    public DeviceDTO getDevice(long id){
        Device device = deviceRepository.findById(id)
                .orElseThrow(()-> new DeviceNotFoundException("Device not found"));

        return deviceMapper.toResponse(device);
    }

    public DeviceDTO updateDevice(DeviceDTO deviceDTO , long id){

        Device device = deviceRepository.findById(id)
                .orElseThrow(()-> new DeviceNotFoundException("Device not found"));

        deviceMapper.updateDevice(deviceDTO , device);

        deviceRepository.save(device);
        return deviceMapper.toResponse(device);
    }

    public void deleteDevice(long id){
        Device device = deviceRepository.findById(id)
                .orElseThrow(()-> new DeviceNotFoundException("Device not found"));

        device.setDeleted(true);
        deviceRepository.save(device);
    }

    public long getUserIdByDeviceId(long deviceId) throws Exception {
        long userId = deviceRepository.getUserIdByDeviceId(deviceId)
                .orElseThrow(()->new Exception("User Id not found"));

        return userId;
    }
}
