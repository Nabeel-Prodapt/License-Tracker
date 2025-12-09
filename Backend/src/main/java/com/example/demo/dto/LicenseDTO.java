package com.example.demo.dto;

import com.example.demo.model.LicenseType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;

@Data
public class LicenseDTO {
    
    @NotBlank(message = "License key is required")
    @Size(max = 50)
    private String licenseKey;
    
    @NotBlank(message = "Software name is required")
    @Size(max = 100)
    private String softwareName;
    
    @NotNull(message = "Vendor ID is required")
    private Long vendorId;
    
    @NotNull(message = "Valid from date is required")
    private Date validFrom;
    
    @NotNull(message = "Valid to date is required")
    private Date validTo;
    
    @NotNull(message = "License type is required")
    private LicenseType licenseType;
    
    @NotNull(message = "Max usage is required")
    @Min(value = 1, message = "Max usage must be at least 1")
    private Integer maxUsage;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}