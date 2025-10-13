package com.jovan.erp_v1.search_request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterTypeStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

public record FiscalQuarterSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		LocalDate startDate,
		LocalDate startDateBefore,
		LocalDate startDateAfter,
		LocalDate endDate,
		LocalDate endDateBefore,
		LocalDate endDateAfter,
		Long fiscalYearId,
		Long fiscalYearIdFrom,
		Long fiscalYearIdTo,
		Integer year,
		Integer yearFrom,
		Integer yearTo,
		LocalDate fiscalStartDate,
		LocalDate fiscalStartDateBefore,
		LocalDate fiscalStartDateAfter,
		LocalDate fiscalEndDate,
		LocalDate fiscalEndDateBefore,
		LocalDate fiscalEndDateAfter,
		FiscalYearStatus yearStatus,
		FiscalQuarterStatus fiscalQuarterStatus,
		FiscalQuarterStatus quarterStatus,
		Boolean confirmed,
		FiscalQuarterTypeStatus status
		) {
}
