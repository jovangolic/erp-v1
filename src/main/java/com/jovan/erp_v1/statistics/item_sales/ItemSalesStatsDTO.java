package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSalesStatsDTO {

	private Long goodsId;
	private Long salesId;
	private Long procurementId;
	private Long salesOrderId;
	private BigDecimal totalQuantity;
    private BigDecimal totalAmount;
}
