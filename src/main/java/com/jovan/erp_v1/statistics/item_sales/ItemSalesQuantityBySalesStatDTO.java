package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesQuantityBySalesStatDTO {

	private Long count;
	private BigDecimal quantity;
	private Long salesId;
	
	public ItemSalesQuantityBySalesStatDTO(Long count,BigDecimal quantity,Long salesId) {
		this.count = count;
		this.quantity = quantity;
		this.salesId = salesId;
	}
}
