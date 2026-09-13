package com.dsk.device_service.controller;

import com.dsk.device_service.dto.DeviceDTO;
import com.dsk.device_service.repository.DeviceRepository;
import com.dsk.device_service.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO deviceDTO){
        DeviceDTO response = deviceService.createDevice(deviceDTO);

        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable long id){
        DeviceDTO response = deviceService.getDevice(id);

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@RequestBody DeviceDTO deviceDTO , @PathVariable long id){
        DeviceDTO response = deviceService.updateDevice(deviceDTO , id);

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> createDevice(@PathVariable long id){
       deviceService.deleteDevice(id);

        return ResponseEntity.noContent().build();
    }
}
