package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.model.QualityStandard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityStandardResponse {

	private Long id;
	private ProductResponse productResponse;
	private String description;
	private BigDecimal minValue;
	private BigDecimal maxValue;
	private Unit unit;
	private List<TestMeasurementForQualityStandardResponse> testResponse;
	
	public QualityStandardResponse(QualityStandard qs) {
		this.id = qs.getId();
		this.productResponse = qs.getProduct() != null ? new ProductResponse(qs.getProduct()) : null;
		this.description = qs.getDescription();
		this.minValue = qs.getMinValue();
		this.maxValue = qs.getMaxValue();
		this.unit = qs.getUnit();
		this.testResponse = qs.getMeasurements().stream()
				.map(TestMeasurementForQualityStandardResponse::new)
				.collect(Collectors.toList());
	}
}
