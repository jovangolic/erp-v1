package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesQuantityBySalesOrderStatDTO {

	private Long count;
	private String orderNumber;
	private BigDecimal quantity;
	private Long salesOrderId;
	
	public ItemSalesQuantityBySalesOrderStatDTO(Long count, String orderNumber,BigDecimal quantity,Long salesOrderId) {
		this.count = count;
		this.orderNumber = orderNumber;
		this.quantity = quantity;
		this.salesOrderId = salesOrderId;
	}
}
