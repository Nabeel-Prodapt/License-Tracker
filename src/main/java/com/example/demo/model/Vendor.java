package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vendor")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Long vendorId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "vendor_name", nullable = false, unique = true)
    private String vendorName; // e.g., Cisco, Palo Alto

    @Email
    @Size(max = 100)
    @Column(name = "support_email", nullable = false)
    private String supportEmail;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    @JsonManagedReference  // Manages forward reference for serialization
    private List<License> licenses;
}
