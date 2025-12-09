package com.example.demo.controller;

import com.example.demo.model.AuditLog;
import com.example.demo.service.AuditLogService;
import com.example.demo.specification.AuditLogSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

import static com.example.demo.specification.AuditLogSpecification.*;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * Fetch audit logs with optional filters
     */
    @GetMapping("/logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public List<AuditLog> getLogs(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String entityId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp to
    ) {

        Specification<AuditLog> spec = Specification
                .where(entityTypeEquals(entityType))
                .and(actionEquals(action))
                .and(usernameEquals(username))
                .and(entityIdEquals(entityId))
                .and(timestampBetween(from, to));

        return auditLogService.findLogs(spec);
    }
}