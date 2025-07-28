package com.jovan.erp_v1.request;

import java.math.BigDecimal;

public record InventoryItemCalculateRequest(
		Long id,
		BigDecimal quantity,
		BigDecimal itemCondition
		) {
}
