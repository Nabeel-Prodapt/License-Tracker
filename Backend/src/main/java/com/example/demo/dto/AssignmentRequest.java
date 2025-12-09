package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class AssignmentRequest {

    @NotBlank(message = "deviceId cannot be blank")
    private String deviceId;

    @NotBlank(message = "licenseKey cannot be blank")
    private String licenseKey;

    // Optionally, add assignedOn date if you want it set manually
    // private Instant assignedOn; 

    // Getters and setters
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }
}