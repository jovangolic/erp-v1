package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesQuantityByProcurementStatDTO {

	private Long count;
	private BigDecimal quantity;
	private Long procurementId;
	
	public ItemSalesQuantityByProcurementStatDTO(Long count,BigDecimal quantity,Long procurementId) {
		this.count = count;
		this.quantity = quantity;
		this.procurementId = procurementId;
	}
}
