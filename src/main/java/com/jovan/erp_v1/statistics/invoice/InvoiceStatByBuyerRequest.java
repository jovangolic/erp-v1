package com.jovan.erp_v1.statistics.invoice;

import java.time.LocalDate;

public record InvoiceStatByBuyerRequest(
		Long buyerId,
		LocalDate fromDate,
		LocalDate toDate,
		InvoiceStatStrategy strategy
		) {
}
