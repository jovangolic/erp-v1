package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityInspectedByQualityCheckStatDTO {

	private Long count;
	private Integer quantityInspected;
	private Long qualityCheckId;
	
	public QuantityInspectedByQualityCheckStatDTO(Long count, Integer quantityInspected, Long qualityCheckId) {
		this.count = count;
		this.quantityInspected = quantityInspected;
		this.qualityCheckId = qualityCheckId;
	}
}
