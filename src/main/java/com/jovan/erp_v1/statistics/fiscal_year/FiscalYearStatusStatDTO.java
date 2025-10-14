package com.jovan.erp_v1.statistics.fiscal_year;

import com.jovan.erp_v1.enumeration.FiscalYearStatus;

public record FiscalYearStatusStatDTO(
		FiscalYearStatus yearStatus,
		Long count
		) {
}
