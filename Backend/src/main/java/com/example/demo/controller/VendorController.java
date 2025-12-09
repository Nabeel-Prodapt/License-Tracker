package com.example.demo.controller;

import com.example.demo.model.Vendor;
import com.example.demo.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:5173")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER', 'AUDITOR')")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @GetMapping("/{vendorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER', 'AUDITOR')")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long vendorId) {
        return vendorService.getVendorById(vendorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<Vendor> createVendor(@Valid @RequestBody Vendor vendor) {
        Vendor created = vendorService.createVendor(vendor);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{vendorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long vendorId, @Valid @RequestBody Vendor vendor) {
        return vendorService.updateVendor(vendorId, vendor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{vendorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteVendor(@PathVariable Long vendorId) {
        if (vendorService.deleteVendor(vendorId)) {
            return ResponseEntity.ok("Vendor deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}