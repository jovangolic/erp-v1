package com.jovan.erp_v1.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.response.DriverReportResponse;
import com.jovan.erp_v1.service.DriverReportService;
import com.jovan.erp_v1.util.RoleGroups;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver-report")
@PreAuthorize(RoleGroups.DRIVER_REPORT_FULL_ACCESS)
public class DriverReportController {

	private final DriverReportService driverReportService;
	
	@GetMapping("/{driverId}/report")
    public ResponseEntity<DriverReportResponse> getDriverReport(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverReportService.generateDriverReport(driverId));
    }
	
	@GetMapping("/{driverId}/report/excel")
	public ResponseEntity<byte[]> downloadDriverReportExcel(@PathVariable Long driverId) throws IOException {
	    DriverReportResponse report = driverReportService.generateDriverReport(driverId);
	    ByteArrayInputStream in = driverReportService.exportDriverReportToExcel(report);
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=driver-report.xlsx");
	    return ResponseEntity.ok()
	            .headers(headers)
	            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	            .body(in.readAllBytes());
	}

	@GetMapping("/{driverId}/report/pdf")
	public ResponseEntity<byte[]> downloadDriverReportPdf(@PathVariable Long driverId) {
	    DriverReportResponse report = driverReportService.generateDriverReport(driverId);
	    ByteArrayInputStream in = driverReportService.exportDriverReportToPdf(report);
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=driver-report.pdf");
	    return ResponseEntity.ok()
	            .headers(headers)
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(in.readAllBytes());
	}
}
