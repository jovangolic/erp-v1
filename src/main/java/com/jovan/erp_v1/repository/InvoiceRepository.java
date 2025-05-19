package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	Optional<Invoice> findByInvoiceNumber(String invocieNumber);
	List<Invoice> findByStatus(InvoiceStatus status);
	List<Invoice> findByIssueDateBetween(LocalDateTime start, LocalDateTime end);
	List<Invoice> findByDueDateBefore(LocalDateTime date);
	List<Invoice> findByBuyer(Buyer buyer);
	@Query("SELECT i FROM Invoice i WHERE i.payment.status = :status")
	List<Invoice> findByPaymentStatus(@Param("status") PaymentStatus status);
	boolean existsByInvoiceNumber(String invoiceNumber);
	List<Invoice> findByBuyerOrderByIssueDateDesc(Buyer buyer);
	List<Invoice> findByBuyerAndStatus(Buyer buyer, InvoiceStatus status);
	List<Invoice> findByInvoiceNumberContainingIgnoreCase(String fragment);
	@Query("SELECT i FROM Invoice i WHERE i.buyer.id = :buyerId ORDER BY i.issueDate DESC")
	List<Invoice> findInvoicesByBuyerSortedByIssueDate(@Param("buyerId") Long buyerId);
	List<Invoice> findByTotalAmount(BigDecimal totalAmount);
}
