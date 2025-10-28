package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.InventoryItemsStatus;

import jakarta.validation.constraints.NotNull;

public record InventoryItemsSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull Long inventoryId,
		@NotNull Long productId,
		@NotNull BigDecimal quantity,
		@NotNull BigDecimal itemCondition,
		@NotNull Boolean confirmed,
		@NotNull InventoryItemsStatus status 
		) {
}
