package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Assignment;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

	@Query("SELECT COUNT(a) FROM Assignment a WHERE a.license.licenseKey = :licenseKey")
	int countAssignmentsByLicenseKey(@Param("licenseKey") String licenseKey);


    Optional<Assignment> findByDevice_DeviceIdAndLicense_LicenseKey(String deviceId, String licenseKey);

    List<Assignment> findByDevice_DeviceId(String deviceId);
    
    List<Assignment> findByLicense_LicenseKey(String licenseKey);

}