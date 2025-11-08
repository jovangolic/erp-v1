package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ItemSalesStatStrategy;

public interface ItemSalesSpecificationRequest {

	LocalDate fromDate();
	LocalDate toDate();
	ItemSalesStatStrategy strategy();
	
	default Long procurementId() { return null; }
    default Long salesOrderId() { return null; }
    default Long goodsId() { return null; }
    default Long salesId() { return null; }

    default BigDecimal minQuantity() { return null; }
    default BigDecimal maxQuantity() { return null; }
    default BigDecimal minUnitPrice() { return null; }
    default BigDecimal maxUnitPrice() { return null; }
}
