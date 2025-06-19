package com.jovan.erp_v1.request;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FiscalYearRequest(
        Long id,
        @NotNull @Positive int year,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull FiscalYearStatus yearStatus,
        @NotNull FiscalQuarterStatus quarterStatus,
        @NotNull List<FiscalQuarterRequest> quarters) {
}
