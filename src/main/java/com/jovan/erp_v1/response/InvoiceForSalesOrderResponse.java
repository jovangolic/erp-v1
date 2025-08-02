package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.model.Invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceForSalesOrderResponse {

	private Long id;
    private String invoiceNumber;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private InvoiceStatus status;
    private BigDecimal totalAmount;
    private SalesResponse salesResponse;
    private PaymentInvoiceResponse paymentInvoiceResponse;
    private String note;
    private UserResponse userResponse;
	
	public InvoiceForSalesOrderResponse(Invoice invoice) {
		this.id = invoice.getId();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.issueDate = invoice.getIssueDate();
        this.dueDate = invoice.getDueDate();
        this.status = invoice.getStatus();
        this.totalAmount = invoice.getTotalAmount();
        this.salesResponse = invoice.getRelatedSales() != null ? new SalesResponse(invoice.getRelatedSales()) : null;
        this.note = invoice.getNote();
        this.paymentInvoiceResponse = invoice.getPayment() != null ? new PaymentInvoiceResponse(invoice.getPayment()) : null;
        this.userResponse = invoice.getCreatedBy() != null ? new UserResponse(invoice.getCreatedBy()) : null;
	}
}
