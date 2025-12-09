package com.example.demo.controller;

import com.example.demo.dto.DeviceDTO;
import com.example.demo.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "http://localhost:5173")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // CREATE - Admin/Engineer only
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<?> createDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        DeviceDTO created = deviceService.createDevice(deviceDTO);
        return ResponseEntity.ok(created);
    }

    // READ ALL with manual pagination and filters
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER', 'AUDITOR')")
    public ResponseEntity<Map<String, Object>> getAllDevices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type) {

        List<DeviceDTO> allDevices = deviceService.getFilteredDevices(location, type);

        int totalElements = allDevices.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = page * size;
        int end = Math.min(start + size, totalElements);

        List<DeviceDTO> pagedDevices;
        if (start >= totalElements) {
            pagedDevices = List.of();
        } else {
            pagedDevices = allDevices.subList(start, end);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", pagedDevices);
        response.put("currentPage", page);
        response.put("totalItems", totalElements);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    // READ ONE
    @GetMapping("/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER', 'AUDITOR')")
    public ResponseEntity<?> getDeviceById(@PathVariable String deviceId) {
        return deviceService.getDeviceById(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - Admin/Engineer only
    @PutMapping("/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    public ResponseEntity<?> updateDevice(
            @PathVariable String deviceId,
            @Valid @RequestBody DeviceDTO deviceDTO) {

        return deviceService.updateDevice(deviceId, deviceDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Admin only
    @DeleteMapping("/{deviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDevice(@PathVariable String deviceId) {
        if (deviceService.deleteDevice(deviceId)) {
            return ResponseEntity.ok("Device deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}