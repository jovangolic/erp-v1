package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.request.BuyerRequest;
import com.jovan.erp_v1.response.BuyerResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BuyerMapper extends AbstractMapper<BuyerRequest> {

	public Buyer toEntity(BuyerRequest request) {
		Objects.requireNonNull(request, "BuyerRequest must not be null");
		validateIdForCreate(request, BuyerRequest::id);
		Buyer buyer = new Buyer();
        buyer.setCompanyName(request.companyName());
        buyer.setAddress(request.address());
        buyer.setContactPerson(request.contactPerson());
        buyer.setEmail(request.email());
        buyer.setPhoneNumber(request.phoneNumber());
        buyer.setPib(request.pib());
        buyer.setSalesOrders(new ArrayList<>());
        return buyer;
	}
	
	public BuyerResponse toResponse(Buyer buyer) {
        Objects.requireNonNull(buyer, "Buyer must not be null");
        return new BuyerResponse(buyer);
    }

    public List<BuyerResponse> toResponseList(List<Buyer> buyers) {
        List<BuyerResponse> responses = new ArrayList<>();
        for (Buyer buyer : buyers) {
            responses.add(toResponse(buyer));
        }
        return responses;
    }
    
    public Buyer updateBuyer(BuyerRequest request, Buyer buyer) {
    	Objects.requireNonNull(request, "BuyerRequest must not be null");
    	Objects.requireNonNull(buyer, "Buyer must not be null");
    	validateIdForUpdate(request, BuyerRequest::id);
        buyer.setCompanyName(request.companyName());
        buyer.setAddress(request.address());
        buyer.setContactPerson(request.contactPerson());
        buyer.setEmail(request.email());
        buyer.setPhoneNumber(request.phoneNumber());
        // PIB se ne menja 
        return buyer;
    }
}
