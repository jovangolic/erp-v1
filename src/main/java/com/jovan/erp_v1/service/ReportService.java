package com.jovan.erp_v1.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.ReportType;
import com.jovan.erp_v1.exception.ReportErrorException;
import com.jovan.erp_v1.mapper.ReportMapper;
import com.jovan.erp_v1.model.Report;
import com.jovan.erp_v1.repository.ReportRepository;
import com.jovan.erp_v1.request.ReportRequest;
import com.jovan.erp_v1.response.ReportResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Override
    public ReportResponse generateReport(ReportRequest request) {
        // Simulacija generisanja izveštaja
        String filePath = "reports/" + UUID.randomUUID() + ".pdf"; // generiši .pdf fajl

        Report report = new Report();
        report.setType(request.type());
        report.setGeneratedAt(LocalDateTime.now());
        report.setFilePath(filePath);

        reportRepository.save(report);
        return reportMapper.toResponse(report);
    }

    @Override
    public Resource downloadReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        try {
            Path path = Paths.get(report.getFilePath());
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found", e);
        }
    }

    @Override
    public ReportResponse getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportErrorException("Report not found with ID: " + id));
        return new ReportResponse(report);
    }

    @Override
    public List<ReportResponse> getReportsByType(ReportType type) {
    	validateReportType(type);
        return reportRepository.findByType(type)
                .stream()
                .map(ReportResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getReportsBetweenDates(LocalDateTime from, LocalDateTime to) {
    	DateValidator.validateRange(from, to);
        return reportRepository.findAll()
                .stream()
                .filter(report -> report.getGeneratedAt() != null &&
                        !report.getGeneratedAt().isBefore(from) &&
                        !report.getGeneratedAt().isAfter(to))
                .map(ReportResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateReportType(ReportType type) {
    	if(type == null) {
    		throw new IllegalArgumentException("ReportType type must not be null");
    	}
    }
}
