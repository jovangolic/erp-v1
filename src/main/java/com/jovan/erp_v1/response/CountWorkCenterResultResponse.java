package com.jovan.erp_v1.response;

import com.jovan.erp_v1.request.CountWorkCenterResultRequest;

public record CountWorkCenterResultResponse(String label, Long count) {

	public CountWorkCenterResultResponse(CountWorkCenterResultRequest dto) {
		this(dto.label(), dto.count());
	}
}
