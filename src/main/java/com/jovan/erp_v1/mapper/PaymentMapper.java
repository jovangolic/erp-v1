package com.jovan.erp_v1.mapper;

import java.util.List;
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

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

	private final BuyerRepository buyerRepository;
	private final SalesRepository salesRepository;
	
	
	public Payment toEntity(PaymentRequest request) {
		Payment payment = new Payment();
		payment.setAmount(request.amount());
		payment.setPaymentDate(request.paymentDate());
		payment.setMethod(request.method());
		payment.setStatus(request.status());
		payment.setReferenceNumber(request.referenceNumber());
		Buyer buyer = buyerRepository.findById(request.buyerId()).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: "+request.buyerId()));
		Sales sales = salesRepository.findById(request.relatedSalesId()).orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + request.relatedSalesId()));
		payment.setBuyer(buyer);
		payment.setRelatedSales(sales);
		return payment;
	}
	
	public PaymentResponse toResponse(Payment payment) {
		return new PaymentResponse(payment);
	}
	
	public List<PaymentResponse> toResponseList(List<Payment> payments){
		return payments.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
}
