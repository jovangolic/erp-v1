package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.TestMeasurement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionForTestMeasurementResponse {

	private Long id;
	private BigDecimal measuredValue;
	private Boolean withinSpec;
	
	public InspectionForTestMeasurementResponse(TestMeasurement tm) {
		this.id = tm.getId();
		this.measuredValue = tm.getMeasuredValue();
		this.withinSpec = tm.getWithinSpec();
	}
}
