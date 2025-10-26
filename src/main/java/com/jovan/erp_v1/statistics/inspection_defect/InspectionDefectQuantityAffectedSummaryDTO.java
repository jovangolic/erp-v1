package com.jovan.erp_v1.statistics.inspection_defect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InspectionDefectQuantityAffectedSummaryDTO {

	private Long totalCount;
	private Integer quantityAffected;
	
	public InspectionDefectQuantityAffectedSummaryDTO(Long totalCount,Integer quantityAffected) {
		this.totalCount = totalCount;
		this.quantityAffected = quantityAffected;
	}
}
