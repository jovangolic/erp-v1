package com.jovan.erp_v1.response;

import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.Vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponse {

	private Long id;
	private String name;
	private String email;
	private String phoneNumber;
	private String address;
	private List<MaterialTransactionForVendorResponse> materialTransactionForVendorResponses;

	public VendorResponse(Vendor vendor) {
		this.id = vendor.getId();
		this.name = vendor.getName();
		this.email = vendor.getEmail();
		this.phoneNumber = vendor.getPhoneNumber();
		this.address = vendor.getAddress();
		this.materialTransactionForVendorResponses = vendor.getMaterialTransactions().stream()
				.map(MaterialTransactionForVendorResponse::new)
				.collect(Collectors.toList());
	}
}
