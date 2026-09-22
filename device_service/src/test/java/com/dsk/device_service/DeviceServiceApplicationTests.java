package com.dsk.device_service;

import com.dsk.device_service.entity.Device;
import com.dsk.device_service.model.DeviceType;
import com.dsk.device_service.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
@Slf4j
class DeviceServiceApplicationTests {

    @Autowired
    DeviceRepository deviceRepository;

	@Test
	void contextLoads() {
	}

    @Disabled
    @Test
    void createDevices(){
        for(int i=0; i<200; i++){
            Device device = Device.builder()
                    .name("Device"+i)
                    .location("Location"+i)
                    .userId((i%10)+1)
                    .deviceType(DeviceType.values()[i%DeviceType.values().length])
                    .createdAt(Instant.now())
                    .isDeleted(false)
                    .build();

            deviceRepository.save(device);
            log.info("Device Repository populated successfully");
        }
    }
}
