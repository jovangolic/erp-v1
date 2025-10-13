package com.jovan.erp_v1.request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterTypeStatus;

import jakarta.validation.constraints.NotNull;

public record FiscalQuarterRequest(
        Long id,
        @NotNull FiscalQuarterStatus quarterStatus,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull Long fiscalYearId,
        @NotNull Boolean confirmed,
        @NotNull FiscalQuarterTypeStatus status) {
}
