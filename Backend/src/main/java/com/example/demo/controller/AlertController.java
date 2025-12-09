package com.example.demo.controller;

import com.example.demo.dto.LicenseDTO;
import com.example.demo.service.LicenseAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:5173")
public class AlertController {

    private final LicenseAlertService licenseAlertService;

    public AlertController(LicenseAlertService licenseAlertService) {
        this.licenseAlertService = licenseAlertService;
    }

    /**
     * GET /alerts?days={days}
     * Returns licenses expiring within the next 'days' days (default 30).
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER', 'AUDITOR')")
    @GetMapping
    public ResponseEntity<List<LicenseDTO>> getAlerts(@RequestParam(defaultValue = "30") int days) {
        List<LicenseDTO> expiringLicenses = licenseAlertService.findExpiringLicenses(days);
        return ResponseEntity.ok(expiringLicenses);
    }
}