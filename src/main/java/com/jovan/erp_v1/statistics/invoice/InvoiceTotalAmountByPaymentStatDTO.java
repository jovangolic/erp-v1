package com.jovan.erp_v1.statistics.invoice;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceTotalAmountByPaymentStatDTO {

	private Long count;
	private String invoiceNumber;
	private BigDecimal totalAmount;
	private Long paymentId;
	
	public InvoiceTotalAmountByPaymentStatDTO(Long count,String invoiceNumber,BigDecimal totalAmount,Long paymentId) {
		this.count = count;
		this.invoiceNumber = invoiceNumber;
		this.totalAmount = totalAmount;
		this.paymentId = paymentId;
	}
}
