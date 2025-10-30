package com.jovan.erp_v1.statistics.invoice;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceTotalAmountBySalesOrderStatDTO {

	private Long count;
	private String invoiceNumber;
	private BigDecimal totalAmount;
	private Long salesOrderId;
	
	public InvoiceTotalAmountBySalesOrderStatDTO(Long count, String invoiceNumber,BigDecimal totalAmount,Long salesOrderId) {
		this.count = count;
		this.invoiceNumber = invoiceNumber;
		this.totalAmount = totalAmount;
		this.salesOrderId = salesOrderId;
	}
}
