package com.example.demo.mapper;

import com.example.demo.dto.AssignmentResponse;
import com.example.demo.model.Assignment;

public class AssignmentMapper {

    public static AssignmentResponse toResponse(Assignment assignment) {
        return new AssignmentResponse(
            assignment.getAssignmentId(),
            assignment.getDevice().getDeviceId(),
            assignment.getLicense().getLicenseKey(),
            assignment.getAssignedOn()
        );
    }
}