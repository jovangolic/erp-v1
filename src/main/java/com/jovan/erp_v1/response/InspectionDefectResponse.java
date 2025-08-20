package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.InspectionDefect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionDefectResponse {

	private Long id;
	
	
	public InspectionDefectResponse(InspectionDefect inspectionDefect) {
		this.id = inspectionDefect.getId();
	}
}
