package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityInspectedByInspectorStatDTO {

	private Long count;
	private Integer quantityInspected;
	private Long inspectorId;
	private String firstName;
	private String lastName;
	
	public QuantityInspectedByInspectorStatDTO(Long count, Integer quantityInspected,Long inspectorId, String firstName,String lastName) {
		this.count = count;
		this.quantityInspected = quantityInspected;
		this.inspectorId = inspectorId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
