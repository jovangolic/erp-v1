package com.jovan.erp_v1.search_request;

import com.jovan.erp_v1.enumeration.BuyerStatus;

public record BuyerSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		String companyName,
		String pib,
		String address,
		String contactPerson,
		String email,
		String phoneNumber,
		BuyerStatus status,
		Boolean confirmed
		) {
}
