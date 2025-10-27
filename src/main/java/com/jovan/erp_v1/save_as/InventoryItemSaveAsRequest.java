package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record InventoryItemSaveAsRequest(
		@NotNull Long productId,
	    @NotNull BigDecimal quantity,
	    @NotNull BigDecimal itemCondition
		) {
}
