package com.jovan.erp_v1.statistics.invoice;

import java.time.LocalDate;

public record InvoiceStatRequest(
		Long buyerId,
		LocalDate fromDate,
		LocalDate toDate
		) {
}
