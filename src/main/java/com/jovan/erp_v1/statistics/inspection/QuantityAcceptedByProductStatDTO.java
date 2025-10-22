package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAcceptedByProductStatDTO {

	private Long count;
	private Integer quantityAccepted;
	private Long productId;
	private String productName;
	
	public QuantityAcceptedByProductStatDTO(Long count,Integer quantityAccepted,Long productId, String productName) {
		this.count = count;
		this.quantityAccepted = quantityAccepted;
		this.productId = productId;
		this.productName = productName;
	}
}
