package com.jovan.erp_v1.response;

import java.math.BigDecimal;

public record StorageCapacityResponse(
		Long storageId,
	    BigDecimal usedCapacity
		) {
}
