package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesUnitPriceByProcurementStatDTO {

	private Long count;
	private BigDecimal unitPrice;
	private Long procurementId;
	
	public ItemSalesUnitPriceByProcurementStatDTO(Long count,BigDecimal unitPrice,Long procurementId) {
		this.count = count;
		this.unitPrice = unitPrice;
		this.procurementId = procurementId;
	}
}
