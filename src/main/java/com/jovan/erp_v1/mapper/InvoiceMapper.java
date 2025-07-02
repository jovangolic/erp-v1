package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.PaymentRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.InvoiceRequest;
import com.jovan.erp_v1.response.InvoiceResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class InvoiceMapper extends AbstractMapper<InvoiceRequest> {
	
	private final BuyerRepository buyerRepository;
	private final SalesRepository salesRepository;
	private final PaymentRepository paymentRepository;
	private final SalesOrderRepository salesOrderRepository;
	private final UserRepository userRepository;

	public Invoice toEntity(InvoiceRequest request) {
        Objects.requireNonNull(request, "InvoiceRequest must not be null");
        validateIdForCreate(request, InvoiceRequest::id);
        return buildInvoiceFromRequest(new Invoice(), request);
    }
	
	public Invoice toUpdateEntity(Invoice invoice,InvoiceRequest request) {
		Objects.requireNonNull(request, "InvoiceRequest must not be null");
		Objects.requireNonNull(invoice, "Invoice must not be null");
		validateIdForUpdate(request, InvoiceRequest::id);
		return buildInvoiceFromRequest(invoice, request);
	}

    public InvoiceResponse toResponse(Invoice invoice) {
    	Objects.requireNonNull(invoice,"Invoice must not be null");
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
    
    private Invoice buildInvoiceFromRequest(Invoice invoice, InvoiceRequest request) {
    	invoice.setInvoiceNumber(request.invoiceNumber());
    	invoice.setIssueDate(request.issueDate());
    	invoice.setDueDate(request.dueDate());
    	invoice.setStatus(request.status());
    	invoice.setTotalAmount(request.totalAmount());
    	invoice.setNote(request.note());
    	invoice.setBuyer(fetchBuyer(request.buyerId()));
    	invoice.setSalesOrder(fetchSalesOrder(request.salesOrderId()));
    	invoice.setPayment(fetchPayment(request.paymentId()));
    	invoice.setRelatedSales(fetchSales(request.salesId()));
    	invoice.setCreatedBy(fetchCreatedBy(request.createdById()));
    	return invoice;
    }

    public List<InvoiceResponse> toResponseList(List<Invoice> invoices) {
    	if(invoices == null || invoices.isEmpty()){
    		return Collections.emptyList();
    	}
        return invoices.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    private Buyer fetchBuyer(Long buyerId) {
    	if(buyerId == null) {
    		throw new BuyerNotFoundException("Buyer ID must not be null");
    	}
    	return buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found " + buyerId));
    }
    
    private Sales fetchSales(Long salesId) {
    	if(salesId == null) {
    		throw new SalesNotFoundException("Sales ID must not be null");
    	}
    	return salesRepository.findById(salesId).orElseThrow(() -> new SalesNotFoundException("Sales not found "+ salesId));
    }
    
    private Payment fetchPayment(Long paymentId) {
    	if(paymentId == null) {
    		throw new PaymentNotFoundException("Payment ID must not be null");
    	}
    	return  paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException("Payment not found " +paymentId));
    }
    
    private SalesOrder fetchSalesOrder(Long salesOrderId) {
    	if(salesOrderId == null) {
    		throw new SalesOrderNotFoundException("SalesOrder ID must not be null");
    	}
    	return salesOrderRepository.findById(salesOrderId).orElseThrow(() -> new SalesOrderNotFoundException("Sales-order not found " +salesOrderId));
    }
    
    private User fetchCreatedBy(Long createdById) {
    	if(createdById == null) {
    		throw new UserNotFoundException("CreatedBy ID must not be null");
    	}
    	return userRepository.findById(createdById).orElseThrow(() -> new UserNotFoundException("CreatedBy "+createdById+ " not found"));
    }
}
