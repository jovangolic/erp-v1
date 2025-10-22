package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAcceptedByQualityCheckStatDTO {

	private Long count;
	private Integer quantityAccepted;
	private Long qualityCheckId;
	
	public QuantityAcceptedByQualityCheckStatDTO(Long count,Integer quantityAccepted,Long qualityCheckId) {
		this.count = count;
		this.quantityAccepted = quantityAccepted;
		this.qualityCheckId = qualityCheckId;
	}
}
