package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.Inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionResponse {

	private Long id;
	
	
	public InspectionResponse(Inspection insp) {
		this.id = insp.getId();
	}
}
