package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.BatchStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BatchSearchRequest(
		@NotNull Long id,
		@NotNull Long idFrom,
		@NotNull Long idTo,
		@NotBlank String code,
		@NotNull Long productId,
		@NotNull Long productIdFrom,
		@NotNull Long productIdTo,
		@NotNull BigDecimal currentQuantity,
		@NotNull BigDecimal currentQuantityMin,
		@NotNull BigDecimal currentQuantityMax,
		@NotBlank String productName,
		@NotNull UnitMeasure unitMeasure,
		@NotNull SupplierType supplierType,
		@NotNull StorageType storageType,
		@NotNull GoodsType goodsType,
		@NotNull Long storageId,
		@NotNull Long storageIdFrom,
		@NotNull Long storageIdTo,
		@NotNull Long supplyId,
		@NotNull Long supplyIdFrom,
		@NotNull Long supplyIdTo,
		@NotNull Long shelfId,
		@NotNull Long shelfIdFrom,
		@NotNull Long shelfIdTo,
		@NotNull Integer quantityProduced,
		@NotNull Integer quantityProducedMin,
		@NotNull Integer quantityProducedMax,
		
		@NotNull BatchStatus status,
		@NotNull Boolean confirmed
		) {
}
