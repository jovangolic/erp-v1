package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityInspectedByProductStatDTO {

	private Long count;
	private Integer quantityInspected;
	private Long productId;
	private String productName;
	
	public QuantityInspectedByProductStatDTO(Long count, Integer quantityInspected, Long productId, String productName) {
		this.count = count;
		this.quantityInspected = quantityInspected;
		this.productId = productId;
		this.productName = productName;
	}
}
