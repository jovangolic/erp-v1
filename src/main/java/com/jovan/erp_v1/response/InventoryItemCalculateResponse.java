package com.jovan.erp_v1.response;

import java.math.BigDecimal;

public record InventoryItemCalculateResponse(
		Long id,
		BigDecimal quantity,
		BigDecimal itemCondition,
		BigDecimal diference
		) {
	public InventoryItemCalculateResponse(Long id, BigDecimal quantity, BigDecimal itemCondition) {
        this(id, quantity, itemCondition, quantity.subtract(itemCondition).max(BigDecimal.ZERO));
    }
}
