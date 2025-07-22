package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.request.CountWorkCenterCapacityRequest;

public record CountWorkCenterCapacityResponse(BigDecimal capacity, Long count) {

	public CountWorkCenterCapacityResponse(CountWorkCenterCapacityRequest dto) {
		this(dto.capacity(), dto.count());
	}
}
