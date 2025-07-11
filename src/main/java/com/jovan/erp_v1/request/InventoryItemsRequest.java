package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryItemsRequest(
		
		Long id,
		@NotNull(message = "Inventory ID is required")
	    Long inventoryId,

	    @NotNull(message = "Product ID is required")
	    Long productId,

	    @NotNull(message = "Quantity is required")
		@DecimalMin(value = "0.0", inclusive = true, message = "Quantity must be at least 0")
		BigDecimal quantity,

	    @NotNull(message = "Condition is required")
	    @Min(value = 0, message = "Condition must be at least 0")
	    Integer condition
		) {
}
