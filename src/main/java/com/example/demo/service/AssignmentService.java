package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.dto.AssignmentRequest;
import com.example.demo.dto.AssignmentResponse;
import com.example.demo.mapper.AssignmentMapper;
import com.example.demo.model.Assignment;
import com.example.demo.model.Device;
import com.example.demo.model.License;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.LicenseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final DeviceRepository deviceRepository;
    private final LicenseRepository licenseRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             DeviceRepository deviceRepository,
                             LicenseRepository licenseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.deviceRepository = deviceRepository;
        this.licenseRepository = licenseRepository;
    }

    @Transactional
    public AssignmentResponse assignLicenseToDevice(AssignmentRequest request) {
        Device device = deviceRepository.findById(request.getDeviceId())
            .orElseThrow(() -> new RuntimeException("Device not found"));
        License license = licenseRepository.findById(request.getLicenseKey())
            .orElseThrow(() -> new RuntimeException("License not found"));

        int currentAssignmentCount = assignmentRepository.countAssignmentsByLicenseKey(request.getLicenseKey());

        if (currentAssignmentCount + 1 > license.getMaxUsage()) {
            throw new RuntimeException("Cannot assign license: max usage exceeded");
        }

        Optional<Assignment> assignmentOpt = assignmentRepository.findByDevice_DeviceIdAndLicense_LicenseKey(
            request.getDeviceId(), request.getLicenseKey());

        if (assignmentOpt.isPresent()) {
            throw new RuntimeException("This license is already assigned to this device.");
        }

        Assignment assignment = new Assignment();
        assignment.setDevice(device);
        assignment.setLicense(license);
        assignment.setAssignedOn(java.time.Instant.now());

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return AssignmentMapper.toResponse(savedAssignment);
    }


    public List<Assignment> getAssignmentsByDevice(String deviceId) {
        return assignmentRepository.findByDevice_DeviceId(deviceId);
    }
    
    public List<Assignment> getAssignmentsByLicense(String licenseKey) {
        return assignmentRepository.findByLicense_LicenseKey(licenseKey);
    }
    
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }


}