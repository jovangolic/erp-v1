package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.BillOfMaterialsStatus;

import jakarta.validation.constraints.NotNull;

public record BillOfMaterialsSearchRequest(
		@NotNull Long id,
		@NotNull Long idfrom,
		@NotNull Long idTo,
		@NotNull Long productId,
		@NotNull Long productIdFrom,
		@NotNull Long productIdTo,
		
		
		@NotNull Long componentId,
		@NotNull Long componentIdFrom,
		@NotNull Long componentIdTo,
		@NotNull BigDecimal quantity,
		@NotNull BigDecimal quantityMin,
		@NotNull BigDecimal quantityMax,
		@NotNull Boolean confirmed,
		@NotNull BillOfMaterialsStatus status
		) {

}
