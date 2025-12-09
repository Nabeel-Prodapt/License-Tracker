package com.example.demo.mapper;

import com.example.demo.dto.LicenseDTO;
import com.example.demo.model.License;
import com.example.demo.model.LicenseType;
import com.example.demo.repository.VendorRepository;

public class LicenseMapper {

    public static LicenseDTO toDTO(License license) {
        LicenseDTO dto = new LicenseDTO();
        dto.setLicenseKey(license.getLicenseKey());
        dto.setSoftwareName(license.getSoftwareName());
        dto.setValidFrom(license.getValidFrom());
        dto.setValidTo(license.getValidTo());
        dto.setLicenseType(license.getLicenseType());
        dto.setMaxUsage(license.getMaxUsage());
        dto.setNotes(license.getNotes());
        dto.setVendorId(license.getVendor() != null ? license.getVendor().getVendorId() : null);
        return dto;
    }

    public static License toEntity(LicenseDTO dto, VendorRepository vendorRepository) {
        License license = new License();
        license.setLicenseKey(dto.getLicenseKey());
        license.setSoftwareName(dto.getSoftwareName());
        license.setValidFrom(dto.getValidFrom());
        license.setValidTo(dto.getValidTo());
        license.setLicenseType(dto.getLicenseType() != null ? dto.getLicenseType() : LicenseType.PER_DEVICE);
        license.setMaxUsage(dto.getMaxUsage());
        license.setNotes(dto.getNotes());

        if (dto.getVendorId() != null) {
            vendorRepository.findById(dto.getVendorId()).ifPresent(license::setVendor);
        }

        return license;
    }
}