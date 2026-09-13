package com.dsk.device_service.entity;

import com.dsk.device_service.model.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DeviceType deviceType;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "modified_at")
    private Instant modifiedAt;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
