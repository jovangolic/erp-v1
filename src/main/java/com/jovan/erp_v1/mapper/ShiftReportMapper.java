package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.ShiftReport;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.ShiftReportRequest;
import com.jovan.erp_v1.response.ShiftReportResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class ShiftReportMapper extends AbstractMapper<ShiftReportRequest> {

    public ShiftReport toEntity(ShiftReportRequest request, User createdBy, Shift shift) {
    	Objects.requireNonNull(request,"ShiftReportRequest must not be null");
    	Objects.requireNonNull(createdBy,"User must not be null");
    	Objects.requireNonNull(shift,"Shift must not be null");
    	validateIdForCreate(request, ShiftReportRequest::id);
        ShiftReport report = new ShiftReport();
        report.setId(request.id());
        report.setDescription(request.description());
        report.setFilePath(request.filePath());
        report.setCreatedBy(createdBy);
        report.setRelatedShift(shift);
        return report;
    }
    
    public ShiftReport toUpdateEntity(ShiftReport report, ShiftReportRequest request, User createdBy, Shift shift) {
    	Objects.requireNonNull(request,"ShiftReportRequest must not be null");
    	Objects.requireNonNull(report,"ShiftReport must not be null");
    	Objects.requireNonNull(createdBy,"User must not be null");
    	Objects.requireNonNull(shift,"Shift must not be null");
    	validateIdForUpdate(request, ShiftReportRequest::id);
    	return buildShiftReportFromRequest(report, request, createdBy, shift);
    }
    
    public ShiftReportResponse toResponse(ShiftReport report) {
    	Objects.requireNonNull(report,"ShiftReport must not be null");
    	return new ShiftReportResponse(report);
    }

    public ShiftReport buildShiftReportFromRequest(ShiftReport report,ShiftReportRequest request, User createdBy, Shift shift) {
    	report.setDescription(request.description());
        report.setFilePath(request.filePath());;
        report.setCreatedBy(createdBy);
        report.setRelatedShift(shift);
        return report;
    }
    
    public List<ShiftReportResponse> toResponseList(List<ShiftReport> r){
    	if(r == null || r.isEmpty()) {
    		return Collections.emptyList();
    	}
    	return r.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
