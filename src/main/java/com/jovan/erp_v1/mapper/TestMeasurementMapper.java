package com.jovan.erp_v1.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.model.QualityStandard;
import com.jovan.erp_v1.model.TestMeasurement;
import com.jovan.erp_v1.request.TestMeasurementRequest;
import com.jovan.erp_v1.response.TestMeasurementResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class TestMeasurementMapper extends AbstractMapper<TestMeasurementRequest> {

	public TestMeasurement toEntity(TestMeasurementRequest request,Inspection inspection,QualityStandard standard) {
		Objects.requireNonNull(request, "TestMeasurementRequest  must not be null");
		Objects.requireNonNull(inspection, "Inspection must not be null");
		Objects.requireNonNull(standard, "QualityStandard must not be null");
		validateIdForCreate(request, TestMeasurementRequest::id);
		TestMeasurement tm = new TestMeasurement();
		tm.setId(request.id());
		tm.setInspection(inspection);
		tm.setStandard(standard);
		tm.setMeasuredValue(request.measuredValue());
		tm.setWithinSpec(calculateWithinSpec(request.measuredValue(), standard));
		return tm;
	}
	
	public TestMeasurement toEntityUpdate(TestMeasurement tm,TestMeasurementRequest request,Inspection inspection,QualityStandard standard) {
		Objects.requireNonNull(tm, "TestMeasurement  must not be null");
		Objects.requireNonNull(request, "TestMeasurementRequest  must not be null");
		Objects.requireNonNull(inspection, "Inspection must not be null");
		Objects.requireNonNull(standard, "QualityStandard must not be null");
		validateIdForUpdate(request, TestMeasurementRequest::id);
		tm.setInspection(inspection);
		tm.setStandard(standard);
		tm.setMeasuredValue(request.measuredValue());
		tm.setWithinSpec(calculateWithinSpec(request.measuredValue(), standard));
		return tm;
	}
	
	public TestMeasurementResponse toResponse(TestMeasurement tm) {
		Objects.requireNonNull(tm, "TestMeasurement  must not be null");
		return new TestMeasurementResponse(tm);
	}
	
	public List<TestMeasurementResponse> toResponseList(List<TestMeasurement> tm){
		if(tm == null || tm.isEmpty()) {
			return Collections.emptyList();
		}
		return tm.stream().map(this::toResponse).collect(Collectors.toList());
	}
	
	private boolean calculateWithinSpec(BigDecimal measuredValue, QualityStandard standard) {
        return measuredValue.compareTo(standard.getMinValue()) >= 0 &&
               measuredValue.compareTo(standard.getMaxValue()) <= 0;
    }
}
