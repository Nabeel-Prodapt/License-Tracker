package com.example.demo.service;

import com.example.demo.dto.DashboardOverviewDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final DeviceRepository deviceRepository;
    private final LicenseRepository licenseRepository;
    private final AssignmentRepository assignmentRepository;

    public DashboardService(DeviceRepository deviceRepository,
                            LicenseRepository licenseRepository,
                            AssignmentRepository assignmentRepository) {
        this.deviceRepository = deviceRepository;
        this.licenseRepository = licenseRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public DashboardOverviewDTO getDashboardOverview() {
        long totalDevices = deviceRepository.count();
        long totalLicenses = licenseRepository.count();

        // Define devices at risk by status not ACTIVE (customize as needed)
        long devicesAtRisk = deviceRepository.findAll().stream()
                .filter(device -> device.getStatus() != null && device.getStatus() != DeviceStatus.ACTIVE)
                .count();

        // Licenses expiring in next 30 days
        LocalDate today = LocalDate.now();
        LocalDate in30Days = today.plusDays(30);

        List<License> expiringLicenses = licenseRepository.findAll().stream()
            .filter(license -> {
                if (license.getValidTo() == null) return false;
                LocalDate validToDate = license.getValidTo().toLocalDate();
                return (!validToDate.isBefore(today) && !validToDate.isAfter(in30Days));
            })
            .collect(Collectors.toList());

        long licensesExpiring = expiringLicenses.size();

        List<DashboardOverviewDTO.ExpiringLicenseInfo> expiringLicenseInfos = expiringLicenses.stream()
            .map(license -> {
                long devicesUsed = assignmentRepository.findByLicense_LicenseKey(license.getLicenseKey())
                        .stream().map(Assignment::getDevice).distinct().count();
                return new DashboardOverviewDTO.ExpiringLicenseInfo(
                        license.getSoftwareName(),
                        license.getVendor() != null ? license.getVendor().getVendorName() : "Unknown",
                        devicesUsed,
                        license.getValidTo().toString()
                );
            })
            .collect(Collectors.toList());

        return new DashboardOverviewDTO(
                totalDevices,
                totalLicenses,
                devicesAtRisk,
                licensesExpiring,
                expiringLicenseInfos
        );
    }
}