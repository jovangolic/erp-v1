package com.jovan.erp_v1.service;

import org.springframework.core.io.Resource;

import com.jovan.erp_v1.request.ReportRequest;
import com.jovan.erp_v1.response.ReportResponse;

public interface IReportService {

    ReportResponse generateReport(ReportRequest request);

    Resource downloadReport(Long reportId);
}
