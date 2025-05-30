package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.ReportType;
import com.jovan.erp_v1.model.Report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private Long id;
    private ReportType type;
    private LocalDateTime generatedAt;
    private String filePath;

    public ReportResponse(Report report) {
        this.id = report.getId();
        this.type = report.getType();
        this.generatedAt = report.getGeneratedAt();
        this.filePath = report.getFilePath();
    }
}
