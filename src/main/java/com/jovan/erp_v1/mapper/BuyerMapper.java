package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Buyer;

import com.jovan.erp_v1.request.BuyerRequest;
import com.jovan.erp_v1.response.BuyerResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BuyerMapper {

	
	
	public Buyer toEntity(BuyerRequest request) {
		Buyer buyer = new Buyer();
        buyer.setCompanyName(request.companyName());
        buyer.setAddress(request.address());
        buyer.setContactPerson(request.contactPerson());
        buyer.setEmail(request.email());
        buyer.setPhoneNumber(request.phoneNumber());
        
        // Automatski generišemo PIB
        buyer.setPib(Buyer.generateRandomPib());

        // salesOrders se ignoriše — inicijalno prazan
        buyer.setSalesOrders(new ArrayList<>());

        return buyer;
	}
	
	public BuyerResponse toResponse(Buyer buyer) {
        BuyerResponse response = new BuyerResponse();
        response.setId(buyer.getId());
        response.setCompanyName(buyer.getCompanyName());
        response.setAddress(buyer.getAddress());
        response.setContactPerson(buyer.getContactPerson());
        response.setEmail(buyer.getEmail());
        response.setPhoneNumber(buyer.getPhoneNumber());
        response.setPib(buyer.getPib());

        return response;
    }

    public List<BuyerResponse> toResponseList(List<Buyer> buyers) {
        List<BuyerResponse> responses = new ArrayList<>();
        for (Buyer buyer : buyers) {
            responses.add(toResponse(buyer));
        }
        return responses;
    }
    
    public void updateBuyer(BuyerRequest request, Buyer buyer) {
        buyer.setCompanyName(request.companyName());
        buyer.setAddress(request.address());
        buyer.setContactPerson(request.contactPerson());
        buyer.setEmail(request.email());
        buyer.setPhoneNumber(request.phoneNumber());
        // PIB se ne menja (osim ako želiš da ga ponovo generišeš ovde)
    }
}
