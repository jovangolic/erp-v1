package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityInspectedByBatchStatDTO {

	private Long count;
	private Integer quantityInspected;
	private Long batchId;
	private String batchCode;
	
	public QuantityInspectedByBatchStatDTO(Long count, Integer quantityInspected, Long batchId, String batchCode) {
		this.count = count;
		this.quantityInspected = quantityInspected;
		this.batchId = batchId;
		this.batchCode = batchCode;
	}
}
