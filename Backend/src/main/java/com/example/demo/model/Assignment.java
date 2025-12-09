package com.example.demo.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "assignments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"device_id", "license_key"}))
public class Assignment {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "assignment_id")
  private Long assignmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "device_id", nullable = false)
  private Device device;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "license_key", nullable = false)
  private License license;

  @Column(name = "assigned_on", nullable = false, updatable = false)
  private Instant assignedOn;

  @PrePersist
  public void prePersist() {
    if (assignedOn == null) {
      assignedOn = Instant.now();
    }
  }
}