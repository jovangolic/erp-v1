package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.request.PaymentRequest;
import com.jovan.erp_v1.response.PaymentResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentMapper extends AbstractMapper<PaymentRequest> {

	private final BuyerRepository buyerRepository;
	private final SalesRepository salesRepository;
	
	
	public Payment toEntity(PaymentRequest request) {
		Objects.requireNonNull(request,"PaymentRequest must not be null");
		validateIdForCreate(request, PaymentRequest::id);
		return buildPaymentFromRequest(new Payment(), request);
	}
	
	public Payment toUpdateEntity(Payment payment, PaymentRequest request) {
		Objects.requireNonNull(request,"PaymentRequest must not be null");
		Objects.requireNonNull(payment,"Payment must not be null");
		validateIdForUpdate(request, PaymentRequest::id);
		return buildPaymentFromRequest(payment, request);
	}
	
	private Payment buildPaymentFromRequest(Payment payment, PaymentRequest request) {
		payment.setAmount(request.amount());
		payment.setPaymentDate(request.paymentDate());
		payment.setMethod(request.method());
		payment.setStatus(request.status());
		payment.setReferenceNumber(request.referenceNumber());
		Buyer buyer = fetchBuyer(request.buyerId());
		Sales sales = fetchSales(request.relatedSalesId());
		payment.setBuyer(buyer);
		payment.setRelatedSales(sales);
		return payment;
	}
	
	public PaymentResponse toResponse(Payment payment) {
		Objects.requireNonNull(payment,"Payment must not be null");
		return new PaymentResponse(payment);
	}
	
	public List<PaymentResponse> toResponseList(List<Payment> payments){
		if(payments == null || payments.isEmpty()) {
			return Collections.emptyList();
		}
		return payments.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
	private Buyer fetchBuyer(Long buyerId) {
    	if(buyerId == null) {
    		throw new BuyerNotFoundException("Buyer ID must not be null");
    	}
    	return buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with ID: "+buyerId));
    }
	
	private Sales fetchSales(Long salesId) {
    	if(salesId == null) {
    		throw new SalesNotFoundException("Sales ID must not be null");
    	}
    	return salesRepository.findById(salesId).orElseThrow(() -> new BuyerNotFoundException("Sales not found with ID: "+salesId));
    }
	
}
