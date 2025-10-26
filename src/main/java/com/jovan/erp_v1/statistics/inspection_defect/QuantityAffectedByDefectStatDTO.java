package com.jovan.erp_v1.statistics.inspection_defect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAffectedByDefectStatDTO {

	private Long count;
	private Integer quantityAffected;
	private Long defectId;
	
	public QuantityAffectedByDefectStatDTO(Long count,Integer quantityAffected,Long defectId) {
		this.count = count;
		this.quantityAffected = quantityAffected;
		this.defectId = defectId;
	}
}
