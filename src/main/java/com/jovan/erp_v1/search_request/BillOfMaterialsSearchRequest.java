package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.BillOfMaterialsStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

public record BillOfMaterialsSearchRequest(
		Long id,
		Long idfrom,
		Long idTo,
		Long productId,
		Long productIdFrom,
		Long productIdTo,
		BigDecimal parentCurrentQuantity,
		BigDecimal parentCurrentQuantityMin,
		BigDecimal parentCurrentQuantityMax,
		String productName,
		UnitMeasure productUnitMeasure,
		SupplierType productSupplierType,
		StorageType productStorageType,
		GoodsType productGoodsType,
		Long parentStorageId,
		Long parentStorageIdFrom,
		Long parentStorageIdTo,
		Long parentSupplyId,
		Long parentSupplyIdFrom,
		Long parentSupplyIdTo,
		Long parentShelfId,
		Long parentShelfIdFrom,
		Long parentShelfIdTo,
		
		Long componentId,
		Long componentIdFrom,
		Long componentIdTo,
		BigDecimal componentCurrentQuantity,
		BigDecimal componentCurrentQuantityMin,
		BigDecimal componentCurrentQuantityMax,
		String componentName,
		UnitMeasure componentUnitMeasure,
		SupplierType componentSupplierType,
		StorageType componentStorageType,
		GoodsType componentGoodsType,
		Long componentStorageId,
		Long componentStorageIdFrom,
		Long componentStorageIdTo,
		Long componentSupplyId,
		Long componentSupplyIdFrom,
		Long componentSupplyIdTo,
		Long componentShelfId,
		Long componentShelfIdFrom,
		Long componentShelfIdTo,
		BigDecimal quantity,
		BigDecimal quantityMin,
		BigDecimal quantityMax,
		Boolean confirmed,
		BillOfMaterialsStatus status
		) {
}
