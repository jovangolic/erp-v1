package com.jovan.erp_v1.response;

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

	public VendorResponse(Vendor vendor) {
		this.id = vendor.getId();
		this.name = vendor.getName();
		this.email = vendor.getEmail();
		this.phoneNumber = vendor.getPhoneNumber();
		this.address = vendor.getAddress();
	}
}
