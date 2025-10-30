package com.jovan.erp_v1.statistics.invoice;

import java.time.LocalDate;

public record InvoiceStatBySalesRequest(
		Long salesId,
		LocalDate fromDate,
		LocalDate toDate,
		InvoiceStatStrategy strategy
		) {
}
