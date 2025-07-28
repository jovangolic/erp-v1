package com.jovan.erp_v1.response;

import java.math.BigDecimal;

public record StorageItemSummaryResponse(
		Long storageId,
	    BigDecimal totalItemQuantity
		) {
}
