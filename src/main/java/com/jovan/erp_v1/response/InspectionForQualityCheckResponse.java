package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.model.QualityCheck;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionForQualityCheckResponse {

	private Long id;
	private LocalDateTime locDate;
	private UserResponse userResponse;
	private ReferenceType referenceType;
	private Long referenceId;
	private QualityCheckType checkType;
	private QualityCheckStatus status;
	private String notes;
	
	public InspectionForQualityCheckResponse(QualityCheck qc) {
		this.id = qc.getId();
		this.locDate = qc.getLocDate();
		this.userResponse = qc.getInspector() != null ? new UserResponse(qc.getInspector()) : null;
		this.referenceType = qc.getReferenceType();
		this.referenceId = qc.getReferenceId();
		this.checkType = qc.getCheckType();
		this.status = qc.getStatus();
		this.notes = qc.getNotes();
	}
}
