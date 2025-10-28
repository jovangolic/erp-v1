package com.jovan.erp_v1.statistics.inventory_items;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemConditionByProductStatDTO {

	private Long count;
	private BigDecimal itemCondition;
	private Long productId;
	private String productName;
	
	public ItemConditionByProductStatDTO(Long count,BigDecimal itemCondition,Long productId,String productName) {
		this.count = count;
		this.itemCondition = itemCondition;
		this.productId = productId;
		this.productName = productName;
	}
}
