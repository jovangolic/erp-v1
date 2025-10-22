package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityRejectedByProductStatDTO {

	private Long count;
	private Integer quantityRejected;
	private Long productId;
	private String productName;
	
	public QuantityRejectedByProductStatDTO(Long count,Integer quantityRejected, Long productId, String productName) {
		this.count = count;
		this.quantityRejected = quantityRejected;
		this.productId = productId;
		this.productName = productName;
	}
}
