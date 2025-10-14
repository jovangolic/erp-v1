package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

public record GoodsSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		String name,
		UnitMeasure unitMeasure,
		SupplierType supplierType,
		StorageType storageType,
		GoodsType goodsType,
		Long storageId,
		Long storageIdFrom,
		Long storageIdTo,
		String storageName,
		String storageLoc,
		BigDecimal storageCapacity,
		BigDecimal storageCapacityMin,
		BigDecimal storageCapacityMax,
		Long supplyId,
		Long supplyIdFrom,
		Long supplyIdTo,
		Long shelfId,
		Long shelfIdFrom,
		Long shelfIdTo		
		) {
}
