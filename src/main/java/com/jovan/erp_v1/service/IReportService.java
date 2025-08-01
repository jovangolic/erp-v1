package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.io.Resource;

import com.jovan.erp_v1.enumeration.ReportType;
import com.jovan.erp_v1.request.ReportRequest;
import com.jovan.erp_v1.response.ReportResponse;

public interface IReportService {

    ReportResponse generateReport(ReportRequest request);
    ReportResponse updateReport(Long id, ReportRequest request);
    Resource downloadReport(Long reportId);
    ReportResponse getReportById(Long id);
    List<ReportResponse> getReportsByType(ReportType type);
    List<ReportResponse> getReportsBetweenDates(LocalDateTime from, LocalDateTime to);
}
