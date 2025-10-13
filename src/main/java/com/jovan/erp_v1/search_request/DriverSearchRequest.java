package com.jovan.erp_v1.search_request;

import com.jovan.erp_v1.enumeration.DriverStatus;

public record DriverSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		String firstName,
		String lastName,
		String phone,
		DriverStatus status,
		Boolean confirmed
		) {
}
