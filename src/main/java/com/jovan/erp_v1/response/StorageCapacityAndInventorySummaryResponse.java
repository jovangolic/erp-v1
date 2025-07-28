package com.jovan.erp_v1.response;

import java.math.BigDecimal;

public record StorageCapacityAndInventorySummaryResponse(
		Long storageId,
	    BigDecimal capacity,
	    BigDecimal usedCapacity,
	    BigDecimal totalItemQuantity
		) {
	/*public StorageCapacityAndInventorySummaryResponse(Long id, BigDecimal capacity, BigDecimal used, BigDecimal totalQty) {
        this(id, capacity, used, capacity.subtract(used), totalQty);
    }*/
}
