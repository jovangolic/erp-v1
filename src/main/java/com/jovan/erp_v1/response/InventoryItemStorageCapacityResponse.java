package com.jovan.erp_v1.response;

import java.math.BigDecimal;

public record InventoryItemStorageCapacityResponse(
		Long inventoryItemId,
	    Long productId,
	    Long storageId,
	    BigDecimal usedCapacity
		) {
}
