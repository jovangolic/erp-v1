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
	private Integer quantityAffected;
	private InspectionForDefectResponse inspectionForDefectResponse;
	private DefectResponse defectResponse;
	
	public InspectionDefectResponse(InspectionDefect inspectionDefect) {
		this.id = inspectionDefect.getId();
		this.quantityAffected = inspectionDefect.getQuantityAffected();
		this.inspectionForDefectResponse = inspectionDefect.getInspection() != null ? new InspectionForDefectResponse(inspectionDefect.getInspection()) : null;
		this.defectResponse = inspectionDefect.getDefect() != null ? new DefectResponse(inspectionDefect.getDefect()) : null;
	}
}
