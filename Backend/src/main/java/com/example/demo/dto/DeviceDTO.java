package com.example.demo.dto;

import com.example.demo.model.DeviceStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DeviceDTO {
    
    @NotBlank(message = "Device ID is required")
    @Size(max = 30, message = "Device ID must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z]{3}-[A-Z]{3}-\\d{3}$", 
             message = "Device ID must follow pattern: RTR-BLR-001")
    private String deviceId;
    
    @NotBlank(message = "Type is required")
    @Size(max = 30)
    private String type;
    
    @NotBlank(message = "IP Address is required")
    @Pattern(regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", 
             message = "Invalid IP address format")
    private String ipAddress;
    
    @NotBlank(message = "Location is required")
    @Size(max = 100)
    private String location;
    
    @NotBlank(message = "Model is required")
    @Size(max = 50)
    private String model;
    
    @NotNull(message = "Status is required")
    private DeviceStatus status;
}