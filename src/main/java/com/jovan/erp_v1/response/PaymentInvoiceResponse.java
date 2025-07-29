package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.model.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInvoiceResponse {

	private Long id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentMethod method;
    private PaymentStatus status;
    private String referenceNumber;
    
    public PaymentInvoiceResponse(Payment p) {
    	this.id = p.getId();
    	this.amount = p.getAmount();
    	this.paymentDate = p.getPaymentDate();
    	this.method = p.getMethod();
    	this.status = p.getStatus();
    	this.referenceNumber = p.getReferenceNumber();
    }
}
