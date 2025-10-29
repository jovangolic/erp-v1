package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.jovan.erp_v1.enumeration.InvoiceStatus;
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
import com.jovan.erp_v1.statistics.invoice.InvoiceStatRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountByBuyerStatDTO;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
	public List<InvoiceTotalAmountByBuyerStatDTO> getInvoiceStatistics(InvoiceStatRequest request) {
		List<Invoice> invoices = invoiceRepository.findAll(InvoiceSpecification.withFilters(request));
		return invoices.stream()
		        .collect(Collectors.groupingBy(
		            inv -> inv.getBuyer().getId(),
		            Collectors.collectingAndThen(
		                Collectors.toList(),
		                list -> new InvoiceTotalAmountByBuyerStatDTO(
		                    (long) list.size(),
		                    list.get(0).getInvoiceNumber(),
		                    list.stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
		                    list.get(0).getBuyer().getId()
		                )
		            )
		        ))
		        .values()
		        .stream()
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

}
