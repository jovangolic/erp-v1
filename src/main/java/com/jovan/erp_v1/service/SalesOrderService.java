package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
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
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.PaymentReferenceNotFoundException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.mapper.SalesOrderMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.Product;
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
		validateItemSalesRequest(request.items());
		SalesOrder order = salesOrderMapper.toEntity(request);
		String orderNumber = generateOrderNumber();
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
		salesOrder.setOrderDate(request.orderDate());
		salesOrder.setTotalAmount(request.totalAmount());
		salesOrder.setStatus(request.status());
		salesOrder.setNote(request.note());
		// Povezivanje Buyer-a
		Buyer buyer = fetchBuyerId(request.buyerId());
		salesOrder.setBuyer(buyer);
		// Povezivanje Invoice-a
		Invoice invoice = fetchInvoice(request.invoiceId());
		salesOrder.setInvoice(invoice);
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
		return salesOrderRepository.findAll().stream()
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
					itemSales.setSales(sales); // ili .setSalesOrder(salesOrder) ako si to ime koristio
					return itemSales;
				})
				.collect(Collectors.toList());
	}

	// nove metode
	
	@Override
	public List<SalesOrderResponse> findByBuyer_Id(Long buyerId){
		fetchBuyerId(buyerId);
		return salesOrderRepository.findByBuyer_Id(buyerId).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_CompanyNameContainingIgnoreCase(String companyName) {
		validateString(companyName);
		return salesOrderRepository.findByBuyer_CompanyNameContainingIgnoreCase(companyName).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_PibContainingIgnoreCase(String pib) {
		checkPib(pib);
		return salesOrderRepository.findByBuyer_PibContainingIgnoreCase(pib).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_Address(String address) {
		validateString(address);
		return salesOrderRepository.findByBuyer_Address(address).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_ContactPerson(String contactPerson) {
		validateString(contactPerson);
		return salesOrderRepository.findByBuyer_ContactPerson(contactPerson).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_EmailContainingIgnoreCase(String email) {
		validateString(email);
		return salesOrderRepository.findByBuyer_EmailContainingIgnoreCase(email).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByBuyer_PhoneNumberContainingIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		return salesOrderRepository.findByBuyer_PhoneNumberContainingIgnoreCase(phoneNumber).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_InvoiceNumberContainingIgnoreCase(String invoiceNumber) {
		checkInvoiceNumber(invoiceNumber);
		return salesOrderRepository.findByInvoice_InvoiceNumberContainingIgnoreCase(invoiceNumber).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_TotalAmount(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		return salesOrderRepository.findByInvoice_TotalAmount(totalAmount).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_TotalAmountGreaterThan(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		return salesOrderRepository.findByInvoice_TotalAmountGreaterThan(totalAmount).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_TotalAmountLessThan(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		return salesOrderRepository.findByInvoice_TotalAmountLessThan(totalAmount).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<SalesOrderResponse> findByInvoice_TotalAmountBetween(BigDecimal min, BigDecimal max){
		validateBigDecimal(min);
		validateBigDecimal(max);
		return salesOrderRepository.findByInvoiceTotalAmountBetween(min, max).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_IssueDate(LocalDateTime issueDate) {
		DateValidator.validatePastOrPresent(issueDate, "Issue date");
		return salesOrderRepository.findByInvoice_IssueDate(issueDate).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_IssueDateAfter(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Issue date");
		return salesOrderRepository.findByInvoice_IssueDateAfter(date).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_IssueDateBefore(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Issue date");
		return salesOrderRepository.findByInvoice_IssueDateBefore(date).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_IssueDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		return salesOrderRepository.findByInvoice_IssueDateBetween(start, end).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_DueDate(LocalDateTime dueDate) {
		DateValidator.validateNotNull(dueDate, "Due-date");
		return salesOrderRepository.findByInvoice_DueDate(dueDate).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_DueDateAfter(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date");
		return salesOrderRepository.findByInvoice_DueDateAfter(date).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_DueDateBefore(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date");
		return salesOrderRepository.findByInvoice_DueDateBefore(date).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_DueDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		return salesOrderRepository.findByInvoice_DueDateBetween(start, end).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_NoteContainingIgnoreCase(String note) {
		validateString(note);
		return salesOrderRepository.findByInvoice_NoteContainingIgnoreCase(note).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Buyer_Id(Long buyerId) {
		fetchBuyerId(buyerId);
		return salesOrderRepository.findByInvoice_Buyer_Id(buyerId).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_RelatedSales_Id(Long relatedSalesId) {
		fetchSalesId(relatedSalesId);
		return salesOrderRepository.findByInvoice_RelatedSales_Id(relatedSalesId).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_Id(Long paymentId) {
		fetchPaymentId(paymentId);
		return salesOrderRepository.findByInvoice_Payment_Id(paymentId).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_Amount(BigDecimal amount) {
		validateBigDecimal(amount);
		return salesOrderRepository.findByInvoice_Payment_Amount(amount).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_AmountGreaterThan(BigDecimal amount) {
		validateBigDecimal(amount);
		return salesOrderRepository.findByInvoice_Payment_AmountGreaterThan(amount).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_AmountLessThan(BigDecimal amount) {
		validateBigDecimal(amount);
		return salesOrderRepository.findByInvoice_Payment_AmountLessThan(amount).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_Method(PaymentMethod method) {
		validatePaymentMethod(method);
		return salesOrderRepository.findByInvoice_Payment_Method(method).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_Status(PaymentStatus status) {
		validatePaymentStatus(status);
		return salesOrderRepository.findByInvoice_Payment_Status(status).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_ReferenceNumberLikeIgnoreCase(String referenceNumber) {
		checkReferenceNumberExists(referenceNumber);
		return salesOrderRepository.findByInvoice_Payment_ReferenceNumberLikeIgnoreCase(referenceNumber).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_PaymentDate(LocalDateTime paymentDate) {
		DateValidator.validateNotNull(paymentDate, "Payment date");
		return salesOrderRepository.findByInvoice_Payment_PaymentDate(paymentDate).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_Payment_PaymentDateBetween(LocalDateTime startDate,
			LocalDateTime endDate) {
		DateValidator.validateRange(startDate, endDate);
		return salesOrderRepository.findByInvoice_Payment_PaymentDateBetween(startDate, endDate).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_Id(Long userId) {
		fetchUserId(userId);
		return salesOrderRepository.findByInvoice_CreatedBy_Id(userId).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_EmailLikeIgnoreCase(String email) {
		validateString(email);
		return salesOrderRepository.findByInvoice_CreatedBy_EmailLikeIgnoreCase(email).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_Address(String address) {
		validateString(address);
		return salesOrderRepository.findByInvoice_CreatedBy_Address(address).stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderResponse> findByInvoice_CreatedBy_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		return salesOrderRepository.findByInvoice_CreatedBy_PhoneNumberLikeIgnoreCase(phoneNumber).stream()
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

}