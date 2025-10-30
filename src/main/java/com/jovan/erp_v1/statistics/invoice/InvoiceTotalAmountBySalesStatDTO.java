package com.jovan.erp_v1.statistics.invoice;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceTotalAmountBySalesStatDTO {
	
	private Long count;
	private String invoiceNumber;
	private BigDecimal totalAmount;
	private Long salesId;
	
	public InvoiceTotalAmountBySalesStatDTO(Long count,String invoiceNumber,BigDecimal totalAmount,Long salesId) {
		this.count = count;
		this.invoiceNumber = invoiceNumber;
		this.totalAmount = totalAmount;
		this.salesId = salesId;
	}
}
