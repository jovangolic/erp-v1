package com.jovan.erp_v1.save_as;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterTypeStatus;

import jakarta.validation.constraints.NotNull;

public record FiscalQuarterSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull FiscalQuarterStatus quarterStatus,
		@NotNull LocalDate endDate,
		@NotNull Long fiscalYearId,
		@NotNull FiscalQuarterTypeStatus status,
		@NotNull Boolean confirmed
		) {
}
