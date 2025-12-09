package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    // FK to user (optional: you can link directly if you have a User entity)
    @Column(name = "user_id")
    @JsonProperty("userId")
    private Long userId;

    @Column(name = "username", nullable = false, length = 100)
    @JsonProperty("username")
    private String username;

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType; // DEVICE, LICENSE, ASSIGNMENT

    @Column(name = "entity_id", nullable = false, length = 50)
    private String entityId;   // e.g. deviceId, licenseKey, assignmentId

    @Column(name = "action", nullable = false, length = 20)
    private String action;     // CREATE, UPDATE, DELETE

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private Timestamp timestamp;

    @Lob
    @Column(name = "details")
    private String details;    // JSON string: {"old": {...}, "new": {...}}

}