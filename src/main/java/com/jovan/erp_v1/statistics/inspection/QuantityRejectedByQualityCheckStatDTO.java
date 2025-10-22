package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityRejectedByQualityCheckStatDTO {

	private Long count;
	private Integer quantityRejected;
	private Long qualityCheckId;
	
	public QuantityRejectedByQualityCheckStatDTO(Long count,Integer quantityRejected,Long qualityCheckId) {
		this.count = count;
		this.quantityRejected = quantityRejected;
		this.qualityCheckId = qualityCheckId;
	}
}
