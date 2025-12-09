package com.example.demo.service;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Save a new audit log entry
     * @param userId ID of the user performing the action
     * @param username Name of the user performing the action
     * @param entityType Type of the entity (DEVICE, LICENSE, ASSIGNMENT)
     * @param entityId ID of the entity
     * @param action Action performed (CREATE, UPDATE, DELETE)
     * @param details JSON string with old and new state
     * @return saved AuditLog entity
     */
    public AuditLog log(Long userId, String username, String entityType, String entityId,
                        String action, String details) {

        AuditLog auditLog = AuditLog.builder()
                .userId(userId != null ? userId : 0L)        // fallback to 0 if null
                .username(username != null ? username : "")  // fallback to empty string if null
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .details(details)
                .build();

        return auditLogRepository.save(auditLog);
    }

    /**
     * Fetch audit logs with optional filtering using Specification
     * @param spec Specification filter, can be null to fetch all
     * @return list of AuditLog entries
     */
    public List<AuditLog> findLogs(Specification<AuditLog> spec) {
        if (spec != null) {
            return auditLogRepository.findAll(spec);
        } else {
            return auditLogRepository.findAll();
        }
    }
}