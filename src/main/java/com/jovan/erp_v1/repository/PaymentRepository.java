package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	 //nove metoda
	 List<Payment> findByAmount(BigDecimal amount);
	 List<Payment> findByAmountGreaterThan(BigDecimal amount);
	 List<Payment> findByAmountLessThan(BigDecimal amount);
	 List<Payment> findByPaymentDate(LocalDateTime paymentDate);
	 boolean existsByReferenceNumber(String referenceNumber);
	 List<Payment> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName);
	 List<Payment> findByBuyer_PibContainingIgnoreCase(String pib);
	 boolean existsByBuyer_Pib(String pib);
	 List<Payment> findByBuyer_AddressContainingIgnoreCase(String buyerAddress);
	 List<Payment> findByBuyer_EmailContainingIgnoreCase(String buyerEmail);
	 List<Payment> findByBuyer_PhoneNumber(String buyerPhoneNumber);
	 List<Payment> findByRelatedSales_Id(Long relatedSalesId);
	 List<Payment> findByRelatedSales_CreatedAt(LocalDateTime createdAt);
	 List<Payment> findByRelatedSales_TotalPriceGreaterThan(BigDecimal totalPrice);
	 List<Payment> findByRelatedSales_TotalPriceLessThan(BigDecimal totalPrice);
	 List<Payment> findByRelatedSales_TotalPrice(BigDecimal totalPrice);
	 List<Payment> findByRelatedSales_SalesDescriptionContainingIgnoreCase(String salesDescription);
	 @Query("SELECT p FROM Payment p WHERE p.relatedSales.buyer.id = :buyerId")
	 List<Payment> findByRelatedSales_Buyer_Id(@Param("buyerId") Long buyerId);
	 List<Payment> findByBuyer_IdAndStatus(Long buyerId, PaymentStatus status);
	 List<Payment> findByPaymentDateBetweenAndMethod(LocalDateTime start, LocalDateTime end, PaymentMethod method);
	 List<Payment> findByRelatedSales_SalesDescriptionContainingIgnoreCaseAndBuyer_Id(String description, Long buyerId);
	 Long countByBuyer_Id(Long buyerId);
	 boolean existsByRelatedSales_Id(Long salesId);
}	
