package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
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
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
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
		Buyer buyer = fetchBuyer(request.buyerId());
		invoice.setBuyer(buyer);
		User user = fetchCreatedBy(request.createdById());
		invoice.setCreatedBy(user);
		Sales sales = fetchSales(request.salesId());
		invoice.setRelatedSales(sales);
		Payment payment = fetchPayment(request.paymentId());
		invoice.setPayment(payment);
		SalesOrder salesOrder = fetchSalesOrder(request.salesOrderId());
		if (invoice.getDueDate().isBefore(invoice.getIssueDate())) {
			throw new IllegalArgumentException("Due date cannot be before issue date");
		}
		// PROVERA da salesOrder vec nema fakturu
		if (salesOrder.getInvoice() != null) {
			throw new IllegalStateException("Sales order already has an invoice");
		}
		invoice.setSalesOrder(salesOrder);
		// Vezanje fakture i na drugoj strani
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
		Buyer buyer = fetchBuyer(request.buyerId());
		invoice.setBuyer(buyer);
		User user = fetchCreatedBy(request.createdById());
		invoice.setCreatedBy(user);
		Sales sales = fetchSales(request.salesId());
		invoice.setRelatedSales(sales);
		Payment payment = fetchPayment(request.paymentId());
		invoice.setPayment(payment);
		SalesOrder salesOrder = fetchSalesOrder(request.salesOrderId());
		DateValidator.validateRange(request.issueDate(), request.dueDate());
		validateInvoiceStatus(request.status());
		validateBigDecimal(request.totalAmount());
		validateString(request.note(),"Note");
		invoice.setIssueDate(request.issueDate());
		invoice.setDueDate(request.dueDate());
		invoice.setStatus(request.status());
		invoice.setTotalAmount(request.totalAmount());
		invoice.setNote(request.note());
		// Ako se menja SalesOrder, proveri da li novi već ima neku drugu fakturu
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
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + id));
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
	public List<InvoiceResponse> findByStatus(InvoiceStatus status) {
		validateInvoiceStatus(status);
		return invoiceRepository.findByStatus(status).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByBuyerIdAndStatus( Long buyerId,  InvoiceStatus status) {
		
		return invoiceRepository.findByBuyerIdAndStatus(buyerId, status).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByTotalAmount(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		return invoiceRepository.findAll().stream()
				.filter(i -> i.getTotalAmount().compareTo(totalAmount) == 0)
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByBuyerId(Long buyerId) {
		Buyer buyer = fetchBuyer(buyerId);
		return invoiceRepository.findByBuyerId(buyerId).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByRelatedSales_Id(Long salesId) {
		return invoiceRepository.findAll().stream()
				.filter(i -> i.getRelatedSales() != null && i.getRelatedSales().getId().equals(salesId))
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByPayment_Id(Long paymentId) {
		return invoiceRepository.findAll().stream()
				.filter(i -> i.getPayment() != null && i.getPayment().getId().equals(paymentId))
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByIssueDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		return invoiceRepository.findByIssueDateBetween(start, end).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByDueDateBefore(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date and time must not be null");
		return invoiceRepository.findByDueDateBefore(date).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByInvoiceNumberContainingIgnoreCase(String fragment) {
		validateString(fragment, "Search by fragment");
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
	public List<InvoiceResponse> findInvoicesByBuyerSortedByIssueDate(Long buyerId) {
		return invoiceRepository.findInvoicesByBuyerSortedByIssueDate(buyerId).stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	//dodate metode:
	@Override
	public InvoiceResponse findByInvoiceNumber(String invocieNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByPaymentStatus(PaymentStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByBuyerIdOrderByIssueDateDesc(Long buyerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByBuyerCompanyNameContainingIgnoreCase(String companyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByBuyerPib(String pib) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByBuyerEmailContainingIgnoreCase(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByBuyerPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByNoteContainingIgnoreCase(String note) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByRelatedSales_TotalPrice(BigDecimal totalPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByRelatedSales_TotalPriceGreaterThan(BigDecimal totalPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByRelatedSales_TotalPriceLessThan(BigDecimal totalPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByPayment_Amount(BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByPayment_PaymentDate(LocalDateTime paymentDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByPayment_PaymentDateBetween(LocalDateTime paymentDateStart,
			LocalDateTime paymentDateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByPayment_Method(PaymentMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByPayment_ReferenceNumberContainingIgnoreCase(String referenceNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_Id(Long salesOrderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsBySalesOrder_orderNumber(String orderNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_OrderDate(LocalDateTime orderDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_OrderDateBetween(LocalDateTime orderDateStart,
			LocalDateTime orderDateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_TotalAmount(BigDecimal totalAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_TotalAmountGreaterThan(BigDecimal totalAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_TotalAmountLessThan(BigDecimal totalAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_Status(OrderStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_NoteContainingIgnoreCase(String note) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByCreatedBy_Id(Long createdById) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByCreatedBy_EmailContainingIgnoreCase(String createdByEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoiceResponse> findByCreatedBy_FirstNameContainingIgnoreCaseAndCreatedBy_LastNameContainingIgnoreCase(
			String createdByFirstName, String createdByLastName) {
		// TODO Auto-generated method stub
		return null;
	}

	private String generateInvoiceNumber() {
		String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		long count = invoiceRepository.count() + 1;
		return String.format("INV-%s-%04d", datePart, count);
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
    
    private void validateInvoiceStatus(InvoiceStatus status) {
    	if(status == null) {
    		throw new IllegalArgumentException("InvoiceStatus status must not be null");
    	}
    }
    
    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new IllegalArgumentException("Number must be strictly positive and not null");
    	}
    }
    
    private void validateString(String str, String fieldName) {
    	if (str == null || str.trim().isEmpty()) {
    		throw new IllegalArgumentException(fieldName + " must not be null or empty");
    	}
    }

}
