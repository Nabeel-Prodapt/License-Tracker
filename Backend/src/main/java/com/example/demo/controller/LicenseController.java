package com.example.demo.controller;

import com.example.demo.dto.LicenseDTO;
import com.example.demo.service.LicenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/licenses")
@CrossOrigin(origins = "http://localhost:5173")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<?> createLicense(@Valid @RequestBody LicenseDTO licenseDTO) {
        LicenseDTO created = licenseService.createLicense(licenseDTO);
        return ResponseEntity.ok(created);
    }

    // READ ALL with manual pagination and filters
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER', 'AUDITOR')")
    public ResponseEntity<Map<String, Object>> getAllLicenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long vendorId) {

        List<LicenseDTO> allLicenses = licenseService.getLicensesByVendor(vendorId);

        int totalElements = allLicenses.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = page * size;
        int end = Math.min(start + size, totalElements);

        List<LicenseDTO> pagedLicenses;
        if (start >= totalElements) {
            pagedLicenses = List.of();
        } else {
            pagedLicenses = allLicenses.subList(start, end);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", pagedLicenses);
        response.put("currentPage", page);
        response.put("totalItems", totalElements);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    // READ ONE
    @GetMapping("/{licenseKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER', 'AUDITOR')")
    public ResponseEntity<?> getLicenseById(@PathVariable String licenseKey) {
        return licenseService.getLicenseByKey(licenseKey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{licenseKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<?> updateLicense(@PathVariable String licenseKey, @Valid @RequestBody LicenseDTO licenseDTO) {
        return licenseService.updateLicense(licenseKey, licenseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{licenseKey}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteLicense(@PathVariable String licenseKey) {
        if (licenseService.deleteLicense(licenseKey)) {
            return ResponseEntity.ok("License deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}