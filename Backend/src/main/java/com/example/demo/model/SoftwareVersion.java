package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "software_version")
public class SoftwareVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sv_id")
    private Long svId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device; // Many versions can belong to one device

    @Column(name = "software_name", length = 100, nullable = false)
    private String softwareName;

    @Column(name = "current_version", length = 20, nullable = false)
    private String currentVersion;

    @Column(name = "latest_version", length = 20)
    private String latestVersion; // optional

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private SoftwareStatus status;

    @Column(name = "last_checked")
    private Date lastChecked; // For health check timestamp

}