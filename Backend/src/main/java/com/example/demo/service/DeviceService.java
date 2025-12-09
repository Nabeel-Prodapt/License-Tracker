package com.example.demo.service;

import com.example.demo.audit.Auditable;
import com.example.demo.dto.DeviceDTO;
import com.example.demo.mapper.DeviceMapper;
import com.example.demo.model.Assignment;
import com.example.demo.model.Device;
import com.example.demo.model.DeviceStatus;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.DeviceRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    private final AuditLogService auditLogService;

    private final AssignmentRepository assignmentRepository;

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository, AssignmentRepository assignmentRepository, AuditLogService auditLogService) {
        this.deviceRepository = deviceRepository;
        this.assignmentRepository = assignmentRepository;
        this.auditLogService = auditLogService;	
    }

    // CREATE
    @Auditable(entityType = "DEVICE", action = "CREATE")
    public DeviceDTO createDevice(DeviceDTO dto) {
        Device device = DeviceMapper.toEntity(dto);
        Device saved = deviceRepository.save(device);
        return DeviceMapper.toDTO(saved);
    }

    // READ ALL
    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(DeviceMapper::toDTO)
                .toList();
    }

    // FILTER: by location or type
    public List<DeviceDTO> getFilteredDevices(String location, String type) {
        List<Device> devices;

        if (location != null && type != null) {
            devices = deviceRepository.findByLocationContainingIgnoreCaseAndTypeContainingIgnoreCase(location, type);
        } else if (location != null) {
            devices = deviceRepository.findByLocationContainingIgnoreCase(location);
        } else if (type != null) {
            devices = deviceRepository.findByTypeContainingIgnoreCase(type);
        } else {
            devices = deviceRepository.findAll();
        }

        return devices.stream().map(DeviceMapper::toDTO).toList();
    }

    // READ ONE
    public Optional<DeviceDTO> getDeviceById(String deviceId) {
        return deviceRepository.findById(deviceId).map(DeviceMapper::toDTO);
    }

    // UPDATE
    @Auditable(entityType = "DEVICE", action = "UPDATE")
    public Optional<DeviceDTO> updateDevice(String deviceId, DeviceDTO dto) {
        return deviceRepository.findById(deviceId).map(existing -> {
            existing.setType(dto.getType());
            existing.setIpAddress(dto.getIpAddress());
            existing.setLocation(dto.getLocation());
            existing.setModel(dto.getModel());
            existing.setStatus(dto.getStatus());
            Device updated = deviceRepository.save(existing);
            return DeviceMapper.toDTO(updated);
        });
    }

    // DELETE
    @Auditable(entityType = "DEVICE", action = "DELETE")
    public boolean deleteDevice(String deviceId) {
        if (deviceRepository.existsById(deviceId)) {
            deviceRepository.deleteById(deviceId);
            return true;
        }
        return false;
    }
    
    @Transactional
    public Optional<DeviceDTO> updateDeviceStatusWithAssignmentHandling(String deviceId, DeviceDTO dto, Long userId, String username) {
        return deviceRepository.findById(deviceId).map(existing -> {
            Device old = DeviceMapper.toEntity(DeviceMapper.toDTO(existing)); // keep old copy

            boolean statusChangedToDecommissioned =
                dto.getStatus() == DeviceStatus.DECOMMISSIONED
                && existing.getStatus() != DeviceStatus.DECOMMISSIONED;

            existing.setType(dto.getType());
            existing.setIpAddress(dto.getIpAddress());
            existing.setLocation(dto.getLocation());
            existing.setModel(dto.getModel());
            existing.setStatus(dto.getStatus());

            Device updated = deviceRepository.save(existing);

            if (statusChangedToDecommissioned) {
                List<Assignment> assignments = assignmentRepository.findByDevice_DeviceId(deviceId);
                assignmentRepository.deleteAll(assignments);
            }

            // Log manually
            String details = "{ \"old\": " + DeviceMapper.toDTO(old) + ", \"new\": " + DeviceMapper.toDTO(updated) + " }";
            auditLogService.log(userId, username, "DEVICE", deviceId, "UPDATE", details);

            return DeviceMapper.toDTO(updated);
        });
    }

}