package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
public class Device {

    @Id
    @Column(name = "device_id", length = 30, nullable = false, unique = true)
    private String deviceId; // e.g., RTR-BLR-001

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false)
    private String type; // Router, Switch, Firewall

    @NotBlank
    @Size(max = 15)
    @Column(nullable = false, unique = true, length = 15)
    private String ipAddress;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String location; // Region, site, or DC

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String model; // Vendor-specific

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeviceStatus status = DeviceStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    // Relationships
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Assignment> assignments = new HashSet<>();
}