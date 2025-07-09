package com.jovan.erp_v1.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.ShiftReport;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ShiftReportRequest;
import com.jovan.erp_v1.response.ShiftReportResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShiftReportMapper extends AbstractMapper<ShiftReportRequest> {

	private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;

    public ShiftReport toEntity(ShiftReportRequest request) {
    	Objects.requireNonNull(request,"ShiftReportRequest must not be null");
        ShiftReport report = new ShiftReport();
        report.setDescription(request.description());
        report.setCreatedAt(LocalDateTime.now());
        report.setFilePath(request.filePath());
        if (request.createdById() != null) {
            userRepository.findById(request.createdById())
                .ifPresent(report::setCreatedBy);
        }
        if (request.relatedShiftId() != null) {
            shiftRepository.findById(request.relatedShiftId())
                .ifPresent(report::setRelatedShift);
        }
        return report;
    }
    
    public ShiftReport toUpdateEntity(ShiftReport report, ShiftReportRequest request) {
    	Objects.requireNonNull(request,"ShiftReportRequest must not be null");
    	Objects.requireNonNull(report,"ShiftReport must not be null");
    	validateIdForUpdate(request, ShiftReportRequest::id);
    	return buildShiftReportFromRequest(report, request);
    }
    
    public ShiftReportResponse toResponse(ShiftReport report) {
    	Objects.requireNonNull(report,"ShiftReport must not be null");
    	return new ShiftReportResponse(report);
    }

    public ShiftReport buildShiftReportFromRequest(ShiftReport report,ShiftReportRequest request) {
    	report.setDescription(request.description());
    	report.setCreatedAt(request.createdAt());
        report.setFilePath(request.filePath());;
        if (report.getCreatedBy() != null) {
        	report.setCreatedBy(fetchUserId(request.createdById()));
        }
        else {
        	Objects.requireNonNull(request,"ShiftReportRequest must not be null");
        }
        if (report.getRelatedShift() != null) {
            report.setRelatedShift(fetchShiftId(request.relatedShiftId()));
        }
        else {
        	Objects.requireNonNull(report,"ShiftReport must not be null");
        }
        return report;
    }
    
    public List<ShiftReportResponse> toResponseList(List<ShiftReport> r){
    	if(r == null || r.isEmpty()) {
    		return Collections.emptyList();
    	}
    	return r.stream().map(this::toResponse).collect(Collectors.toList());
    }
    
    private User fetchUserId(Long userId) {
    	if(userId == null) {
    		throw new UserNotFoundException("User ID must not be null");
    	}
    	return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id "+ userId));
    }
    
    private Shift fetchShiftId(Long shiftId) {
    	if(shiftId == null) {
    		throw new NoSuchShiftErrorException("Shift ID must not be null");
    	}
    	return shiftRepository.findById(shiftId).orElseThrow(() -> new NoSuchShiftErrorException("Shift not found with id "+shiftId));
    }
	
}
