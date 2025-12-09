package com.example.demo.repository;

import java.sql.Date;
import java.util.List;
import com.example.demo.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LicenseRepository extends JpaRepository<License, String> {
	@Query("SELECT l FROM License l JOIN FETCH l.vendor WHERE (:vendorId IS NULL OR l.vendor.vendorId = :vendorId)")
	List<License> findByVendorId(@Param("vendorId") Long vendorId);
    List<License> findByValidToBetween(Date start, Date end);
}
