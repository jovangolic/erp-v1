package com.jovan.erp_v1.statistics.inspection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityAcceptedByInspectorStatDTO {

	private Long count;
	private Integer quantityAccepted;
	private Long inspectorId;
	private String firstName;
	private String lastName;
	
	public QuantityAcceptedByInspectorStatDTO(Long count,Integer quantityAccepted,Long inspectorId, String firstName,String lastName) {
		this.count = count;
		this.quantityAccepted = quantityAccepted;
		this.inspectorId = inspectorId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
