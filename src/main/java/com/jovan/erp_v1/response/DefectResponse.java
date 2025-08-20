package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.model.Defect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectResponse {

	private Long id;
	private String code;
	private String name;
	private String description;
	private SeverityLevel severity;
	
	public DefectResponse(Defect d) {
		this.id = d.getId();
		this.code = d.getCode();
		this.name = d.getName();
		this.description = d.getDescription();
		this.severity = d.getSeverity();
		
	}
}
