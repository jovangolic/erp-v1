package com.jovan.erp_v1.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.mapper.ReportMapper;
import com.jovan.erp_v1.model.Report;
import com.jovan.erp_v1.repository.ReportRepository;
import com.jovan.erp_v1.request.ReportRequest;
import com.jovan.erp_v1.response.ReportResponse;

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
}
