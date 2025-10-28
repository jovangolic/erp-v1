package com.jovan.erp_v1.statistics.inventory_items;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityByProductStatDTO {

	private Long count;
	private BigDecimal quantity;
	private Long productId;
	private String productName;
	
	public QuantityByProductStatDTO(Long count,BigDecimal quantity, Long productId, String productName) {
		this.count = count;
		this.quantity = quantity;
		this.productId = productId;
		this.productName = productName;
	}
}
