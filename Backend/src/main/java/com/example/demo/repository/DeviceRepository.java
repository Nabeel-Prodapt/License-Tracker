package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByLocationContainingIgnoreCase(String location);
    List<Device> findByTypeContainingIgnoreCase(String type);
    List<Device> findByLocationContainingIgnoreCaseAndTypeContainingIgnoreCase(String location, String type);
}