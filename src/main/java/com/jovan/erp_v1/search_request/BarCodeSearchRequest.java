package com.jovan.erp_v1.search_request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.BarCodeStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;


public record BarCodeSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		String code,
		LocalDateTime scannedAt,
		LocalDateTime scannedAtBefore,
		LocalDateTime scannedAtAfter,
		LocalDateTime scannedAtStart,
		LocalDateTime scannedAtEnd,
		Long userId,
		Long userIdFrom,
		Long userIdTo,
		String firstName,
		String lastName,
		Long goodsId,
		Long goodsIdFrom,
		Long goodsIdTo,
		String goodsName,
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
		BarCodeStatus status,
		Boolean confirmed
		) {
}
