package com.example.demo.service;

import com.example.demo.dto.DeviceDTO;
import com.example.demo.model.DeviceStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final DeviceService deviceService;

    public ReportService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Returns list of non-compliant devices (e.g. status not ACTIVE).
     */
    public List<DeviceDTO> getNonCompliantDevices() {
        List<DeviceDTO> devices = deviceService.getAllDevices();
        return devices.stream()
                .filter(device -> device.getStatus() != DeviceStatus.ACTIVE)
                .collect(Collectors.toList());
    }
}