package com.example.demo.dto;

import java.time.Instant;

public class AssignmentResponse {
    private Long assignmentId;
    private String deviceId;
    private String licenseKey;
    private Instant assignedOn;

    // Constructor
    public AssignmentResponse(Long assignmentId, String deviceId, String licenseKey, Instant assignedOn) {
        this.assignmentId = assignmentId;
        this.deviceId = deviceId;
        this.licenseKey = licenseKey;
        this.assignedOn = assignedOn;
    }

    // Getters
    public Long getAssignmentId() { return assignmentId; }
    public String getDeviceId() { return deviceId; }
    public String getLicenseKey() { return licenseKey; }
    public Instant getAssignedOn() { return assignedOn; }
}