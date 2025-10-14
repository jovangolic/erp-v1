package com.jovan.erp_v1.statistics.fiscal_year;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;

public record FiscalYearQuarterStatDTO(
		FiscalQuarterStatus quarterStatus,
		Long count
		) {
}
