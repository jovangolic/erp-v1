package com.jovan.erp_v1.statistics.invoice;

import java.time.LocalDate;

public record InvoiceStatByPaymentRequest(
		Long paymentId,
		LocalDate fromDate,
		LocalDate toDate,
		InvoiceStatStrategy strategy
		) implements InvoiceSpecificationRequest {
}
