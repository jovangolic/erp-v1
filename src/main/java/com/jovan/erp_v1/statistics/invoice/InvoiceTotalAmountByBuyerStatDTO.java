package com.jovan.erp_v1.statistics.invoice;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceTotalAmountByBuyerStatDTO {

	private Long count;
	private String invoiceNumber;
	private BigDecimal totalAmount;
	private Long buyerId;
	
	public InvoiceTotalAmountByBuyerStatDTO(Long count,String invoiceNumber,BigDecimal totalAmount,Long buyerId) {
		this.count = count;
		this.invoiceNumber = invoiceNumber;
		this.totalAmount = totalAmount;
		this.buyerId = buyerId;
	}
}
