package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.request.PaymentRequest;
import com.jovan.erp_v1.response.PaymentResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class PaymentMapper extends AbstractMapper<PaymentRequest> {

	public Payment toEntity(PaymentRequest request,Buyer buyer, Sales relatedSales) {
		Objects.requireNonNull(request,"PaymentRequest must not be null");
		Objects.requireNonNull(buyer,"Buyer must not be null");
		Objects.requireNonNull(relatedSales,"Sales must not be null");
		validateIdForCreate(request, PaymentRequest::id);
		Payment p = new Payment();
		p.setId(request.id());
		p.setAmount(request.amount());
		p.setStatus(request.status());
		p.setReferenceNumber(request.referenceNumber());
		p.setBuyer(buyer);
		p.setRelatedSales(relatedSales);
		return p;
	}
	
	public Payment toUpdateEntity(Payment payment, PaymentRequest request, Buyer buyer, Sales relatedSales) {
		Objects.requireNonNull(request,"PaymentRequest must not be null");
		Objects.requireNonNull(payment,"Payment must not be null");
		validateIdForUpdate(request, PaymentRequest::id);
		return buildPaymentFromRequest(payment, request,buyer,relatedSales);
	}
	
	private Payment buildPaymentFromRequest(Payment payment, PaymentRequest request, Buyer buyer, Sales relatedSales) {
		payment.setAmount(request.amount());
		payment.setMethod(request.method());
		payment.setStatus(request.status());
		payment.setReferenceNumber(request.referenceNumber());
		payment.setBuyer(buyer);
		payment.setRelatedSales(relatedSales);
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
}
