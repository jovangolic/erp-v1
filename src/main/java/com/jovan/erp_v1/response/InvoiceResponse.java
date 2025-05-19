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
public class InvoiceResponse {

	private Long id;
    private String invoiceNumber;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private InvoiceStatus status;
    private BigDecimal totalAmount;
    private String buyerName;
    private String note;
    private String createdByName;
    private BigDecimal paymentAmount;
    private String salesDescription;

    public InvoiceResponse(Invoice invoice) {
    	this.id = invoice.getId();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.issueDate = invoice.getIssueDate();
        this.dueDate = invoice.getDueDate();
        this.status = invoice.getStatus();
        this.totalAmount = invoice.getTotalAmount();
        this.buyerName = invoice.getBuyer().getCompanyName();
        this.note = invoice.getNote();
        this.createdByName = invoice.getCreatedBy().getEmail();
        this.paymentAmount = invoice.getPayment().getAmount();
        this.salesDescription = invoice.getRelatedSales().getSalesDescription();
    }
}
