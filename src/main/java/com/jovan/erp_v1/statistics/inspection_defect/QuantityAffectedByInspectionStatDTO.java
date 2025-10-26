package com.jovan.erp_v1.statistics.inspection_defect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAffectedByInspectionStatDTO {

	private Long count;
	private Integer quantityAffected;
	private Long inspectionId;
	
	public QuantityAffectedByInspectionStatDTO(Long count,Integer quantityAffected,Long inspectionId) {
		this.count = count;
		this.quantityAffected = quantityAffected;
		this.inspectionId = inspectionId;
	}
}
