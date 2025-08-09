package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.ReportType;
import com.jovan.erp_v1.request.ReportRequest;
import com.jovan.erp_v1.response.ReportResponse;
import com.jovan.erp_v1.service.IReportService;
import com.jovan.erp_v1.util.RoleGroups;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@PreAuthorize(RoleGroups.REPORT_READ_ACCESS)
public class ReportController {

    private final IReportService reportService;

    @PreAuthorize(RoleGroups.REPORT_FULL_ACCESS)
    @PostMapping("/generate")
    public ResponseEntity<ReportResponse> generate(@RequestBody ReportRequest request) {
        return ResponseEntity.ok(reportService.generateReport(request));
    }

    @PreAuthorize(RoleGroups.REPORT_READ_ACCESS)
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Resource resource = reportService.downloadReport(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .body(resource);
    }

    @PreAuthorize(RoleGroups.REPORT_READ_ACCESS)
    @GetMapping("/get/{id}")
    public ResponseEntity<ReportResponse> getById(@PathVariable Long id) {
        ReportResponse response = reportService.getReportById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.REPORT_READ_ACCESS)
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ReportResponse>> getByType(@PathVariable ReportType type) {
        List<ReportResponse> response = reportService.getReportsByType(type);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.REPORT_READ_ACCESS)
    @GetMapping("/date-range")
    public ResponseEntity<List<ReportResponse>> getReportsBetweenDates(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<ReportResponse> responses = reportService.getReportsBetweenDates(from, to);
        return ResponseEntity.ok(responses);
    }
}
