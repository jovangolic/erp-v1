package com.jovan.erp_v1.save_as;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

import jakarta.validation.constraints.NotNull;

public record FiscalYearSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull Integer year,
		@NotNull LocalDate endDate,
		@NotNull FiscalYearStatus yearStatus,
		@NotNull FiscalQuarterStatus quarterStatus
		) {
}
