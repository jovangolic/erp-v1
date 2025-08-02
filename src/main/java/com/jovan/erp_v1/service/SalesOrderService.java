package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.DuplicateOrderNumberException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.InvoiceNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.PaymentReferenceNotFoundException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.SalesOrderMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.InvoiceRepository;
import com.jovan.erp_v1.repository.PaymentRepository;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.request.SalesOrderRequest;
import com.jovan.erp_v1.response.SalesOrderResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesOrderService implements ISalesOrder {

	private final SalesOrderRepository salesOrderRepository;
	private final SalesOrderMapper salesOrderMapper;
	private final BuyerRepository buyerRepository;
	private final InvoiceRepository invoiceRepository;
	private final GoodsRepository goodsRepository;
	private final SalesRepository salesRepository;
	private final ProcurementRepository procurementRepository;
	private final UserRepository userRepository;
	private final PaymentRepository paymentRepository;

	@Transactional
	@Override
	public SalesOrderResponse createOrder(SalesOrderRequest request) {
		validateString(request.orderNumber());
		validateString(request.note());
		validateBigDecimal(request.totalAmount());
		validateOrderStatus(request.status());
		validateItemSalesRequest(request.items());
		Buyer buyer = fetchBuyerId(request.buyerId());
		Invoice invoice = fetchInvoice(request.invoiceId());
		SalesOrder order = salesOrderMapper.toEntity(request,buyer,invoice);
		String orderNumber = generateOrderNumber();
		checkOrderNumber(orderNumber, true);
		order.setOrderNumber(orderNumber);
		SalesOrder saved = salesOrderRepository.save(order);
		return salesOrderMapper.toResponse(saved);
	}

	@Transactional
	@Override
	public SalesOrderResponse updateOrder(Long id, SalesOrderRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		SalesOrder salesOrder = salesOrderRepository.findById(id)
				.orElseThrow(() -> new SalesOrderNotFoundException("Sales order not found with id: " + id));
		validateItemSalesRequest(request.items());
		validateString(request.orderNumber());
		validateString(request.note());
		validateBigDecimal(request.totalAmount());
		validateOrderStatus(request.status());
		Buyer buyer = salesOrder.getBuyer();
		if(request.buyerId() != null && (buyer.getId() == null || !request.buyerId().equals(buyer.getId()))) {
			buyer = fetchBuyerId(request.buyerId());
		}
		Invoice invoice = salesOrder.getInvoice();
		if(request.invoiceId() != null && (invoice.getId() == null || !request.invoiceId().equals(invoice.getId()))) {
			invoice = fetchInvoice(request.invoiceId());
		}
		salesOrderMapper.toEntityUpdate(salesOrder, request, buyer, invoice);
		List<ItemSales> itemSalesList = mapToItemSalesList(request.items(), salesOrder);
		salesOrder.setItems(itemSalesList);
		SalesOrder saved = salesOrderRepository.save(salesOrder);
		return salesOrderMapper.toResponse(saved);
	}

	@Override
	public String generateOrderNumber() {
		// Npr: SO-2025-000123
		long count = salesOrderRepository.count();
		return String.format("SO-%d-%06d", Year.now().getValue(), count + 1);
	}

	@Override
	public List<SalesOrderResponse> getAllOrders() {
		List<SalesOrder> items = salesOrderRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("SalesOrder list is empty");
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public SalesOrderResponse getOrderById(Long id) {
		SalesOrder salesId = salesOrderRepository.findById(id)
				.orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + id));
		return new SalesOrderResponse(salesId);
	}

	@Transactional
	@Override
	public void deleteSales(Long id) {
		if (!salesRepository.existsById(id)) {
			throw new SalesOrderNotFoundException("SalesOrder not found with id: " + id);
		}
		salesOrderRepository.deleteById(id);
	}

	private List<ItemSales> mapToItemSalesList(List<ItemSalesRequest> items, SalesOrder salesOrder) {
		return items.stream()
				.map(item -> {
					ItemSales itemSales = new ItemSales();
					itemSales.setQuantity(item.quantity());
					itemSales.setUnitPrice(item.unitPrice());
					Goods goods = goodsRepository.findById(item.goodsId())
							.orElseThrow(
									() -> new GoodsNotFoundException("Goods not found with id: " + item.goodsId()));
					itemSales.setGoods(goods);
					Procurement procurement = procurementRepository.findById(item.procurementId())
							.orElseThrow(() -> new ProcurementNotFoundException(
									"Procurement not found with id: " + item.procurementId()));
					itemSales.setProcurement(procurement);
					Sales sales = salesRepository.findById(item.salesId()).orElseThrow(
							() -> new SalesNotFoundException("Sales not found with id: " + item.salesId()));
					itemSales.setSales(sales); // ili .setSalesOrder(salesOrder) 
					return itemSales;
				})
				.collect(Collectors.toList());
	}

	// nove metode
	
	@Override
	public List<SalesOrderResponse> findByBuyer_Id(Long buyerId){
		fetchBuyerId(buyerId);
		List<SalesOrder> items = salesOrderRepository.findByBuyer_Id(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for buyer-id %d is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_CompanyNameContainingIgnoreCase(String companyName) {
		validateString(companyName);
		List<SalesOrder> items = salesOrderRepository.findByBuyer_CompanyNameContainingIgnoreCase(companyName);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for buyer's company-name %s is found", companyName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_PibContainingIgnoreCase(String pib) {
		checkPib(pib);
		List<SalesOrder> items = salesOrderRepository.findByBuyer_PibContainingIgnoreCase(pib);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for buyer's pib %s is found", pib);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_Address(String address) {
		validateString(address);
		List<SalesOrder> items = salesOrderRepository.findByBuyer_Address(address);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for buyer's address %s is found", address);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_ContactPerson(String contactPerson) {
		validateString(contactPerson);
		List<SalesOrder> items = salesOrderRepository.findByBuyer_ContactPerson(contactPerson);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for buyer's contact-person %s is found", contactPerson);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_EmailContainingIgnoreCase(String email) {
		validateString(email);
		List<SalesOrder> items = salesOrderRepository.findByBuyer_EmailContainingIgnoreCase(email);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for buyer's email %s is found", email);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_PhoneNumberContainingIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<SalesOrder> items = salesOrderRepository.findByBuyer_PhoneNumberContainingIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for buyer's phone-number %s is found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_InvoiceNumberContainingIgnoreCase(String invoiceNumber) {
		checkInvoiceNumber(invoiceNumber);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_InvoiceNumberContainingIgnoreCase(invoiceNumber);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice-number %s is found", invoiceNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_TotalAmount(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_TotalAmount(totalAmount);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice's total-amount%s is found", totalAmount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_TotalAmountGreaterThan(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_TotalAmountGreaterThan(totalAmount);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice's total-amount greater than %s is found", totalAmount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_TotalAmountLessThan(BigDecimal totalAmount) {
		validateBigDecimalNonNegative(totalAmount);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_TotalAmountLessThan(totalAmount);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice's total-amount less than %s is found", totalAmount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<SalesOrderResponse> findByInvoice_TotalAmountBetween(BigDecimal min, BigDecimal max){
		validateMinAndMax(min, max);
		List<SalesOrder> items = salesOrderRepository.findByInvoiceTotalAmountBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice's totam-amount between %s and %s is found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_IssueDate(LocalDateTime issueDate) {
		DateValidator.validatePastOrPresent(issueDate, "Issue date");
		List<SalesOrder> items = salesOrderRepository.findByInvoice_IssueDate(issueDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice issue-date %s is found", issueDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_IssueDateAfter(LocalDateTime date) {
		DateValidator.validateNotInPast(date, "Issue date");
		List<SalesOrder> items = salesOrderRepository.findByInvoice_IssueDateAfter(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice issue-date after %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_IssueDateBefore(LocalDateTime date) {
		DateValidator.validateNotInFuture(date, "Issue date");
		List<SalesOrder> items = salesOrderRepository.findByInvoice_IssueDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice issue-date before %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_IssueDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_IssueDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice issue-date between %s and %s is found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_DueDate(LocalDateTime dueDate) {
		DateValidator.validateNotNull(dueDate, "Due-date");
		List<SalesOrder> items = salesOrderRepository.findByInvoice_DueDate(dueDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice due-date %s is found", dueDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_DueDateAfter(LocalDateTime date) {
		DateValidator.validateNotInPast(date, "Date");
		List<SalesOrder> items = salesOrderRepository.findByInvoice_DueDateAfter(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice due-date after %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_DueDateBefore(LocalDateTime date) {
		DateValidator.validateNotInFuture(date, "Date");
		List<SalesOrder> items = salesOrderRepository.findByInvoice_DueDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice due-date before %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_DueDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_DueDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice due-date between %s and %s is found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_NoteContainingIgnoreCase(String note) {
		validateString(note);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_NoteContainingIgnoreCase(note);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice note %s is found", note);
			throw new NoDataFoundException(msg); 
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Buyer_Id(Long buyerId) {
		fetchBuyerId(buyerId);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Buyer_Id(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to buyer-id %d is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_RelatedSales_Id(Long relatedSalesId) {
		fetchSalesId(relatedSalesId);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_RelatedSales_Id(relatedSalesId);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to related-sales id %d is found", relatedSalesId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_Id(Long paymentId) {
		fetchPaymentId(paymentId);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_Id(paymentId);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice boundto payment-id %d is found", paymentId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_Amount(BigDecimal amount) {
		validateBigDecimal(amount);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_Amount(amount);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to payment-amount %s is found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_AmountGreaterThan(BigDecimal amount) {
		validateBigDecimal(amount);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_AmountGreaterThan(amount);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to payment amount greater than %s is found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_AmountLessThan(BigDecimal amount) {
		validateBigDecimalNonNegative(amount);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_AmountLessThan(amount);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to payment amount less than %s is found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_Method(PaymentMethod method) {
		validatePaymentMethod(method);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_Method(method);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to payment-method %s is found", method);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_Status(PaymentStatus status) {
		validatePaymentStatus(status);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to payment-status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_ReferenceNumberLikeIgnoreCase(String referenceNumber) {
		checkReferenceNumberExists(referenceNumber);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_ReferenceNumberLikeIgnoreCase(referenceNumber);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to payment reference-number %s is found", referenceNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_PaymentDate(LocalDateTime paymentDate) {
		DateValidator.validateNotNull(paymentDate, "Payment date");
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_PaymentDate(paymentDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice bound to payment-date %s is found", paymentDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_PaymentDateBetween(LocalDateTime startDate,
			LocalDateTime endDate) {
		DateValidator.validateRange(startDate, endDate);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_Payment_PaymentDateBetween(startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No SalesOrder for invoice boud to payment date between %s and %s is found",
					startDate.format(formatter),endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_Id(Long userId) {
		fetchUserId(userId);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_CreatedBy_Id(userId);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for Invoice bound to createdBy-id %d is found", userId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_EmailLikeIgnoreCase(String email) {
		validateString(email);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_CreatedBy_EmailLikeIgnoreCase(email);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for Invoice bound to createdBy email %s is found", email);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_Address(String address) {
		validateString(address);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_CreatedBy_Address(address);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for Invoice bound to createdBy address %s is found", address);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<SalesOrder> items = salesOrderRepository.findByInvoice_CreatedBy_PhoneNumberLikeIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No SalesOrder for invoice bound to cretedBy phone-number %s is found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(
			String firstName,
			String lastName) {
		validateString(firstName);
		validateString(lastName);
		return salesOrderRepository
				.findByInvoice_CreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(firstName, lastName).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	private void validateItemSalesRequest(List<ItemSalesRequest> items) {
		if (items == null || items.isEmpty()) {
			throw new IllegalArgumentException("List of items must not be null nor empty");
		}
		for (ItemSalesRequest item : items) {
			validateItemSalesRequest(item);
		}
	}

	private void validateItemSalesRequest(ItemSalesRequest items) {
		if (items.goodsId() == null) {
			throw new GoodsNotFoundException("Goods ID must not be null");
		}
		if (items.salesId() == null) {
			throw new SalesNotFoundException("Sales ID must not be null");
		}
		if (items.procurementId() == null) {
			throw new ProcurementNotFoundException("Procurement ID must not be null");
		}
		validateBigDecimal(items.quantity());
		validateBigDecimal(items.unitPrice());
	}

	private void validateBigDecimal(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}

	private void validateString(String str) {
		if (str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("String must not be null nor empty");
		}
	}

	private void validateOrderStatus(OrderStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("OrderStatus status must not be null");
		}
	}

	private Payment fetchPaymentId(Long id) {
		if (id == null) {
			throw new PaymentNotFoundException("Payment ID muast not be null");
		}
		return paymentRepository.findById(id)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
	}

	private Buyer fetchBuyerId(Long buyerId) {
		if (buyerId == null) {
			throw new BuyerNotFoundException("Buyer ID must not be null");
		}
		return buyerRepository.findById(buyerId)
				.orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + buyerId));
	}

	private User fetchUserId(Long userId) {
		if (userId == null) {
			throw new UserNotFoundException("User ID must nut be null");
		}
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
	}

	private Sales fetchSalesId(Long salesId) {
		if (salesId == null) {
			throw new SalesNotFoundException("Sales ID must not be null");
		}
		return salesRepository.findById(salesId)
				.orElseThrow(() -> new SalesNotFoundException("Sales not found with id " + salesId));
	}

	private Invoice fetchInvoice(Long invoiceId) {
		if (invoiceId == null) {
			throw new InvoiceNotFoundException("Invoice ID must not be null");
		}
		return invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id " + invoiceId));
	}

	private void checkOrderNumber(String orderNumber, boolean shouldBeUnique) {
		boolean exists = salesOrderRepository.existsByOrderNumber(orderNumber);
		if (shouldBeUnique && exists) {
			throw new DuplicateOrderNumberException("Order number already exists: " + orderNumber);
		} else if (!shouldBeUnique && !exists) {
			throw new SalesOrderNotFoundException("Order number not found: " + orderNumber);
		}
	}

	private void validatePaymentStatus(PaymentStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("PaymentStatus status must not be null");
		}
	}

	private void validatePaymentMethod(PaymentMethod method) {
		if (method == null) {
			throw new IllegalArgumentException("PaymentMethod method must not be null");
		}
	}

	private void checkInvoiceNumber(String invoiceNumber) {
		if (!salesOrderRepository.existsByInvoice_InvoiceNumber(invoiceNumber)) {
			throw new IllegalArgumentException("Invoice number not found: " + invoiceNumber);
		}
	}

	private void checkReferenceNumberExists(String referenceNumber) {
		if (!salesOrderRepository.existsByInvoice_Payment_ReferenceNumberLikeIgnoreCase(referenceNumber)) {
			throw new PaymentReferenceNotFoundException("Reference number not found: " + referenceNumber);
		}
	}

	private void checkPib(String pib) {
		if (!salesOrderRepository.existsByBuyer_Pib(pib)) {
			throw new BuyerNotFoundException("PIB  number not found " + pib);
		}
	}
	
	private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Min i Max ne smeju biti null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Min mora biti >= 0, a Max mora biti > 0");
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Min ne može biti veći od Max");
        }
    }
}