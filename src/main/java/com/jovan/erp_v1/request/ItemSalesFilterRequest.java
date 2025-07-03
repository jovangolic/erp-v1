package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

public record ItemSalesFilterRequest(
		Long goodsId,
	    String goodsName,
	    UnitMeasure unitMeasure,
	    SupplierType supplierType,
	    StorageType storageType,
	    GoodsType goodsType,
	    Long buyerId,
	    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	    LocalDateTime createdAtStart,
	    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	    LocalDateTime createdAtEnd,
	    BigDecimal minTotalPrice,
	    BigDecimal maxTotalPrice,
	    BigDecimal minUnitPrice,
	    BigDecimal maxUnitPrice,
	    OrderStatus orderStatus
		) {
}
