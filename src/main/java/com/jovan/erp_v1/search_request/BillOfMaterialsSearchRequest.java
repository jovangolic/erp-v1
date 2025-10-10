package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.BillOfMaterialsStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import jakarta.validation.constraints.NotNull;

public record BillOfMaterialsSearchRequest(
		@NotNull Long id,
		@NotNull Long idfrom,
		@NotNull Long idTo,
		@NotNull Long productId,
		@NotNull Long productIdFrom,
		@NotNull Long productIdTo,
		@NotNull BigDecimal parentCurrentQuantity,
		@NotNull BigDecimal parentCurrentQuantityMin,
		@NotNull BigDecimal parentCurrentQuantityMax,
		@NotNull String productName,
		@NotNull UnitMeasure productUnitMeasure,
		@NotNull SupplierType productSupplierType,
		@NotNull StorageType productStorageType,
		@NotNull GoodsType productGoodsType,
		@NotNull Long parentStorageId,
		@NotNull Long parentStorageIdFrom,
		@NotNull Long parentStorageIdTo,
		@NotNull Long parentSupplyId,
		@NotNull Long parentSupplyIdFrom,
		@NotNull Long parentSupplyIdTo,
		@NotNull Long parentShelfId,
		@NotNull Long parentShelfIdFrom,
		@NotNull Long parentShelfIdTo,
		
		@NotNull Long componentId,
		@NotNull Long componentIdFrom,
		@NotNull Long componentIdTo,
		@NotNull BigDecimal componentCurrentQuantity,
		@NotNull BigDecimal componentCurrentQuantityMin,
		@NotNull BigDecimal componentCurrentQuantityMax,
		@NotNull String componentName,
		@NotNull UnitMeasure componentUnitMeasure,
		@NotNull SupplierType componentSupplierType,
		@NotNull StorageType componentStorageType,
		@NotNull GoodsType componentGoodsType,
		@NotNull Long componentStorageId,
		@NotNull Long componentStorageIdFrom,
		@NotNull Long componentStorageIdTo,
		@NotNull Long componentSupplyId,
		@NotNull Long componentSupplyIdFrom,
		@NotNull Long componentSupplyIdTo,
		@NotNull Long componentShelfId,
		@NotNull Long componentShelfIdFrom,
		@NotNull Long componentShelfIdTo,
		@NotNull BigDecimal quantity,
		@NotNull BigDecimal quantityMin,
		@NotNull BigDecimal quantityMax,
		@NotNull Boolean confirmed,
		@NotNull BillOfMaterialsStatus status
		) {

}
