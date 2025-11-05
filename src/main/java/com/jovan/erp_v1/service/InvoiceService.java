package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.InvoiceTypeStatus;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.InvoiceNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
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
import com.jovan.erp_v1.repository.specification.InvoiceSpecification;
import com.jovan.erp_v1.request.InvoiceRequest;
import com.jovan.erp_v1.response.InvoiceResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.InvoiceSaveAsRequest;
import com.jovan.erp_v1.search_request.InvoiceSearchRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceSpecificationRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatByBuyerRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatByPaymentRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatBySalesOrderRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatBySalesRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatStrategy;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountByBuyerStatDTO;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountByPaymentStatDTO;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountBySalesOrderStatDTO;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountBySalesStatDTO;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService  implements IInvoiceService {

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
		DateValidator.validateNotNull(request.issueDate(), "Issue date");
		DateValidator.validateNotNull(request.dueDate(), "Due date");
		validateInvoiceStatus(request.status());
		validateBigDecimal(request.totalAmount());
		validateString(request.note(), "Note");
		Buyer buyer = fetchBuyer(request.buyerId());
		User user = fetchCreatedBy(request.createdById());
		Sales sales = fetchSales(request.salesId());
		Payment payment = fetchPayment(request.paymentId());
		SalesOrder salesOrder = fetchSalesOrder(request.salesOrderId());
		// PROVERA da salesOrder vec nema fakturu
		if (salesOrder.getInvoice() != null) {
			throw new IllegalStateException("Sales order already has an invoice");
		}
		String generatedInvoiceNumber;
		do {
			generatedInvoiceNumber = generateInvoiceNumber();
		} while (invoiceRepository.existsByInvoiceNumber(generatedInvoiceNumber));
		Invoice invoice = invoiceMapper.toEntity(request,buyer,sales,payment,salesOrder,user);
		if (invoice.getDueDate().isBefore(invoice.getIssueDate())) {
			throw new IllegalArgumentException("Due date cannot be before issue date");
		}
		// Vezanje fakture i na drugoj strani
		salesOrder.setInvoice(invoice);
		try {
			Invoice saved = invoiceRepository.save(invoice);
			return invoiceMapper.toResponse(saved);
		}
		catch(DataIntegrityViolationException e) {
			throw new IllegalStateException("InvoiceNumber already exists due to concurrent write");
		}
	}

	@Transactional
	@Override
	public InvoiceResponse updateInvoice(Long id, InvoiceRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + id));
		DateValidator.validateNotNull(request.issueDate(), "Issue date");
		DateValidator.validateNotNull(request.dueDate(), "Due date");
		validateInvoiceStatus(request.status());
		validateBigDecimal(request.totalAmount());
		validateString(request.note(), "Note");
		Buyer buyer = invoice.getBuyer();
		if(request.buyerId() != null && (buyer.getId() == null || !request.buyerId().equals(buyer.getId()))) {
			buyer = fetchBuyer(request.buyerId());
		}
		User user = invoice.getCreatedBy();
		if(request.createdById() != null && (user.getId() == null || !request.createdById().equals(user.getId()))) {
			user = fetchCreatedBy(request.createdById());
		}
		Sales sales = invoice.getRelatedSales();
		if(request.salesId() != null && (sales.getId() == null || !request.salesId().equals(sales.getId()))) {
			sales = fetchSales(request.salesId());
		}
		Payment payment = invoice.getPayment();
		if(request.paymentId() != null && (payment.getId() == null || !request.paymentId().equals(payment.getId()))) {
			payment = fetchPayment(request.paymentId());
		}
		SalesOrder salesOrder = invoice.getSalesOrder();
		if(request.salesOrderId() != null && (salesOrder.getId() == null || !request.salesOrderId().equals(salesOrder.getId()))) {
			salesOrder = fetchSalesOrder(request.salesOrderId());
		}
		// Ako se menja SalesOrder, provera da li novi vec ima neku drugu fakturu
		if (salesOrder.getInvoice() != null && !salesOrder.getInvoice().getId().equals(invoice.getId())) {
			throw new IllegalStateException("The new sales order is already linked to another invoice");
		}
		invoice.setSalesOrder(salesOrder);
		salesOrder.setInvoice(invoice);
		invoiceMapper.toUpdateEntity(invoice, request, buyer, sales, payment, salesOrder, user);
		if (invoice.getDueDate().isBefore(invoice.getIssueDate())) {
			throw new IllegalArgumentException("Due date cannot be before issue date");
		}
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
		List<Invoice> items = invoiceRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Invoice list is empty");
		}
		return items.stream()
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
		List<Invoice> items = invoiceRepository.findByStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByBuyerIdAndStatus( Long buyerId,  InvoiceStatus status) {
		fetchBuyer(buyerId);
		validateInvoiceStatus(status);
		List<Invoice> items = invoiceRepository.findByBuyerIdAndStatus(buyerId, status);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for buyer-id %d and invoice-status %s is found",
					buyerId,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
		fetchBuyer(buyerId);
		List<Invoice> items = invoiceRepository.findByBuyerId(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for buyer-id %d is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
		List<Invoice> items = invoiceRepository.findByIssueDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Invoice for issued date between %s and %s is found",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByDueDateBefore(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date and time must not be null");
		List<Invoice> items = invoiceRepository.findByDueDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Invoice for due date before %s is found",
					date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
		validateBuyerExists(buyerId);
		List<Invoice> items = invoiceRepository.findInvoicesByBuyerSortedByIssueDate(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for buyer-id %s sorted by issue date is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(invoiceMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	//dodate metode:
	@Override
	public InvoiceResponse findByInvoiceNumber(String invocieNumber) {
		validateString(invocieNumber, "Invoice number");
		Invoice invoice = invoiceRepository.findByInvoiceNumber(invocieNumber).orElseThrow(() -> new InvoiceNotFoundException("InvoiceNumber not found"+ invocieNumber));
		return new InvoiceResponse(invoice);
	}

	@Override
	public List<InvoiceResponse> findByPaymentStatus(PaymentStatus status) {
		validatePaymentStatus(status);
		List<Invoice> items = invoiceRepository.findByPaymentStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for payment status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByBuyerIdOrderByIssueDateDesc(Long buyerId) {
		validateBuyerExists(buyerId);
		List<Invoice> items = invoiceRepository.findByBuyerId(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for buyer id %d is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByBuyerCompanyNameContainingIgnoreCase(String companyName) {
		validateString(companyName, "Company name");
		List<Invoice> items = invoiceRepository.findByBuyerCompanyNameContainingIgnoreCase(companyName);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for buyer company-name %s is found", companyName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByBuyerPib(String pib) {
		if (pib == null || pib.trim().isEmpty()) {
			throw new IllegalArgumentException("PIB must not be null or empty");
		}
		if(!invoiceRepository.existsByBuyer_Pib(pib)) {
			throw new InvoiceNotFoundException("No invoices found for buyer with PIB: " + pib);
		}
		return invoiceRepository.findByBuyerPib(pib).stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByBuyerEmailContainingIgnoreCase(String email) {
		validateString(email, "Email");
		List<Invoice> items = invoiceRepository.findByBuyerEmailContainingIgnoreCase(email);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for buyer email %s is found", email);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByBuyerPhoneNumber(String phoneNumber) {
		validateString(phoneNumber, "Phone number");
		List<Invoice> items = invoiceRepository.findByBuyerPhoneNumber(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for buyer phone-number %s is found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByNoteContainingIgnoreCase(String note) {
		validateString(note, "Note");
		List<Invoice> items = invoiceRepository.findByNoteContainingIgnoreCase(note);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for note %s is found", note);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByRelatedSales_TotalPrice(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		List<Invoice> items = invoiceRepository.findByRelatedSales_TotalPrice(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for related-sales with total price %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByRelatedSales_TotalPriceGreaterThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		List<Invoice> items = invoiceRepository.findByRelatedSales_TotalPriceGreaterThan(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for related-sales with total price greater than %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByRelatedSales_TotalPriceLessThan(BigDecimal totalPrice) {
		validateBigDecimalNonNegative(totalPrice);
		List<Invoice> items = invoiceRepository.findByRelatedSales_TotalPriceLessThan(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for related-sales with total price less than %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByPayment_Amount(BigDecimal amount) {
		validateBigDecimal(amount);
		List<Invoice> items = invoiceRepository.findByPayment_Amount(amount);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for payment amound %s is found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByPayment_PaymentDate(LocalDateTime paymentDate) {
		DateValidator.validateNotNull(paymentDate, "Date and time");
		List<Invoice> items = invoiceRepository.findByPayment_PaymentDate(paymentDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Invoice for payment date %s is found",
					paymentDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByPayment_PaymentDateBetween(LocalDateTime paymentDateStart,
			LocalDateTime paymentDateEnd) {
		DateValidator.validateRange(paymentDateStart, paymentDateEnd);
		List<Invoice> items = invoiceRepository.findByPayment_PaymentDateBetween(paymentDateStart, paymentDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Invoice for payment date between %s and %s is found", 
					paymentDateStart.format(formatter), paymentDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByPayment_Method(PaymentMethod method) {
		validatePaymentMehod(method);
		List<Invoice> items = invoiceRepository.findByPayment_Method(method);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for payment method %s is found", method);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByPayment_ReferenceNumberContainingIgnoreCase(String referenceNumber) {
		validateString(referenceNumber, "Reference number");
		List<Invoice> items = invoiceRepository.findByPayment_ReferenceNumberContainingIgnoreCase(referenceNumber);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for payment reference-number %s is found", referenceNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_Id(Long salesOrderId) {
		fetchSalesOrder(salesOrderId);
		List<Invoice> items = invoiceRepository.findBySalesOrder_Id(salesOrderId);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for sales-order id %d is found", salesOrderId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public boolean existsBySalesOrder_OrderNumber(String orderNumber) {
		if(orderNumber == null || orderNumber.trim().isEmpty()) {
			throw new IllegalArgumentException("OrderNumber must not be null or empty");
		}
		if(!invoiceRepository.existsByInvoiceNumber(orderNumber)) {
			throw new InvoiceNotFoundException("Mo orderNumber for SalesOrder not found "+orderNumber);
		}
		return invoiceRepository.existsBySalesOrder_OrderNumber(orderNumber);
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_OrderDate(LocalDateTime orderDate) {
		DateValidator.validateNotNull(orderDate, "Date and time");
		List<Invoice> items = invoiceRepository.findBySalesOrder_OrderDate(orderDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Invoice for sales-order date %s is found", 
					orderDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_OrderDateBetween(LocalDateTime orderDateStart,
			LocalDateTime orderDateEnd) {
		DateValidator.validateRange(orderDateStart, orderDateEnd);
		List<Invoice> items = invoiceRepository.findBySalesOrder_OrderDateBetween(orderDateStart, orderDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Invoice for sales-order date between %s and %s is found", 
					orderDateStart.format(formatter),orderDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_TotalAmount(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		List<Invoice> items = invoiceRepository.findBySalesOrder_TotalAmount(totalAmount);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for sales-order total amount %s is found", totalAmount);
			throw new NoDataFoundException(msg);
		}
		return invoiceRepository.findBySalesOrder_TotalAmount(totalAmount).stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_TotalAmountGreaterThan(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		List<Invoice> items = invoiceRepository.findBySalesOrder_TotalAmountGreaterThan(totalAmount);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for sales-order total amount greater than %s is found", totalAmount);
			throw new NoDataFoundException(msg);
		}
		return invoiceRepository.findBySalesOrder_TotalAmountGreaterThan(totalAmount).stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_TotalAmountLessThan(BigDecimal totalAmount) {
		validateBigDecimalNonNegative(totalAmount);
		List<Invoice> items = invoiceRepository.findBySalesOrder_TotalAmountLessThan(totalAmount);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for sales-order total amount less than %s is found", totalAmount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_Status(OrderStatus status) {
		validateOrderStatus(status);
		List<Invoice> items = invoiceRepository.findBySalesOrder_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice found for sales-order status %s", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findBySalesOrder_NoteContainingIgnoreCase(String note) {
		validateString(note, "Note");
		List<Invoice> items = invoiceRepository.findBySalesOrder_NoteContainingIgnoreCase(note);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice found for note %s", note);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByCreatedBy_Id(Long createdById) {
		fetchCreatedBy(createdById);
		List<Invoice> items = invoiceRepository.findByCreatedBy_Id(createdById);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice for createdby id %d, is found", createdById);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByCreatedBy_EmailContainingIgnoreCase(String createdByEmail) {
		validateString(createdByEmail, "createdByEmail");
		List<Invoice> items = invoiceRepository.findByCreatedBy_EmailContainingIgnoreCase(createdByEmail);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice createdby email %s is found", createdByEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InvoiceResponse> findByCreatedBy_FirstNameContainingIgnoreCaseAndCreatedBy_LastNameContainingIgnoreCase(
			String createdByFirstName, String createdByLastName) {
		validateDoubleString(createdByFirstName, createdByLastName);
		List<Invoice> items = invoiceRepository.findByCreatedBy_FirstNameContainingIgnoreCaseAndCreatedBy_LastNameContainingIgnoreCase(createdByFirstName, createdByLastName);
		if(items.isEmpty()) {
			String msg = String.format("No Invoice createdby first-name %s and last-name %s is found", 
					createdByFirstName,createdByLastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InvoiceResponse::new)
				.collect(Collectors.toList());
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public List<InvoiceTotalAmountByBuyerStatDTO> getInvoiceStatisticsByBuyer(InvoiceStatByBuyerRequest request) {
	    long totalInvoices = invoiceRepository.count();
	    InvoiceStatStrategy strategy = 
	        request.strategy() != null ? request.strategy() : InvoiceStatStrategy.AUTO;
	    switch (strategy) {
	        case SQL -> {
	            if (totalInvoices < 10000) {
	                return aggregateInMemory(request,
	                		inv -> inv.getBuyer().getId(),
	                		list -> new InvoiceTotalAmountByBuyerStatDTO((long)list.size(),
	                				list.get(0).getInvoiceNumber(),
	                				list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
	                				list.get(0).getRelatedSales().getId()));
	            } else {
	                return invoiceRepository.countInvoiceTotalAmountByBuyer(
	                        request.buyerId(),
	                        request.fromDate(),
	                        request.toDate()
	                );
	            }
	        }
	        case MEMORY -> {
	        	return aggregateInMemory(request,
                		inv -> inv.getBuyer().getId(),
                		list -> new InvoiceTotalAmountByBuyerStatDTO((long)list.size(),
                				list.get(0).getInvoiceNumber(),
                				list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
                				list.get(0).getRelatedSales().getId()));
	        }
	        case AUTO -> {
	            // AUTO odlucuje sam na osnovu velicine dataset-a
	        	if (totalInvoices < 10000) {
	                return aggregateInMemory(request,
	                		inv -> inv.getBuyer().getId(),
	                		list -> new InvoiceTotalAmountByBuyerStatDTO((long)list.size(),
	                				list.get(0).getInvoiceNumber(),
	                				list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
	                				list.get(0).getRelatedSales().getId()));
	            } else {
	                return invoiceRepository.countInvoiceTotalAmountByBuyer(
	                        request.buyerId(),
	                        request.fromDate(),
	                        request.toDate()
	                );
	            }
	        }
	        default -> throw new ValidationException("Unknown strategy: " + strategy);
	    }
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<InvoiceTotalAmountBySalesStatDTO> getInvoiceStatisticsBySales(InvoiceStatBySalesRequest request) {
		long totalInvoices = invoiceRepository.count();
		InvoiceStatStrategy strategy = request.strategy() != null ? request.strategy() : InvoiceStatStrategy.AUTO;
		switch (strategy) {
			case SQL -> {
				if(totalInvoices < 10000) {
					return aggregateInMemory(request,
							inv -> inv.getRelatedSales().getId(),
							list -> new InvoiceTotalAmountBySalesStatDTO((long)list.size(),
									list.get(0).getInvoiceNumber(),
									list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
									list.get(0).getRelatedSales().getId()));
				}
				else {
					return invoiceRepository.countInvoiceTotalAmountBySales(request.salesId(), request.fromDate(), request.toDate());
				}
			}
			case MEMORY -> {
				return aggregateInMemory(request,
						inv -> inv.getRelatedSales().getId(),
						list -> new InvoiceTotalAmountBySalesStatDTO((long)list.size(),
								list.get(0).getInvoiceNumber(),
								list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
								list.get(0).getRelatedSales().getId()));
			}
			case AUTO -> {
				if(totalInvoices < 10000) {
					return aggregateInMemory(request,
							inv -> inv.getRelatedSales().getId(),
							list -> new InvoiceTotalAmountBySalesStatDTO((long)list.size(),
									list.get(0).getInvoiceNumber(),
									list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
									list.get(0).getRelatedSales().getId()));
				}
				else {
					return invoiceRepository.countInvoiceTotalAmountBySales(request.salesId(), request.fromDate(), request.toDate());
				}
			}
			default -> throw new ValidationException("Unknown strategy: " + strategy);
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<InvoiceTotalAmountByPaymentStatDTO> getInvoiceStatisticsByPayment(InvoiceStatByPaymentRequest request) {
		long totalInvoices = invoiceRepository.count();
		InvoiceStatStrategy strategy = request.strategy() != null ? request.strategy() : InvoiceStatStrategy.AUTO;
		switch(strategy) {
			case SQL -> {
				if(totalInvoices < 10000) {
					return aggregateInMemory(request,
						inv -> inv.getPayment().getId(),
						list -> new InvoiceTotalAmountByPaymentStatDTO((long)list.size(),
								list.get(0).getInvoiceNumber(),
								list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
								list.get(0).getPayment().getId()));
			}
			else {
				return invoiceRepository.countInvoiceTotalAmountByPayment(request.paymentId(), request.fromDate(), request.toDate());
			}
		}
			case MEMORY -> {
				return aggregateInMemory(request,
						inv -> inv.getPayment().getId(),
						list -> new InvoiceTotalAmountByPaymentStatDTO((long)list.size(),
								list.get(0).getInvoiceNumber(),
								list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
								list.get(0).getPayment().getId()));
			}
			case AUTO -> {
				if(totalInvoices < 10000) {
					return aggregateInMemory(request,
							inv -> inv.getPayment().getId(),
							list -> new InvoiceTotalAmountByPaymentStatDTO((long)list.size(),
									list.get(0).getInvoiceNumber(),
									list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
									list.get(0).getPayment().getId()));
				}
				else {
					return invoiceRepository.countInvoiceTotalAmountByPayment(request.paymentId(), request.fromDate(), request.toDate());
				}
			}
			default -> throw new ValidationException("Unknown strategy: " + strategy);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<InvoiceTotalAmountBySalesOrderStatDTO> getInvoiceStatisticsBySalesOrder(
			InvoiceStatBySalesOrderRequest request) {
		long totalInvoices = invoiceRepository.count();
		InvoiceStatStrategy strategy = request.strategy() != null ? request.strategy() : InvoiceStatStrategy.AUTO;
		switch(strategy) {
		case SQL -> {
			if(totalInvoices < 10000) {
				return aggregateInMemory(request,
						inv -> inv.getSalesOrder().getId(),
						list -> new InvoiceTotalAmountBySalesOrderStatDTO((long)list.size(),
								list.get(0).getInvoiceNumber(),
								list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
								list.get(0).getSalesOrder().getId()));
			}
			else {
				return invoiceRepository.countInvoiceTotalAmountBySalesOrder(request.salesOrderId(), request.fromDate(), request.toDate());
			}
		}
		case MEMORY -> {
			return aggregateInMemory(request,
					inv -> inv.getSalesOrder().getId(),
					list -> new InvoiceTotalAmountBySalesOrderStatDTO((long)list.size(),
							list.get(0).getInvoiceNumber(),
							list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
							list.get(0).getSalesOrder().getId()));
		}
		case AUTO -> {
			if(totalInvoices < 10000) {
				return aggregateInMemory(request,
						inv -> inv.getSalesOrder().getId(),
						list -> new InvoiceTotalAmountBySalesOrderStatDTO((long)list.size(),
								list.get(0).getInvoiceNumber(),
								list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
								list.get(0).getSalesOrder().getId()));
			}
			else {
				return invoiceRepository.countInvoiceTotalAmountBySalesOrder(request.salesOrderId(), request.fromDate(), request.toDate());
			}
		}
		default -> throw new ValidationException("Unknown strategy " + strategy);
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public InvoiceResponse trackInvoice(Long id) {
		Invoice inv = invoiceRepository.trackInvoice(id).orElseThrow(() -> new ValidationException("Invoice not found with id "+id));
		return new InvoiceResponse(inv);
	}

	@Transactional(readOnly = true)
	@Override
	public List<InvoiceResponse> findByReports(Long id, String note) {
		if(id != null) validateInvoiceId(id);
		if(note != null && !note.trim().isEmpty()) validateString(note, "Note");
		List<Invoice> items = invoiceRepository.findByReports(id, note);
		return items.stream().map(invoiceMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional
	@Override
	public InvoiceResponse confirmInvoice(Long id) {
		Invoice inv = invoiceRepository.findById(id).orElseThrow(() -> new ValidationException("Invoice not found with id "+id));
		inv.setConfirmed(true);
		inv.setTypeStatus(InvoiceTypeStatus.CONFIRMED);
		return new InvoiceResponse(invoiceRepository.save(inv));
	}

	@Transactional
	@Override
	public InvoiceResponse cancelInvoice(Long id) {
		Invoice inv = invoiceRepository.findById(id).orElseThrow(() -> new ValidationException("Invoice not found with id "+id));
		if(inv.getTypeStatus() != InvoiceTypeStatus.CONFIRMED && inv.getTypeStatus() != InvoiceTypeStatus.NEW) {
			throw new ValidationException("Only NEW or CONFIRMED invoices can be cancelled");
		}
		inv.setTypeStatus(InvoiceTypeStatus.CANCELLED);
		return new InvoiceResponse(invoiceRepository.save(inv));
	}

	@Transactional
	@Override
	public InvoiceResponse closeInvoice(Long id) {
		Invoice inv = invoiceRepository.findById(id).orElseThrow(() -> new ValidationException("Invoice not found with id "+id));
		if(inv.getTypeStatus() != InvoiceTypeStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED invoices can be closed");
		}
		inv.setTypeStatus(InvoiceTypeStatus.CLOSED);
		return new InvoiceResponse(invoiceRepository.save(inv));
	}

	@Transactional
	@Override
	public InvoiceResponse changeStatus(Long id, InvoiceTypeStatus status) {
		Invoice inv = invoiceRepository.findById(id).orElseThrow(() -> new ValidationException("Invoice not found with id "+id));
		validateInvoiceTypeStatus(status);
		if(inv.getTypeStatus() == InvoiceTypeStatus.CLOSED) {
			throw new ValidationException("Closed invoices cannot change status");
		}
		if(status == InvoiceTypeStatus.CONFIRMED) {
			if(inv.getTypeStatus() != InvoiceTypeStatus.NEW) {
				throw new ValidationException("Only NEW invoices can be confirmed");
			}
			inv.setConfirmed(true);
		}
		inv.setTypeStatus(status);
		return new InvoiceResponse(invoiceRepository.save(inv));
	}

	@Transactional
	@Override
	public InvoiceResponse saveInvoice(InvoiceRequest request) {
		Invoice inv = Invoice.builder()
				.issueDate(LocalDateTime.now())
				.dueDate(request.dueDate())
				.status(request.status())
				.totalAmount(request.totalAmount())
				.buyer(fetchBuyer(request.buyerId()))
				.relatedSales(fetchSales(request.salesId()))
				.payment(fetchPayment(request.paymentId()))
				.note(request.note())
				.salesOrder(fetchSalesOrder(request.salesOrderId()))
				.createdBy(fetchCreatedBy(request.createdById()))
				.typeStatus(request.typeStatus())
				.confirmed(request.confirmed())
				.build();
		Invoice saved = invoiceRepository.save(inv);
		return new InvoiceResponse(saved);
	}
	
	private final AbstractSaveAsService<Invoice, InvoiceResponse> saveAsHelper = new AbstractSaveAsService<Invoice, InvoiceResponse>() {
		
		@Override
		protected InvoiceResponse toResponse(Invoice entity) {
			return new InvoiceResponse(entity);
		}
		
		@Override
		protected JpaRepository<Invoice, Long> getRepository() {
			return invoiceRepository;
		}
		
		@Override
		protected Invoice copyAndOverride(Invoice source, Map<String, Object> overrides) {
			return Invoice.builder()
					.invoiceNumber((String) overrides.getOrDefault("invoiceNumber", source.getInvoiceNumber()))
					.dueDate((LocalDateTime) overrides.getOrDefault("dueDate", source.getDueDate()))
					.status((InvoiceStatus) overrides.getOrDefault("status", source.getStatus()))
					.totalAmount((BigDecimal) overrides.getOrDefault("totalAmount", source.getTotalAmount()))
					.buyer(fetchBuyer((Long) overrides.getOrDefault("buyerId", source.getBuyer().getId())))
					.relatedSales(fetchSales((Long) overrides.getOrDefault("relatedSales", source.getRelatedSales().getId())))
					.payment(fetchPayment((Long) overrides.getOrDefault("paymentId", source.getPayment().getId())))
					.note((String) overrides.getOrDefault("note", source.getNote()))
					.salesOrder(fetchSalesOrder((Long) overrides.getOrDefault("salesOrderId", source.getSalesOrder().getId())))
					.createdBy(fetchCreatedBy((Long) overrides.getOrDefault("createdById", source.getCreatedBy().getId())))
					.build();
		}
	};

	@Transactional
	@Override
	public InvoiceResponse saveAs(InvoiceSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		Buyer buyer = fetchBuyer(request.buyerId());
		Sales relatedSales = fetchSales(request.relatedSalesId());
		Payment payment = fetchPayment(request.paymentId());
		SalesOrder so = fetchSalesOrder(request.salesOrderId());
		User user = fetchCreatedBy(request.createdById());
		if(request.invoiceNumber() != null) overrides.put("Invoice Number", request.invoiceNumber());
		if(request.dueDate() != null) overrides.put("Due Date", request.dueDate());
		if(request.status() != null) overrides.put("Status", request.status());
		if(request.totalAmount() != null) overrides.put("Total Amount", request.totalAmount());
		if(request.buyerId() != null) overrides.put("Buyer ID", buyer);
		if(request.relatedSalesId() != null) overrides.put("Related Sales Id", relatedSales);
		if(request.paymentId() != null) overrides.put("Payment ID", payment);
		if(request.note() != null) overrides.put("Note", request.note());
		if(request.salesOrderId() != null) overrides.put("Sales Order ID", so);
		if(request.createdById() != null) overrides.put("CreatedBy ID", user);
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}
	
	private final AbstractSaveAllService<Invoice, InvoiceResponse> saveAllHelper = new AbstractSaveAllService<Invoice, InvoiceResponse>() {
		
		@Override
		protected Function<Invoice, InvoiceResponse> toResponse() {
			return InvoiceResponse::new;
		}
		
		@Override
		protected JpaRepository<Invoice, Long> getRepository() {
			return invoiceRepository;
		}
		
		@Override
		protected void beforeSaveAll(List<Invoice> entities) {
		    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		    log.info("User '{}' is saving {} invoice ", currentUser, entities.size());
		}
	};

	@Transactional
	@Override
	public List<InvoiceResponse> saveAll(List<InvoiceRequest> requests) {
		if (requests == null || requests.isEmpty()) {
	        throw new ValidationException("Invoice request list must not be empty.");
	    }
		List<Invoice> items = requests.stream()
				.map(item -> Invoice.builder()
						.id(item.id())
						.dueDate(item.dueDate())
						.status(item.status())
						.totalAmount(item.totalAmount())
						.buyer(fetchBuyer(item.buyerId()))
						.relatedSales(fetchSales(item.salesId()))
						.payment(fetchPayment(item.paymentId()))
						.note(item.note())
						.salesOrder(fetchSalesOrder(item.salesOrderId()))
						.createdBy(fetchCreatedBy(item.createdById()))
						.typeStatus(item.typeStatus())
						.confirmed(item.confirmed())
						.build())
				.toList();
		return saveAllHelper.saveAll(items);
	}

	@Override
	public List<InvoiceResponse> generalSearch(InvoiceSearchRequest request) {
		Specification<Invoice> spec = InvoiceSpecification.fromRequest(request);
		List<Invoice> items = invoiceRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Invoices found for given criteria");
		}
		return items.stream().map(invoiceMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public InvoiceResponse trackInvoiceByBuyer(Long buyerId) {
		List<Invoice> items = invoiceRepository.trackInvoiceByBuyer(buyerId);
		if(items.isEmpty()) {
			throw new NoDataFoundException("Invoice with buyer-id "+buyerId + " not found");
		}
		Invoice inv = items.get(0);
		return new InvoiceResponse(inv);
	}

	@Transactional(readOnly = true)
	@Override
	public InvoiceResponse trackInvoiceBySales(Long salesId) {
		List<Invoice> items = invoiceRepository.trackInvoiceBySales(salesId);
		if(items.isEmpty()) {
			throw new NoDataFoundException("Invoice with sales-id " + salesId + " not found");
		}
		Invoice inv = items.get(0);
		return new InvoiceResponse(inv);
	}

	@Transactional(readOnly = true)
	@Override
	public InvoiceResponse trackInvoiceByPayment(Long paymentId) {
		List<Invoice> items = invoiceRepository.trackInvoiceByPayment(paymentId);
		if(items.isEmpty()) {
			throw new NoDataFoundException("Invoice with payment-id "+paymentId+ " not found");
		}
		Invoice inv = items.get(0);
		return new InvoiceResponse(inv);
	}

	@Transactional(readOnly = true)
	@Override
	public InvoiceResponse trackInvoiceBySalesOrder(Long salesOrderId) {
		List<Invoice> items = invoiceRepository.trackInvoiceBySalesOrder(salesOrderId);
		if(items.isEmpty()) {
			throw new NoDataFoundException("Invoice with sales-order-id "+salesOrderId+ " not found");
		}
		Invoice inv = items.get(0);
		return new InvoiceResponse(inv);
	}
	
	private List<InvoiceTotalAmountByBuyerStatDTO> aggregateInMemoryByBuyer(InvoiceStatByBuyerRequest request) {
	    return aggregateInMemory(request,
	    		inv -> inv.getBuyer().getId(),
	    		list -> new InvoiceTotalAmountByBuyerStatDTO((long)list.size(),
	    				list.get(0).getInvoiceNumber(),
	    				list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
	    				list.get(0).getBuyer().getId()
	    		)
	    );
	}
	
	private <T,R extends InvoiceSpecificationRequest> List<T> aggregateInMemory(R request, Function<Invoice, Long> groupByFn, Function<List<Invoice>, T> dtoMapper){
		List<Invoice> invoices = invoiceRepository.findAll(InvoiceSpecification.withDynamicFilters(request));
		return invoices.stream().collect(Collectors.groupingBy(groupByFn))
				.values()
				.stream()
				.map(dtoMapper)
				.toList();
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}

	private String generateInvoiceNumber() {
		String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		long count = invoiceRepository.countByIssueDateBetween(
			LocalDate.now().atStartOfDay(),
			LocalDate.now().plusDays(1).atStartOfDay()
		) + 1;
		return String.format("INV-%s-%04d", datePart, count);
	}

	
	private void validateDoubleString(String s1, String s2) {
        if (s1 == null || s1.trim().isEmpty() || s2 == null || s2.trim().isEmpty()) {
            throw new IllegalArgumentException("Both string must not be null or empty");
        }
    }
	
	private void validateOrderStatus(OrderStatus status) {
		if(status == null) {
			throw new IllegalArgumentException("OrderStatus status must not be null");
		}
	}
	
	private void validatePaymentMehod(PaymentMethod method) {
		if(method == null) {
			throw new IllegalArgumentException("PaymentMethod method must not be null");
		}
	}
	
	private void validateBuyerExists(Long buyerId) {
	    if (buyerId == null) {
	        throw new BuyerNotFoundException("Buyer ID must not be null");
	    }
	    if (!buyerRepository.existsById(buyerId)) {
	        throw new BuyerNotFoundException("Buyer not found with ID: " + buyerId);
	    }
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
    
    private void validatePaymentStatus(PaymentStatus status) {
    	if(status == null) {
    		throw new IllegalArgumentException("PaymentStatus status must not be null");
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

    private Invoice validateInvoiceId(Long id) {
    	if(id == null) {
    		throw new ValidationException("Invoice ID must not be null");
    	}
    	return invoiceRepository.findById(id).orElseThrow(() -> new ValidationException("Invoice not found with id "+id));
    }
    
    private void validateInvoiceTypeStatus(InvoiceTypeStatus status) {
    	Optional.of(status)
    		.orElseThrow(() -> new ValidationException("InvoiceTypeStatus status must not be null"));
    }

}
