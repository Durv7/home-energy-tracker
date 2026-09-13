package com.dsk.user_service;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.type.descriptor.jdbc.TimestampUtcAsOffsetDateTimeJdbcType;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User{
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private String name;
     private String surname;
     private String email;
     private String address;
     private boolean alerting;
     @Column(name = "energy_alerting_threshold")
     private Double energyAlertingThreshold;
     @Column(name = "created_at" , updatable = false , nullable = false)
     private Instant createdAt;
     @Column(name = "modified_at")
     private Instant modifiedAt;
     @Column( name = "is_deleted")
     private boolean isDeleted;
}
