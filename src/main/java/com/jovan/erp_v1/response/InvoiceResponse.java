package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.InvoiceTypeStatus;
import com.jovan.erp_v1.model.Invoice;
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
    private BuyerResponse buyerResponse;
    private SalesResponse salesResponse;
    private PaymentInvoiceResponse paymentInvoiceResponse;
    private String note;
    private SalesOrderResponse salesOrderResponse;
    private UserResponse userResponse;
    private InvoiceTypeStatus typeStatus;
    private Boolean confirmed;


    public InvoiceResponse(Invoice invoice) {
    	this.id = invoice.getId();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.issueDate = invoice.getIssueDate();
        this.dueDate = invoice.getDueDate();
        this.status = invoice.getStatus();
        this.totalAmount = invoice.getTotalAmount();
        this.buyerResponse = invoice.getBuyer() != null ? new BuyerResponse(invoice.getBuyer()) : null;
        this.salesResponse = invoice.getRelatedSales() != null ? new SalesResponse(invoice.getRelatedSales()) : null;
        this.note = invoice.getNote();
        this.paymentInvoiceResponse = invoice.getPayment() != null ? new PaymentInvoiceResponse(invoice.getPayment()) : null;
        this.salesOrderResponse = invoice.getSalesOrder() != null ? new SalesOrderResponse(invoice.getSalesOrder()) : null;
        this.userResponse = invoice.getCreatedBy() != null ? new UserResponse(invoice.getCreatedBy()) : null;
        this.typeStatus = invoice.getTypeStatus();
        this.confirmed = invoice.getConfirmed();
    }
}
