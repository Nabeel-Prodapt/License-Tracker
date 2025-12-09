package com.example.demo.controller;

import com.example.demo.dto.AssignmentRequest;
import com.example.demo.dto.AssignmentResponse;
import com.example.demo.mapper.AssignmentMapper;
import com.example.demo.model.Assignment;
import com.example.demo.service.AssignmentService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER','AUDITOR')")
    public ResponseEntity<List<AssignmentResponse>> getAllAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        List<AssignmentResponse> responses = assignments.stream()
            .map(AssignmentMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Only ADMIN and ENGINEER roles can create assignments
    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER')")
    @PostMapping
    public ResponseEntity<?> assignLicenseToDevice(@Valid @RequestBody AssignmentRequest request) {
        try {
            AssignmentResponse assignment = assignmentService.assignLicenseToDevice(request);
            return ResponseEntity.ok(assignment);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ADMIN, ENGINEER, and AUDITOR roles can view assignments by device
    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER','AUDITOR')")
    @GetMapping("/by-device/{deviceId}")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByDevice(@PathVariable String deviceId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByDevice(deviceId);
        List<AssignmentResponse> responses = assignments.stream()
        		.map(AssignmentMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER','AUDITOR')")
    @GetMapping("/by-license/{licenseKey}")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByLicense(@PathVariable String licenseKey) {
        List<Assignment> assignments = assignmentService.getAssignmentsByLicense(licenseKey);
        List<AssignmentResponse> responses = assignments.stream()
            .map(AssignmentMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

}