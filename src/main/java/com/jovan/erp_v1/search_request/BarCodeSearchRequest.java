package com.jovan.erp_v1.search_request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.BarCodeStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BarCodeSearchRequest(
		@NotNull Long id,
		@NotNull Long idFrom,
		@NotNull Long idTo,
		@NotBlank String code,
		@NotNull LocalDateTime scannedAt,
		@NotNull LocalDateTime scannedAtBefore,
		@NotNull LocalDateTime scannedAtAfter,
		@NotNull LocalDateTime scannedAtStart,
		@NotNull LocalDateTime scannedAtEnd,
		@NotNull Long userId,
		@NotNull Long userIdFrom,
		@NotNull Long userIdTo,
		@NotBlank String firstName,
		@NotBlank String lastName,
		@NotNull Long goodsId,
		@NotNull Long goodsIdFrom,
		@NotNull Long goodsIdTo,
		@NotBlank String goodsName,
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
		@NotNull BarCodeStatus status,
		@NotNull Boolean confirmed
		) {
}
