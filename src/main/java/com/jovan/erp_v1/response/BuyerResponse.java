package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.Buyer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyerResponse {

	private Long id;
	private String companyName;
	private String pib;
	private String address;
	private String contactPerson;
	private String email;
	private String phoneNumber;
	
	public BuyerResponse(Buyer buyer) {
		this.id = buyer.getId();
		this.companyName = buyer.getCompanyName();
		this.pib = buyer.getPib();
		this.address = buyer.getAddress();
		this.contactPerson = buyer.getContactPerson();
		this.email = buyer.getEmail();
		this.phoneNumber = buyer.getPhoneNumber();
	}
}
