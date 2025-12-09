package com.example.demo.specification;

import com.example.demo.model.AuditLog;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;

public class AuditLogSpecification {

    public static Specification<AuditLog> entityTypeEquals(String entityType) {
        return (root, query, cb) -> entityType == null ? null : cb.equal(root.get("entityType"), entityType);
    }

    public static Specification<AuditLog> actionEquals(String action) {
        return (root, query, cb) -> action == null ? null : cb.equal(root.get("action"), action);
    }

    public static Specification<AuditLog> usernameEquals(String username) {
        return (root, query, cb) -> username == null ? null : cb.equal(root.get("username"), username);
    }

    public static Specification<AuditLog> entityIdEquals(String entityId) {
        return (root, query, cb) -> entityId == null ? null : cb.equal(root.get("entityId"), entityId);
    }

    public static Specification<AuditLog> timestampBetween(Timestamp from, Timestamp to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            if (from != null && to != null) return cb.between(root.get("timestamp"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("timestamp"), from);
            return cb.lessThanOrEqualTo(root.get("timestamp"), to);
        };
    }
}