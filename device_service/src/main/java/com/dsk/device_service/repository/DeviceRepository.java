package com.dsk.device_service.repository;

import com.dsk.device_service.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device , Long> {

    @Query("SELECT d.userId FROM Device d WHERE d.id = :deviceId")
    public Optional<Long> getUserIdByDeviceId(@Param("deviceId") long deviceId);
}
