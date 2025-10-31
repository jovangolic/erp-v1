package com.jovan.erp_v1.statistics.invoice;

import java.time.LocalDate;

public record InvoiceStatBySalesOrderRequest(
		Long salesOrderId,
		LocalDate fromDate,
		LocalDate toDate,
		InvoiceStatStrategy strategy
		) implements InvoiceSpecificationRequest {

}
