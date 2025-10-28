package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InventoryItemsStatus;
import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

public record InventoryItemsSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		Long inventoryId,
		Long inventoryIdFrom,
		Long inventoryIdTo,
		Long storageEmployeeId,
		Long storageEmployeeIdFrom,
		Long storageEmployeeIdTo,
		Long storageForemanId,
		Long storageForemanIdFrom,
		Long storageForemanIdTo,
		LocalDate inventoryDate,
		LocalDate inventoryDateBefore,
		LocalDate inventoryDateAfter,
		Boolean inventoryAligned,
		InventoryStatus inventoryStatus,
		Long productId,
		Long productIdFrom,
		Long productIdTo,
		BigDecimal productCurrentQuantity,
		BigDecimal productCurrentQuantityMin,
		BigDecimal productCurrentQuantityMax,
		String productName,
		UnitMeasure unitMeasure,
		SupplierType supplierType,
		StorageType storageType,
		GoodsType goodsType,
		Long productStorageId,
		Long productStorageIdFrom,
		Long productStorageIdTo,
		Long productSupplyId,
		Long productSupplyIdFrom,
		Long productSupplyIdTo,
		Long productShelfId,
		Long productShelfIdFrom,
		Long productShelfIdTo,
		BigDecimal quantity,
		BigDecimal quantityMin,
		BigDecimal quantityMax,
		BigDecimal itemCondition,
		BigDecimal itemConditionMin,
		BigDecimal itemConditionMax,
		
		Boolean confirmed,
		InventoryItemsStatus status
		) {
}
