package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.ReportType;

import jakarta.validation.constraints.NotNull;

public record ReportRequest(

        Long id,
        @NotNull ReportType type,
        LocalDateTime generatedAt
        ) {

}
