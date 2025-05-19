package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.InvoiceNotFoundException;
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.mapper.InvoiceMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.InvoiceRepository;
import com.jovan.erp_v1.repository.PaymentRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.InvoiceRequest;
import com.jovan.erp_v1.response.InvoiceResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceService implements IInvoiceService {

	private final InvoiceRepository invoiceRepository;
    private final BuyerRepository buyerRepository;
    private final SalesRepository salesRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceMapper invoiceMapper;
    private final SalesOrderRepository salesOrderRepository;
    private final UserRepository userRepository;
    
    @Transactional
	@Override
	public InvoiceResponse createInvoice(InvoiceRequest request) {
		Invoice invoice = invoiceMapper.toEntity(request);
	    Buyer buyer = buyerRepository.findById(request.buyerId())
	        .orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + request.buyerId()));
	    invoice.setBuyer(buyer);
	    User user = userRepository.findById(request.createdById()).orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.createdById()));
	    invoice.setCreatedBy(user);
	    Sales sales = salesRepository.findById(request.salesId())
	        .orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + request.salesId()));
	    invoice.setRelatedSales(sales);
	    Payment payment = paymentRepository.findById(request.paymentId())
	        .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + request.paymentId()));
	    invoice.setPayment(payment);
	    SalesOrder salesOrder = salesOrderRepository.findById(request.salesOrderId())
	        .orElseThrow(() -> new SalesOrderNotFoundException("SalesOrder not found with id: " + request.salesOrderId()));
	    if (invoice.getDueDate().isBefore(invoice.getIssueDate())) {
	        throw new IllegalArgumentException("Due date cannot be before issue date");
	    }
	    //PROVERA da salesOrder vec nema fakturu
	    if (salesOrder.getInvoice() != null) {
	        throw new IllegalStateException("Sales order already has an invoice");
	    }
	    invoice.setSalesOrder(salesOrder);
	    //Vezanje fakture i na drugoj strani
	    salesOrder.setInvoice(invoice);
	    invoice.setIssueDate(LocalDateTime.now());
	    invoice.setInvoiceNumber(generateInvoiceNumber());
	    Invoice saved = invoiceRepository.save(invoice);
	    return invoiceMapper.toResponse(saved);
	}
    
    @Transactional
	@Override
	public InvoiceResponse updateInvoice(Long id, InvoiceRequest request) {
		Invoice invoice = invoiceRepository.findById(id)
		        .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + id));
		invoice.setIssueDate(request.issueDate());
	    invoice.setDueDate(request.dueDate());
	    invoice.setStatus(request.status());
	    invoice.setTotalAmount(request.totalAmount());
	    invoice.setNote(request.note());

	    Buyer buyer = buyerRepository.findById(request.buyerId())
	        .orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + request.buyerId()));
	    invoice.setBuyer(buyer);

	    Sales sales = salesRepository.findById(request.salesId())
	        .orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + request.salesId()));
	    invoice.setRelatedSales(sales);

	    Payment payment = paymentRepository.findById(request.paymentId())
	        .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + request.paymentId()));
	    invoice.setPayment(payment);

	    SalesOrder salesOrder = salesOrderRepository.findById(request.salesOrderId())
	        .orElseThrow(() -> new SalesOrderNotFoundException("SalesOrder not found with id: " + request.salesOrderId()));
	    invoice.setSalesOrder(salesOrder);
	 //Ako se menja SalesOrder, proveri da li novi već ima neku drugu fakturu
	    if (salesOrder.getInvoice() != null && !salesOrder.getInvoice().getId().equals(invoice.getId())) {
	        throw new IllegalStateException("The new sales order is already linked to another invoice");
	    }
	    invoice.setSalesOrder(salesOrder);
	    salesOrder.setInvoice(invoice);
	    invoice.setInvoiceNumber(generateInvoiceNumber());
	    Invoice saved = invoiceRepository.save(invoice);
		return invoiceMapper.toResponse(saved);
	}
	@Override
	public InvoiceResponse getInvoiceById(Long id) {
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: "+id));
		return invoiceMapper.toResponse(invoice);
	}
	@Override
	public List<InvoiceResponse> getAllInvoices() {
		return invoiceRepository.findAll().stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Transactional
	@Override
	public void deleteInvoice(Long id) {
		invoiceRepository.deleteById(id);
	}
	@Override
	public List<InvoiceResponse> getByInvoiceStatus(InvoiceStatus status) {
		return invoiceRepository.findByStatus(status).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<InvoiceResponse> getByBuyerAndStatus(Buyer buyer, InvoiceStatus status) {
		return invoiceRepository.findByBuyerAndStatus(buyer, status).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<InvoiceResponse> getByTotalAmount(BigDecimal totalAmount) {
	        return invoiceRepository.findAll().stream()
	                .filter(i -> i.getTotalAmount().compareTo(totalAmount) == 0)
	                .map(invoiceMapper::toResponse)
	                .collect(Collectors.toList());
	    }
	@Override
	public List<InvoiceResponse> getByBuyerId(Long buyerId) {
		Buyer buyer = buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + buyerId));
		return invoiceRepository.findByBuyer(buyer).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
    public List<InvoiceResponse> getBySalesId(Long salesId) {
        return invoiceRepository.findAll().stream()
                .filter(i -> i.getRelatedSales() != null && i.getRelatedSales().getId().equals(salesId))
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());
    }
	@Override
    public List<InvoiceResponse> getByPaymentId(Long paymentId) {
        return invoiceRepository.findAll().stream()
                .filter(i -> i.getPayment() != null && i.getPayment().getId().equals(paymentId))
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());
    }
	@Override
	public List<InvoiceResponse> getByIssueDateBetween(LocalDateTime start, LocalDateTime end) {
		return invoiceRepository.findByIssueDateBetween(start, end).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<InvoiceResponse> getByDueDateBefore(LocalDateTime date) {
		return invoiceRepository.findByDueDateBefore(date).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<InvoiceResponse> searchByInvoiceNumberFragment(String fragment) {
	    List<Invoice> invoices = invoiceRepository.findByInvoiceNumberContainingIgnoreCase(fragment);
	    return invoices.stream()
	                   .map(invoiceMapper::toResponse)
	                   .collect(Collectors.toList());
	}
	@Override
	public boolean existsByInvoiceNumber(String invoiceNumber) {
		return invoiceRepository.existsByInvoiceNumber(invoiceNumber);
	}
	@Override
    public List<InvoiceResponse> getInvoicesByBuyerSortedByIssueDate(Long buyerId) {
        return invoiceRepository.findInvoicesByBuyerSortedByIssueDate(buyerId).stream()
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());
    }
	
	private String generateInvoiceNumber() {
        long count = invoiceRepository.count() + 1;
        return String.format("INV-%04d", count);
    }
	
	
}

