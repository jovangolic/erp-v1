package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.PaymentNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
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
		Payment payment = paymentMapper.toEntity(request);
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
		return paymentRepository.findAll().stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> getPaymentsByBuyer(Long buyerId) {
		Buyer buyer = buyerRepository.findById(buyerId)
				.orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + buyerId));
		return paymentRepository.findByBuyerId(buyer.getId()).stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
		validatePaymentStatus(status);
		return paymentRepository.findByStatus(status).stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> getPaymentsByMethod(PaymentMethod method) {
		validatePaymentMethod(method);
		return paymentRepository.findByMethod(method).stream()
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
		logger.info("Updating payment with ID={}", id);
	    Payment payment = paymentRepository.findById(id)
	        .orElseThrow(() -> {
	            logger.warn("Payment not found with ID={}", id);
	            return new PaymentNotFoundException("Payment not found with id: " + id);
	        });
		validatePaymentRequest(request);
		Payment updated = paymentRepository.save(payment);
	    logger.info("Payment with ID={} successfully updated", updated.getId());
	    return paymentMapper.toResponse(updated);
	}
	//nove metode

	@Override
	public List<PaymentResponse> findByAmount(BigDecimal amount) {
		validateBigDecimal(amount);
		return paymentRepository.findByAmount(amount).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByAmountGreaterThan(BigDecimal amount) {
		validateBigDecimal(amount);
		return paymentRepository.findByAmountGreaterThan(amount).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByAmountLessThan(BigDecimal amount) {
		validateBigDecimal(amount);
		return paymentRepository.findByAmountLessThan(amount).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByPaymentDate(LocalDateTime paymentDate) {
		DateValidator.validatePastOrPresent(paymentDate, "Payment date");
		return paymentRepository.findByPaymentDate(paymentDate).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName) {
		validateString(buyerCompanyName);
		return paymentRepository.findByBuyer_CompanyNameContainingIgnoreCase(buyerCompanyName).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_PibContainingIgnoreCase(String pib) {
		validatePibIsUnique(pib);
		return paymentRepository.findByBuyer_PibContainingIgnoreCase(pib).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_AddressContainingIgnoreCase(String buyerAddress) {
		validateString(buyerAddress);
		return paymentRepository.findByBuyer_AddressContainingIgnoreCase(buyerAddress).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_EmailContainingIgnoreCase(String buyerEmail) {
		validateString(buyerEmail);
		return paymentRepository.findByBuyer_EmailContainingIgnoreCase(buyerEmail).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_PhoneNumber(String buyerPhoneNumber) {
		validateString(buyerPhoneNumber);
		return paymentRepository.findByBuyer_PhoneNumber(buyerPhoneNumber).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_Id(Long relatedSalesId) {
		fetchSales(relatedSalesId);
		return paymentRepository.findByRelatedSales_Id(relatedSalesId).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_CreatedAt(LocalDateTime createdAt) {
		DateValidator.validateNotInFuture(createdAt, "Date and Time");
		return paymentRepository.findByRelatedSales_CreatedAt(createdAt).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_TotalPriceGreaterThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return paymentRepository.findByRelatedSales_TotalPriceGreaterThan(totalPrice).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_TotalPriceLessThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return paymentRepository.findByRelatedSales_TotalPriceLessThan(totalPrice).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_TotalPrice(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return paymentRepository.findByRelatedSales_TotalPrice(totalPrice).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_SalesDescriptionContainingIgnoreCase(String salesDescription) {
		validateString(salesDescription);
		return paymentRepository.findByRelatedSales_SalesDescriptionContainingIgnoreCase(salesDescription).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_Buyer_Id(Long buyerId) {
		fetchBuyer(buyerId);
		return paymentRepository.findByRelatedSales_Buyer_Id(buyerId).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByBuyer_IdAndStatus(Long buyerId, PaymentStatus status) {
		validatePaymentStatus(status);
		return paymentRepository.findByBuyer_IdAndStatus(buyerId, status).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByPaymentDateBetweenAndMethod(LocalDateTime start, LocalDateTime end,
			PaymentMethod method) {
		DateValidator.validateRange(start, end);
		validatePaymentMethod(method);
		return paymentRepository.findByPaymentDateBetweenAndMethod(start, end, method).stream()
				.map(PaymentResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PaymentResponse> findByRelatedSales_SalesDescriptionContainingIgnoreCaseAndBuyer_Id(String description,
			Long buyerId) {
		fetchBuyer(buyerId);
		validateString(description);
		return paymentRepository.findByRelatedSales_SalesDescriptionContainingIgnoreCaseAndBuyer_Id(description, buyerId).stream()
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
		fetchBuyer(request.buyerId());
		fetchSales(request.relatedSalesId());
		validateBigDecimal(request.amount());
		DateValidator.validateNotInFuture(request.paymentDate(), "Payment date");
		validatePaymentMethod(request.method());
		validatePaymentStatus(request.status());
		validateReferenceNumberIsUnique(request.referenceNumber());
		
	}

}
