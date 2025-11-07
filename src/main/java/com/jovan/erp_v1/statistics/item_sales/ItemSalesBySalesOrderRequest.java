package com.jovan.erp_v1.statistics.item_sales;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ItemSalesStatStrategy;

public record ItemSalesBySalesOrderRequest(
		Long salesOrderId,
		LocalDate fromDate,
		LocalDate toDate,
		ItemSalesStatStrategy strategy
		) implements ItemSalesSpecificationRequest {
}

	


