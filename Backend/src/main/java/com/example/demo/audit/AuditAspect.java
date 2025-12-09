package com.example.demo.audit;

import com.example.demo.model.AuditLog;
import com.example.demo.model.User;
import com.example.demo.service.AuditLogService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    // ThreadLocal to store "old state" before method executes
    private final ThreadLocal<Object> oldState = new ThreadLocal<>();

    @Before("@annotation(auditable)")
    public void captureOldState(JoinPoint joinPoint, Auditable auditable) {
        String action = auditable.action();

        // Only capture for UPDATE/DELETE
        if (!action.equals("UPDATE") && !action.equals("DELETE")) return;

        if (joinPoint.getArgs().length > 0) {
            Object arg = joinPoint.getArgs()[0];
            oldState.set(arg); // store old state
        }
    }

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Object result, Auditable auditable) throws Exception {

        String entityType = auditable.entityType();
        String action = auditable.action();

        // Fetch authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "SYSTEM";

        Long userId = null;
        if (auth != null) {
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent()) {
                userId = userOpt.get().getUserId();
            }
        }


        // Determine entityId (first argument or returned object)
        String entityId = "";
        if (joinPoint.getArgs().length > 0) {
            try {
                entityId = objectMapper.writeValueAsString(joinPoint.getArgs()[0]);
            } catch (Exception e) {
                entityId = joinPoint.getArgs()[0].toString();
            }
        }

        // Capture old and new state
        Map<String, Object> detailsMap = new HashMap<>();

        switch (action) {
            case "UPDATE" -> {
                Object oldObj = oldState.get();
                detailsMap.put("old", oldObj);
                detailsMap.put("new", result);
                oldState.remove(); // clear ThreadLocal
            }
            case "DELETE" -> {
                detailsMap.put("old", joinPoint.getArgs()[0]);
                detailsMap.put("new", null);
            }
            default -> { // CREATE
                detailsMap.put("old", null);
                detailsMap.put("new", result);
            }
        }

        String details = objectMapper.writeValueAsString(detailsMap);

        auditLogService.log(userId, username, entityType, entityId, action, details);
    }
}