package com.jovan.erp_v1.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.ShiftReport;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ShiftReportRequest;
import com.jovan.erp_v1.response.ShiftReportResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShiftReportMapper {

	private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;

    public ShiftReport toEntity(ShiftReportRequest request) {
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

    public ShiftReportResponse toDto(ShiftReport report) {
        ShiftReportResponse response = new ShiftReportResponse();
        response.setId(report.getId());
        response.setDescription(report.getDescription());
        response.setCreatedAt(report.getCreatedAt());
        response.setFilePath(report.getFilePath());

        if (report.getCreatedBy() != null) {
            response.setCreatedById(report.getCreatedBy().getId());
            response.setCreatedByUsername(report.getCreatedBy().getEmail());
        }

        if (report.getRelatedShift() != null) {
            response.setRelatedShiftId(report.getRelatedShift().getId());
        }

        return response;
    }
	
}
