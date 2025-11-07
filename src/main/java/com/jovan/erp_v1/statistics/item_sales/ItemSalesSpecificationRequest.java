package com.jovan.erp_v1.statistics.item_sales;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ItemSalesStatStrategy;

public interface ItemSalesSpecificationRequest {

	LocalDate fromDate();
	LocalDate toDate();
	ItemSalesStatStrategy strategy();
}
