package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAcceptedByBatchStatDTO {

	private Long count;
	private Integer quantityAccepted;
	private Long batchId;
	private String batchCode;
	
	public QuantityAcceptedByBatchStatDTO(Long count, Integer quantityAccepted,Long batchId,String batchCode) {
		this.count = count;
		this.quantityAccepted = quantityAccepted;
		this.batchId = batchId;
		this.batchCode = batchCode;
	}
}
