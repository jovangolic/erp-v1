package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.InvoiceStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InvoiceSaveAsRequest(
		@NotNull Long sourceId,
		@NotBlank String invoiceNumber,
		@NotNull LocalDateTime dueDate,
		@NotNull InvoiceStatus status,
		@NotNull BigDecimal totalAmount,
		@NotNull Long buyerId,
		@NotNull Long relatedSalesId,
		@NotNull Long paymentId,
		@NotBlank String note,
		@NotNull Long salesOrderId,
		@NotNull Long createdById
		) {
}
