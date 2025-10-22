package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityRejectedByBatchStatDTO {

	private Long count;
	private Integer quantityRejected;
	private Long batchId;
	private String batchCode;
	
	public QuantityRejectedByBatchStatDTO(Long count,Integer quantityRejected,Long batchId,String batchCode) {
		this.count = count;
		this.quantityRejected = quantityRejected;
		this.batchId = batchId;
		this.batchCode = batchCode;
	}
}
