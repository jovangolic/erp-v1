package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.TestMeasurement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestMeasurementForQualityStandardResponse {

	private Long id;
	private InspectionResponse inspectionResponse;
	private BigDecimal measuredValue;
	private Boolean withinSpec;
	
	public TestMeasurementForQualityStandardResponse(TestMeasurement tm) {
		this.id = tm.getId();
		this.inspectionResponse = tm.getInspection() != null ? new InspectionResponse(tm.getInspection()) : null;
		this.measuredValue = tm.getMeasuredValue();
		this.withinSpec = tm.getWithinSpec();
	}
}
