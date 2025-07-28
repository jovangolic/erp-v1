package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record InventoryItemsRequest(
		Long id,
		@NotNull(message = "Inventory ID is required")
	    Long inventoryId,
	    @NotNull(message = "Product ID is required")
	    Long productId,
	    @NotNull(message = "Quantity is required")
	    @DecimalMin(value = "0.0", inclusive = true, message = "Quantity must be at least 0")
	    @Digits(integer = 10, fraction = 2, message = "Maximum 10 digits before and 2 after decimal point")
	    BigDecimal quantity,
	    @NotNull(message = "Condition is required")
	    @DecimalMin(value = "0.0", inclusive = true, message = "Condition must be at least 0")
	    @Digits(integer = 10, fraction = 2, message = "Maximum 10 digits before and 2 after decimal point")
	    BigDecimal condition
		) {
}
