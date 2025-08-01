package com.jovan.erp_v1.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Report;
import com.jovan.erp_v1.request.ReportRequest;
import com.jovan.erp_v1.response.ReportResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class ReportMapper extends AbstractMapper<ReportRequest> {

    public Report toEntity(ReportRequest request,String filePath) {
    	Objects.requireNonNull(request, "ReportRequest must not be null");
    	validateIdForCreate(request, ReportRequest::id);
        Report report = new Report();
        report.setType(request.type());
        report.setFilePath(filePath);
        return report;
    }
    
    public Report toEntityUpdate(Report report, ReportRequest request,String filePath) {
    	Objects.requireNonNull(report, "Report must not be null");
    	Objects.requireNonNull(request, "ReportRequest must not be null");
    	validateIdForUpdate(request, ReportRequest::id);
    	report.setType(request.type());
    	report.setFilePath(filePath);
    	return report;
    }

    public ReportResponse toResponse(Report report) {
    	Objects.requireNonNull(report, "Report must not be null");
        return new ReportResponse(report);
    }
}
