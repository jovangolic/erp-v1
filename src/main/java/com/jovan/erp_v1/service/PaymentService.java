package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentMapper paymentMapper;
	private final BuyerRepository buyerRepository;
	private final SalesRepository salesRepository;
	
	
	
	@Transactional
	@Override
    public PaymentResponse createPayment(PaymentRequest request) {
        Buyer buyer = buyerRepository.findById(request.buyerId())
            .orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + request.buyerId()));
        Sales sales = salesRepository.findById(request.relatedSalesId())
            .orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + request.relatedSalesId()));

        Payment payment = paymentMapper.toEntity(request);
        payment.setBuyer(buyer);
        payment.setRelatedSales(sales);
        
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(savedPayment);
    }
	
	
	@Override
	public PaymentResponse getPaymentById(Long id) {
		Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: "+id));
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
		Buyer buyer = buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: "+buyerId));
		return paymentRepository.findByBuyerId(buyer.getId()).stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
		return paymentRepository.findByStatus(status).stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<PaymentResponse> getPaymentsByMethod(PaymentMethod method) {
		return paymentRepository.findByMethod(method).stream()
				.map(paymentMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Transactional
	@Override
	public void deletePayment(Long id) {
		if(!paymentRepository.existsById(id)) {
			throw new PaymentNotFoundException("Payment not found wuth id: "+id);
		}
		paymentRepository.deleteById(id);
	}
	
	@Transactional
	@Override
	public PaymentResponse updatePayment(Long id, PaymentRequest request) {
		Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: "+id));
		payment.setAmount(request.amount());
		payment.setPaymentDate(request.paymentDate());
		payment.setMethod(request.method());
		payment.setStatus(request.status());
		payment.setReferenceNumber(request.referenceNumber());
		Buyer buyer = buyerRepository.findById(request.buyerId())
	            .orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + request.buyerId()));
	    Sales sales = salesRepository.findById(request.relatedSalesId())
	            .orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + request.relatedSalesId()));
	    payment.setBuyer(buyer);
	    payment.setRelatedSales(sales);
	    Payment updated = paymentRepository.save(payment);
		return paymentMapper.toResponse(updated);
	}
	
	
}
