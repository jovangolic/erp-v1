package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesUnitPriceBySalesOrderStatDTO {

	private Long count;
	private String orderNumber;
	private BigDecimal unitPrice;
	private Long salesOrderId;
	
	public ItemSalesUnitPriceBySalesOrderStatDTO(Long count,String orderNumber,BigDecimal unitPrice,Long salesOrderId) {
		this.count = count;
		this.orderNumber = orderNumber;
		this.unitPrice = unitPrice;
		this.salesOrderId = salesOrderId;
	}
}
