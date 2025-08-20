package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.TestMeasurement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestMeasurementResponse {

	private Long id;
	private InspectionResponse inspectionResponse;
	private QualityStandardResponse qualityStandardResponse;
	private BigDecimal measuredValue;
	private Boolean withinSpec;
	
	public TestMeasurementResponse(TestMeasurement tm) {
		this.id = tm.getId();
		this.inspectionResponse = tm.getInspection() != null ? new InspectionResponse(tm.getInspection()) : null;
		this.qualityStandardResponse = tm.getStandard() != null ? new QualityStandardResponse(tm.getStandard()) : null;
		this.measuredValue = tm.getMeasuredValue();
		this.withinSpec = tm.getWithinSpec();
	}
}
