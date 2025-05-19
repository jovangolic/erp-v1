package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.model.Payment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentResponse {

	private Long id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentMethod method;
    private PaymentStatus status;
    private String referenceNumber;
    private BuyerResponse buyer;          // Ili samo buyerId
    private SalesResponse relatedSales;   // Ili samo salesId

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.amount = payment.getAmount();
        this.paymentDate = payment.getPaymentDate();
        this.method = payment.getMethod();
        this.status = payment.getStatus();
        this.referenceNumber = payment.getReferenceNumber();
        this.buyer = new BuyerResponse(payment.getBuyer());
        this.relatedSales = new SalesResponse(payment.getRelatedSales());
    }
	
}
