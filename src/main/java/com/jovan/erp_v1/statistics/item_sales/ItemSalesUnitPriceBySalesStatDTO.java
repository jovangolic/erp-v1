package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesUnitPriceBySalesStatDTO {

	private Long count;
	private BigDecimal unitPrice;
	private Long salesId;
	
	public ItemSalesUnitPriceBySalesStatDTO(Long count,BigDecimal unitPrice,Long salesId) {
		this.count = count;
		this.unitPrice = unitPrice;
		this.salesId = salesId;
	}
}
