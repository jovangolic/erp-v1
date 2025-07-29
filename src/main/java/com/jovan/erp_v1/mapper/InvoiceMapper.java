package com.jovan.erp_v1.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.InvoiceRequest;
import com.jovan.erp_v1.response.InvoiceResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class InvoiceMapper extends AbstractMapper<InvoiceRequest> {
	
	public Invoice toEntity(InvoiceRequest request, Buyer buyer,Sales sales,Payment payment,SalesOrder so,User createdAt) {
        Objects.requireNonNull(request, "InvoiceRequest must not be null");
        Objects.requireNonNull(buyer, "Buyer must not be null");
        Objects.requireNonNull(sales, "Sales must not be null");
        Objects.requireNonNull(payment, "Payment must not be null");
        Objects.requireNonNull(so, "SalesOrder must not be null");
        Objects.requireNonNull(createdAt, "User must not be null");
        validateIdForCreate(request, InvoiceRequest::id);
        Invoice invoice = new Invoice();
        invoice.setId(request.id());
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setDueDate(request.dueDate());
        invoice.setStatus(request.status());
        invoice.setTotalAmount(request.totalAmount());
        invoice.setBuyer(buyer);
        invoice.setRelatedSales(sales);
        invoice.setPayment(payment);
        invoice.setNote(request.note());
        invoice.setSalesOrder(so);
        invoice.setCreatedBy(createdAt);
        return invoice;
    }
	
	public Invoice toUpdateEntity(Invoice invoice,InvoiceRequest request, Buyer buyer,Sales sales,Payment payment,SalesOrder so,User createdAt) {
		Objects.requireNonNull(request, "InvoiceRequest must not be null");
		Objects.requireNonNull(invoice, "Invoice must not be null");
		Objects.requireNonNull(buyer, "Buyer must not be null");
        Objects.requireNonNull(sales, "Sales must not be null");
        Objects.requireNonNull(payment, "Payment must not be null");
        Objects.requireNonNull(so, "SalesOrder must not be null");
        Objects.requireNonNull(createdAt, "User must not be null");
		validateIdForUpdate(request, InvoiceRequest::id);
		return buildInvoiceFromRequest(invoice, request,buyer,sales,payment,so,createdAt);
	}

    private Invoice buildInvoiceFromRequest(Invoice invoice, InvoiceRequest request, Buyer buyer,Sales sales,Payment payment,SalesOrder so,User createdAt) {
    	invoice.setIssueDate(request.issueDate());
    	invoice.setDueDate(request.dueDate());
    	invoice.setStatus(request.status());
    	invoice.setTotalAmount(request.totalAmount());
    	invoice.setNote(request.note());
    	invoice.setBuyer(buyer);
    	invoice.setSalesOrder(so);
    	invoice.setPayment(payment);
    	invoice.setRelatedSales(sales);
    	invoice.setCreatedBy(createdAt);
    	return invoice;
    }
    
    public InvoiceResponse toResponse(Invoice invoice) {
    	Objects.requireNonNull(invoice,"Invoice must not be null");
        return new InvoiceResponse(invoice);
    }

    public List<InvoiceResponse> toResponseList(List<Invoice> invoices) {
    	if(invoices == null || invoices.isEmpty()){
    		return Collections.emptyList();
    	}
        return invoices.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
}
