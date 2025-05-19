package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	 List<Payment> findByBuyerId(Long buyerId);

	 List<Payment> findByStatus(PaymentStatus status);

	 List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);

	 Optional<Payment> findByReferenceNumber(String referenceNumber);
	 
	 List<Payment> findByMethod(PaymentMethod method);
}
