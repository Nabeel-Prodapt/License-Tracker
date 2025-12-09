package com.example.demo.controller;

import com.example.demo.dto.DeviceDTO;
import com.example.demo.dto.LicenseDTO;
import com.example.demo.service.LicenseReportService;
import com.example.demo.service.ReportService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportsController {

    private final ReportService reportService;
    private final LicenseReportService licenseReportService;

    public ReportsController(ReportService reportService,
                             LicenseReportService licenseReportService) {
        this.reportService = reportService;
        this.licenseReportService = licenseReportService;
    }

    // Export Non-Compliant Devices as PDF (download as attachment)
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR')")
    @GetMapping("/non-compliant-devices/export/pdf")
    public ResponseEntity<byte[]> exportNonCompliantDevicesPdf() throws IOException, DocumentException {
        List<DeviceDTO> devices = reportService.getNonCompliantDevices();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        document.add(new Paragraph("Non-Compliant Devices Report", titleFont));
        document.add(new Paragraph(" "));

        for (DeviceDTO device : devices) {
            document.add(new Paragraph("Device ID: " + device.getDeviceId()));
            document.add(new Paragraph("Type: " + device.getType()));
            document.add(new Paragraph("Status: " + device.getStatus()));
            document.add(new Paragraph("Location: " + device.getLocation()));
            document.add(new Paragraph("Model: " + device.getModel()));
            document.add(new Paragraph(" "));
        }
        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=non_compliant_devices.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }

    // Export License Usage as CSV (download as attachment)
    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER','AUDITOR')")
    @GetMapping("/license-usage/export/csv")
    public void exportLicenseUsageCsv(@RequestParam(required = false) Long vendorId,
                                      HttpServletResponse response) throws IOException {
        List<LicenseDTO> licenses = licenseReportService.getLicenseUsageByVendor(vendorId);

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=license_usage.csv");
        try (
            OutputStreamWriter osWriter = new OutputStreamWriter(response.getOutputStream());
            CSVWriter writer = new CSVWriter(osWriter)
        ) {
            String[] header = {"License Key", "Software Name", "Valid From", "Valid To", "License Type", "Max Usage", "Vendor ID"};
            writer.writeNext(header);

            for (LicenseDTO license : licenses) {
                String[] line = {
                        license.getLicenseKey(),
                        license.getSoftwareName(),
                        String.valueOf(license.getValidFrom()),
                        String.valueOf(license.getValidTo()),
                        license.getLicenseType().name(),
                        String.valueOf(license.getMaxUsage()),
                        license.getVendorId() != null ? String.valueOf(license.getVendorId()) : ""
                };
                writer.writeNext(line);
            }
            writer.flush();
        }
    }
}