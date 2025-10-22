package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityRejectedByInspectorStatDTO {

	private Long count;
	private Integer quantityRejected;
	private Long inspectorId;
	private String firstName;
	private String lastName;
	
	public QuantityRejectedByInspectorStatDTO(Long count,Integer quantityRejected,Long inspectorId, String firstName,String lastName) {
		this.count = count;
		this.quantityRejected = quantityRejected;
		this.inspectorId = inspectorId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
