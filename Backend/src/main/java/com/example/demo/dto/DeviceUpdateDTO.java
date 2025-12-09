package com.example.demo.dto;

import com.example.demo.model.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceUpdateDTO {

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "IP Address is required")
    private String ipAddress;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Status is required")
    private DeviceStatus status;
}