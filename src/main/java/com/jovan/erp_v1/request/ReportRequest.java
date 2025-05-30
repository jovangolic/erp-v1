package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.ReportType;

public record ReportRequest(

        Long id,
        ReportType type,
        LocalDateTime generatedAt,
        String filePath) {

}
