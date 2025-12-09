package com.example.demo.service;

import com.example.demo.dto.LicenseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenseReportService {

    private final LicenseService licenseService;

    public LicenseReportService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    /**
     * Get license usage filtered by vendor ID.
     */
    public List<LicenseDTO> getLicenseUsageByVendor(Long vendorId) {
        return licenseService.getLicensesByVendor(vendorId);
    }
}