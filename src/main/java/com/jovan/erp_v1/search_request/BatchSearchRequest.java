package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.BatchStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

public record BatchSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		String code,
		Long productId,
		Long productIdFrom,
		Long productIdTo,
		BigDecimal currentQuantity,
		BigDecimal currentQuantityMin,
		BigDecimal currentQuantityMax,
		String productName,
		UnitMeasure unitMeasure,
		SupplierType supplierType,
		StorageType storageType,
		GoodsType goodsType,
		Long storageId,
		Long storageIdFrom,
		Long storageIdTo,
		Long supplyId,
		Long supplyIdFrom,
		Long supplyIdTo,
		Long shelfId,
		Long shelfIdFrom,
		Long shelfIdTo,
		Integer quantityProduced,
		Integer quantityProducedMin,
		Integer quantityProducedMax,
		LocalDate productionDate,
		LocalDate productionDateBefore,
		LocalDate productionDateAfter,
		LocalDate productionDateStart,
		LocalDate productionDateEnd,
		LocalDate expiryDate,
		LocalDate expiryDateBefore,
		LocalDate expiryDateAfter,
		LocalDate expiryDateStart,
		LocalDate expiryDateEnd,
		BatchStatus status,
		Boolean confirmed
		) {
}
