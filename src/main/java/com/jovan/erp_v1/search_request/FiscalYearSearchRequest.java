package com.jovan.erp_v1.search_request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.enumeration.FiscalYearTypeStatus;

public record FiscalYearSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		Integer year,
		Integer yearFrom,
		Integer yearTo,
		LocalDate startDate,
		LocalDate startDateBefore,
		LocalDate startDateAfter,
		LocalDate endDate,
		LocalDate endDateBefore,
		LocalDate endDateAfter,
		FiscalYearStatus yearStatus,
		FiscalQuarterStatus quarterStatus,
		Boolean confirmed,
		FiscalYearTypeStatus status
		) {
}
