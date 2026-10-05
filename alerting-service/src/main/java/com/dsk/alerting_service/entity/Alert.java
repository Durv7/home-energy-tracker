package com.dsk.alerting_service.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Builder
public class Alert{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private boolean sent;
    private Instant createdAt;
    private String mailContent;
}
