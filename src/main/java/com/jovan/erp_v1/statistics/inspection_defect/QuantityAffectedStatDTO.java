package com.jovan.erp_v1.statistics.inspection_defect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAffectedStatDTO {

	private Long count;
	private Integer quantityAffected;
	
	public QuantityAffectedStatDTO(Long count, Integer quantityAffected) {
		this.count = count;
		this.quantityAffected = quantityAffected;
	}
}
