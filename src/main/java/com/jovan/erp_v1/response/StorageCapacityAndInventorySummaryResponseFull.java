package com.jovan.erp_v1.response;

import java.math.BigDecimal;

public record StorageCapacityAndInventorySummaryResponseFull(
		Long storageId,
	    BigDecimal capacity,
	    BigDecimal usedCapacity,
	    BigDecimal availableCapacity,
	    BigDecimal totalItemQuantity
		) {
}
