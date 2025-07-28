package com.jovan.erp_v1.response;

import java.math.BigDecimal;

public record InventorySummaryResponse(
		Long inventoryId,
	    Long itemCount,
	    BigDecimal totalQuantity
		) {
}
