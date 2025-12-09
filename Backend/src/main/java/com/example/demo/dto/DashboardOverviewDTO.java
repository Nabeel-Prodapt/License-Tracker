package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardOverviewDTO {
    private long totalDevices;
    private long totalLicenses;
    private long devicesAtRisk;
    private long licensesExpiring;
    private List<ExpiringLicenseInfo> expiringLicenses;

    @Data
    @AllArgsConstructor
    public static class ExpiringLicenseInfo {
        private String software;
        private String vendor;
        private long devicesUsed;
        private String expiryDate;
    }
}