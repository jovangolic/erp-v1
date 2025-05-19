package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.PaymentRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.request.InvoiceRequest;
import com.jovan.erp_v1.response.InvoiceResponse;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class InvoiceMapper {
	
	private final BuyerRepository buyerRepository;
	private final SalesRepository salesRepository;
	private final PaymentRepository paymentRepository;
	private final SalesOrderRepository salesOrderRepository;

	public Invoice toEntity(InvoiceRequest request) {
        Invoice invoice = new Invoice();
        Buyer buyer = buyerRepository.findById(request.buyerId()).orElseThrow(() -> new BuyerNotFoundException("Buyer not found"));
        Sales sales = salesRepository.findById(request.salesId()).orElseThrow(() -> new SalesNotFoundException("Sales not found"));
        Payment payment = paymentRepository.findById(request.paymentId()).orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
        SalesOrder salesOrder = salesOrderRepository.findById(request.salesOrderId()).orElseThrow(() -> new SalesOrderNotFoundException("Sales-order not found"));
        invoice.setId(request.id());
        invoice.setInvoiceNumber(request.invoiceNumber());
        invoice.setIssueDate(request.issueDate());
        invoice.setDueDate(request.dueDate());
        invoice.setStatus(request.status());
        invoice.setTotalAmount(request.totalAmount());
        invoice.setNote(request.note());
        invoice.setBuyer(buyer);
        invoice.setRelatedSales(sales);
        invoice.setPayment(payment);
        invoice.setSalesOrder(salesOrder);
        // buyer, sales, payment, salesOrder i user se ne postavljaju ovde,
        // jer se oni postavljaju u servisnoj logici (npr. invoiceService.createInvoice())
        return invoice;
    }

    public InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setIssueDate(invoice.getIssueDate());
        response.setDueDate(invoice.getDueDate());
        response.setStatus(invoice.getStatus());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setNote(invoice.getNote());

        if (invoice.getBuyer() != null) {
            response.setBuyerName(invoice.getBuyer().getCompanyName());
        }

        if (invoice.getCreatedBy() != null) {
            response.setCreatedByName(invoice.getCreatedBy().getEmail());
        }
        if(invoice.getPayment() != null) {
        	response.setPaymentAmount(invoice.getPayment().getAmount());
        }
        if(invoice.getRelatedSales() != null) {
        	response.setSalesDescription(invoice.getRelatedSales().getSalesDescription());
        }

        return response;
    }

    public List<InvoiceResponse> toResponseList(List<Invoice> invoices) {
        return invoices.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
