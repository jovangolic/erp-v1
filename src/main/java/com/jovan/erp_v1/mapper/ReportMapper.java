package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Report;
import com.jovan.erp_v1.request.ReportRequest;
import com.jovan.erp_v1.response.ReportResponse;

@Component
public class ReportMapper {

    public Report toEntity(ReportRequest request) {
        Report report = new Report();
        report.setType(request.type());
        report.setGeneratedAt(request.generatedAt());
        report.setFilePath(request.filePath());
        return report;
    }

    public ReportResponse toResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        if (report.getType() != null) {
            response.setType(report.getType());
        }
        response.setGeneratedAt(report.getGeneratedAt());
        response.setFilePath(report.getFilePath());
        return response;
    }
}
