package com.example.demo.service;

import com.example.demo.dto.LicenseDTO;
import com.example.demo.mapper.LicenseMapper;
import com.example.demo.model.License;
import com.example.demo.repository.LicenseRepository;
import com.example.demo.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final VendorRepository vendorRepository;

    public LicenseService(LicenseRepository licenseRepository, VendorRepository vendorRepository) {
        this.licenseRepository = licenseRepository;
        this.vendorRepository = vendorRepository;
    }

    // CREATE
    public LicenseDTO createLicense(LicenseDTO dto) {
        License license = LicenseMapper.toEntity(dto, vendorRepository);
        License saved = licenseRepository.save(license);
        return LicenseMapper.toDTO(saved);
    }

    // READ ALL
    public List<LicenseDTO> getAllLicenses() {
        return licenseRepository.findAll()
                .stream()
                .map(LicenseMapper::toDTO)
                .toList();
    }

    // FILTER by vendor ID
    public List<LicenseDTO> getLicensesByVendor(Long vendorId) {
        List<License> licenses;
        if (vendorId != null) {
            licenses = licenseRepository.findByVendorId(vendorId);
        } else {
            licenses = licenseRepository.findAll();
        }

        return licenses.stream()
                .map(LicenseMapper::toDTO)
                .toList();
    }

    // READ ONE
    public Optional<LicenseDTO> getLicenseByKey(String licenseKey) {
        return licenseRepository.findById(licenseKey).map(LicenseMapper::toDTO);
    }

    // UPDATE
    public Optional<LicenseDTO> updateLicense(String licenseKey, LicenseDTO dto) {
        return licenseRepository.findById(licenseKey).map(existing -> {
            existing.setSoftwareName(dto.getSoftwareName());
            existing.setValidFrom(dto.getValidFrom());
            existing.setValidTo(dto.getValidTo());
            existing.setLicenseType(dto.getLicenseType());
            existing.setMaxUsage(dto.getMaxUsage());
            existing.setNotes(dto.getNotes());

            if (dto.getVendorId() != null) {
                vendorRepository.findById(dto.getVendorId()).ifPresent(existing::setVendor);
            }

            License updated = licenseRepository.save(existing);
            return LicenseMapper.toDTO(updated);
        });
    }

    // DELETE
    public boolean deleteLicense(String licenseKey) {
        if (licenseRepository.existsById(licenseKey)) {
            licenseRepository.deleteById(licenseKey);
            return true;
        }
        return false;
    }
}