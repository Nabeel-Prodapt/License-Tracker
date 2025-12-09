package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "license")
public class License {

    @Id
    @Size(max = 50)
    @Column(name = "license_key", nullable = false, unique = true, length = 50)
    private String licenseKey;

    @NotBlank
    @Size(max = 100)
    @Column(name = "software_name", nullable = false)
    private String softwareName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonBackReference  // Prevent recursion by ignoring back reference on serialization
    private Vendor vendor;

    @NotNull
    @Column(name = "valid_from", nullable = false)
    private Date validFrom;

    @NotNull
    @Column(name = "valid_to", nullable = false)
    private Date validTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_type", nullable = false, length = 20)
    private LicenseType licenseType;

    @Min(1)
    @Column(name = "max_usage", nullable = false)
    private Integer maxUsage;

    @Lob
    private String notes;
}