package com.example.demo.service;

import com.example.demo.dto.LicenseDTO;
import com.example.demo.mapper.LicenseMapper;
import com.example.demo.model.License;
import com.example.demo.repository.LicenseRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenseAlertService {

    private final LicenseRepository licenseRepository;

    public LicenseAlertService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    /**
     * Find licenses expiring within the next 'days' days.
     */
    public List<LicenseDTO> findExpiringLicenses(int days) {
        LocalDate now = LocalDate.now();
        LocalDate expiryThreshold = now.plusDays(days);
        List<License> licenses = licenseRepository.findByValidToBetween(
            Date.valueOf(now), Date.valueOf(expiryThreshold));
        return licenses.stream()
                .map(LicenseMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Scheduled method runs daily at 1:00 AM to check for licenses expiring in 7 days,
     * and triggers notifications or alerts as needed.
     */
    @Scheduled(cron = "0 0 1 * * *") // daily at 1 AM
    public void dailyLicenseExpiryCheck() {
        List<LicenseDTO> expiringSoon = findExpiringLicenses(7);

        if (!expiringSoon.isEmpty()) {
            // For demonstration, print to log (replace with real notifications/email etc.)
            System.out.println("License expiry alert! Licenses expiring within 7 days: " + expiringSoon.size());

            // TODO: Integrate notification system (email, push, etc.) here

            // Optional: save alerts in DB if you want persistent user alerts
        }
    }
}