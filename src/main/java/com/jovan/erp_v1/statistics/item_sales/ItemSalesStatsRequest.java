package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ItemSalesStatStrategy;

public record ItemSalesStatsRequest(
	    Long salesOrderId,
	    Long procurementId,
	    Long goodsId,
	    Long salesId,
	    LocalDate fromDate,
	    LocalDate toDate,
	    BigDecimal minQuantity,
	    BigDecimal maxQuantity,
	    BigDecimal minUnitPrice,
	    BigDecimal maxUnitPrice
	) implements ItemSalesSpecificationRequest {

	@Override
	public ItemSalesStatStrategy strategy() {
		return ItemSalesStatStrategy.AUTO;
	}}
