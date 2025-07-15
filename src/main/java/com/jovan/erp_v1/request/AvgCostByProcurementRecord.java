package com.jovan.erp_v1.request;

import java.math.BigDecimal;

public record AvgCostByProcurementRecord(
		Long procurementId,
	    BigDecimal avgCost
		) {
}
