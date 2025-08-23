package com.jovan.erp_v1.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.model.Inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionForDefectResponse {

	private Long id;
	private String code;
	private InspectionType type;
	private LocalDateTime inspectionDate;
	private BasicBatchResponse basicBatchResponse;
	private ProductResponse productResponse;
	private UserResponse userResponse;
	private Integer quantityInspected;
	private Integer quantityAccepted;
	private Integer quantityRejected;
	private String notes;
	private InspectionResult result;
	private InspectionForQualityCheckResponse inspectionForQualityCheckResponse;
	private List<InspectionForTestMeasurementResponse>  inspectionForTestMeasurementResponses;
	
	public InspectionForDefectResponse(Inspection insp) {
		this.id = insp.getId();
		this.code = insp.getCode();
		this.type = insp.getType();
		this.inspectionDate = insp.getInspectionDate();
		this.basicBatchResponse = insp.getBatch() != null ? new BasicBatchResponse(insp.getBatch()) : null;
		this.productResponse = insp.getProduct() != null ? new ProductResponse(insp.getProduct()) : null;
		this.userResponse = insp.getInspector() != null ? new UserResponse(insp.getInspector()) : null;
		this.quantityInspected = insp.getQuantityInspected();
		this.quantityAccepted = insp.getQuantityAccepted();
		this.quantityRejected = insp.getQuantityRejected();
		this.notes = insp.getNotes();
		this.result = insp.getResult();
		this.inspectionForQualityCheckResponse = insp.getQualityCheck() != null ? new InspectionForQualityCheckResponse(insp.getQualityCheck()) : null;
		this.inspectionForTestMeasurementResponses = insp.getMeasurements().stream()
				.map(InspectionForTestMeasurementResponse::new)
				.collect(Collectors.toList());
	}
}
