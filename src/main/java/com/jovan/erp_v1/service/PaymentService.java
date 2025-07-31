package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.PaymentMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Payment;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.PaymentRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.request.PaymentRequest;
import com.jovan.erp_v1.response.PaymentResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentMapper paymentMapper;
	private final BuyerRepository buyerRepository;
	private final SalesRepository salesRepository;
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Transactional
	@Override
	public PaymentResponse createPayment(PaymentRequest request) {
		logger.info("Creating payment for buyerId={}, relatedSalesId={}", request.buyerId(), request.relatedSalesId());
		validatePaymentRequest(request);
		Buyer buyer = fetchBuyer(request.buyerId());
		Sales sales = fetchSales(request.relatedSalesId());
		Payment payment = paymentMapper.toEntity(request,buyer,sales);
		Payment savedPayment = paymentRepository.save(payment);
		logger.info("Payment successfully created with ID={}", savedPayment.getId());
		return paymentMapper.toResponse(savedPayment);
	}

	@Override
	public PaymentResponse getPaymentById(Long id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
		return paymentMapper.toResponse(payment);
	}

	@Override
	public List<PaymentResponse> getAllPayments() {
		List<Payment> items = paymentRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Payment list is empty");
		}
		return paymentRepository.findAll().stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> getPaymentsByBuyer(Long buyerId) {
		fetchBuyer(buyerId);
		List<Payment> items = paymentRepository.findByBuyerId(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for buyer-id %d is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
		validatePaymentStatus(status);
		List<Payment> items = paymentRepository.findByStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> getPaymentsByMethod(PaymentMethod method) {
		validatePaymentMethod(method);
		List<Payment> items = paymentRepository.findByMethod(method);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for method %s is found", method);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void deletePayment(Long id) {
		if (!paymentRepository.existsById(id)) {
			throw new PaymentNotFoundException("Payment not found wuth id: " + id);
		}
		paymentRepository.deleteById(id);
	}

	@Transactional
	@Override
	public PaymentResponse updatePayment(Long id, PaymentRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		logger.info("Updating payment with ID={}", id);
	    Payment payment = paymentRepository.findById(id)
	        .orElseThrow(() -> {
	            logger.warn("Payment not found with ID={}", id);
	            return new PaymentNotFoundException("Payment not found with id: " + id);
	        });
		validatePaymentRequest(request);
		Buyer buyer = payment.getBuyer();
		if(request.buyerId() != null && (buyer.getId() == null || !request.buyerId().equals(buyer.getId()))){
			buyer = fetchBuyer(request.buyerId());
		}
		Sales sales = payment.getRelatedSales();
		if(request.relatedSalesId() != null && (sales.getId() == null || !request.relatedSalesId().equals(sales.getId()))) {
			sales = fetchSales(request.relatedSalesId());
		}
		paymentMapper.toUpdateEntity(payment, request, buyer, sales);
		Payment updated = paymentRepository.save(payment);
	    logger.info("Payment with ID={} successfully updated", updated.getId());
	    return paymentMapper.toResponse(updated);
	}
	//nove metode

	@Override
	public List<PaymentResponse> findByAmount(BigDecimal amount) {
		validateBigDecimal(amount);
		List<Payment> items = paymentRepository.findByAmount(amount);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for amount  %s is found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByAmountGreaterThan(BigDecimal amount) {
		validateBigDecimal(amount);
		List<Payment> items = paymentRepository.findByAmountGreaterThan(amount);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for amount greater than %s is found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByAmountLessThan(BigDecimal amount) {
		validateBigDecimalNonNegative(amount);
		List<Payment> items = paymentRepository.findByAmountLessThan(amount);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for amount less than %s is found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByPaymentDate(LocalDateTime paymentDate) {
		DateValidator.validatePastOrPresent(paymentDate, "Payment date");
		List<Payment> items = paymentRepository.findByPaymentDate(paymentDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Payment for payment-date %s is found", paymentDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName) {
		validateString(buyerCompanyName);
		List<Payment> items = paymentRepository.findByBuyer_CompanyNameContainingIgnoreCase(buyerCompanyName);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for buyer's company-name %s is found", buyerCompanyName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_PibContainingIgnoreCase(String pib) {
		validatePibIsUnique(pib);
		List<Payment> items = paymentRepository.findByBuyer_PibContainingIgnoreCase(pib);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for buyer's pib %s is found", pib);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_AddressContainingIgnoreCase(String buyerAddress) {
		validateString(buyerAddress);
		List<Payment> items = paymentRepository.findByBuyer_AddressContainingIgnoreCase(buyerAddress);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for buyer's address %s is found", buyerAddress);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_EmailContainingIgnoreCase(String buyerEmail) {
		validateString(buyerEmail);
		List<Payment> items = paymentRepository.findByBuyer_EmailContainingIgnoreCase(buyerEmail);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for buyer's email %s is found", buyerEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_PhoneNumber(String buyerPhoneNumber) {
		validateString(buyerPhoneNumber);
		List<Payment> items = paymentRepository.findByBuyer_PhoneNumber(buyerPhoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for buyer's phone-number %s is found", buyerPhoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_Id(Long relatedSalesId) {
		fetchSales(relatedSalesId);
		List<Payment> items = paymentRepository.findByRelatedSales_Id(relatedSalesId);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for sales-id %d is found", relatedSalesId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_CreatedAt(LocalDateTime createdAt) {
		DateValidator.validateNotInFuture(createdAt, "Date and Time");
		List<Payment> items = paymentRepository.findByRelatedSales_CreatedAt(createdAt);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Payment for sales' createdAt %s is found", createdAt.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_TotalPriceGreaterThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		List<Payment> items = paymentRepository.findByRelatedSales_TotalPriceGreaterThan(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for sales total price greater than %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_TotalPriceLessThan(BigDecimal totalPrice) {
		validateBigDecimalNonNegative(totalPrice);
		List<Payment> items = paymentRepository.findByRelatedSales_TotalPriceLessThan(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for sales total price less than %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_TotalPrice(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		List<Payment> items = paymentRepository.findByRelatedSales_TotalPrice(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for sales total price %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_SalesDescriptionContainingIgnoreCase(String salesDescription) {
		validateString(salesDescription);
		List<Payment> items = paymentRepository.findByRelatedSales_SalesDescriptionContainingIgnoreCase(salesDescription);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for sales description %s is found", salesDescription);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_Buyer_Id(Long buyerId) {
		fetchBuyer(buyerId);
		List<Payment> items = paymentRepository.findByRelatedSales_Buyer_Id(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for sales bound to buyer-id %d is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return paymentRepository.findByRelatedSales_Buyer_Id(buyerId).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_IdAndStatus(Long buyerId, PaymentStatus status) {
		validatePaymentStatus(status);
		List<Payment> items = paymentRepository.findByBuyer_IdAndStatus(buyerId, status);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for buyer-id %d and payment-status %s is found", 
					buyerId, status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByPaymentDateBetweenAndMethod(LocalDateTime start, LocalDateTime end,
			PaymentMethod method) {
		DateValidator.validateRange(start, end);
		validatePaymentMethod(method);
		List<Payment> items = paymentRepository.findByPaymentDateBetweenAndMethod(start, end, method);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Payment for date between %s and %s , and method %s is found", 
					start.format(formatter),end.format(formatter), method);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_SalesDescriptionContainingIgnoreCaseAndBuyer_Id(String description,
			Long buyerId) {
		fetchBuyer(buyerId);
		validateString(description);
		List<Payment> items = paymentRepository.findByRelatedSales_SalesDescriptionContainingIgnoreCaseAndBuyer_Id(description, buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No Payment for sales description %s and buyer-id %d is found", description,buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	/*@Override
	public Long countByBuyer_Id(Long buyerId) {
		fetchBuyer(buyerId);
		Long count = 0L;
		List<Payment> payments = paymentRepository.findAll();
		for(Payment p : payments) {
			if(p.getBuyer() != null && buyerId.equals(p.getBuyer().getId())) {
				count++;
			}
		}
		return count;
	}*/
	
	@Override
	public Long countByBuyer_Id(Long buyerId) {
		fetchBuyer(buyerId);
		return paymentRepository.countByBuyer_Id(buyerId);
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("String must not be null nor empty.");
		}
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}
	
	private Buyer fetchBuyer(Long buyerId) {
		if(buyerId == null) {
			throw new BuyerNotFoundException("Buyer ID must not be null");
		}
		return buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id "+buyerId));
	}
	
	private Sales fetchSales(Long salesId) {
		if(salesId == null) {
			throw new SalesNotFoundException("Sales ID must not be null");
		}
		return salesRepository.findById(salesId).orElseThrow(() -> new SalesNotFoundException("Sales not found with id: "+salesId));
	}
	
	private void validatePaymentStatus(PaymentStatus status) {
		if(status == null) {
			throw new IllegalArgumentException("PaymentStatus status must not be null");
		}
	}
	
	private void validatePaymentMethod(PaymentMethod method) {
		if(method == null) {
			throw new IllegalArgumentException("PaymentMethod method must not be null");
		}
	}
	
	private void validateReferenceNumberIsUnique(String referenceNumber) {
	    if (referenceNumber == null || referenceNumber.trim().isEmpty()) {
	        throw new IllegalArgumentException("Referentni broj ne sme biti prazan.");
	    }
	    if (paymentRepository.existsByReferenceNumber(referenceNumber)) {
	        throw new IllegalArgumentException("Referentni broj već postoji.");
	    }
	}
	
	private void validatePibIsUnique(String pib) {
		if(pib == null || pib.trim().isEmpty()) {
			throw new IllegalArgumentException("Pib must not be null nor empty");
		}
		if(!paymentRepository.existsByBuyer_Pib(pib)) {
			throw new IllegalArgumentException("Pib already exist");
		}
	}
	
	private void validatePaymentRequest(PaymentRequest request) {
		validateBigDecimal(request.amount());
		DateValidator.validateNotInFuture(request.paymentDate(), "Payment date");
		validatePaymentMethod(request.method());
		validatePaymentStatus(request.status());
		validateReferenceNumberIsUnique(request.referenceNumber());
		
	}

	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
}
