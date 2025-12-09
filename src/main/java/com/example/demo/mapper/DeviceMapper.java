package com.example.demo.mapper;

import com.example.demo.dto.DeviceDTO;
import com.example.demo.model.Device;

public class DeviceMapper {

    public static DeviceDTO toDTO(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setDeviceId(device.getDeviceId());
        dto.setType(device.getType());
        dto.setIpAddress(device.getIpAddress());
        dto.setLocation(device.getLocation());
        dto.setModel(device.getModel());
        dto.setStatus(device.getStatus());
        return dto;
    }

    public static Device toEntity(DeviceDTO dto) {
        Device device = new Device();
        device.setDeviceId(dto.getDeviceId());
        device.setType(dto.getType());
        device.setIpAddress(dto.getIpAddress());
        device.setLocation(dto.getLocation());
        device.setModel(dto.getModel());
        device.setStatus(dto.getStatus());
        return device;
    }
}