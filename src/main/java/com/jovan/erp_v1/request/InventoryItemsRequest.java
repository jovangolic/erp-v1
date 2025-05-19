package com.jovan.erp_v1.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryItemsRequest(
		
		Long id,
		@NotNull(message = "Inventory ID is required")
	    Long inventoryId,

	    @NotNull(message = "Product ID is required")
	    Long productId,

	    @NotNull(message = "Quantity is required")
	    @Min(value = 0, message = "Quantity must be at least 0")
	    Double quantity,

	    @NotNull(message = "Condition is required")
	    @Min(value = 0, message = "Condition must be at least 0")
	    Integer condition
		) {

}
