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
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	Optional<Invoice> findByInvoiceNumber(String invocieNumber);
	List<Invoice> findByStatus(InvoiceStatus status);
	List<Invoice> findByIssueDateBetween(LocalDateTime start, LocalDateTime end);
	List<Invoice> findByDueDateBefore(LocalDateTime date);
	@Query("SELECT i FROM Invoice i WHERE i.payment.status = :status")
	List<Invoice> findByPaymentStatus(@Param("status") PaymentStatus status);
	boolean existsByInvoiceNumber(String invoiceNumber);
	@Query("SELECT i FROM Invoice i WHERE i.buyer.id = :buyerId ORDER BY i.issueDate DESC")
	List<Invoice> findByBuyerIdOrderByIssueDateDesc(@Param("buyerId") Long buyerId);
	@Query("SELECT i FROM Invoice i WHERE i.buyer.id = :buyerId AND i.status = :status")
	List<Invoice> findByBuyerIdAndStatus(@Param("buyerId") Long buyerId, @Param("status") InvoiceStatus status);
	List<Invoice> findByInvoiceNumberContainingIgnoreCase(String fragment);
	@Query("SELECT i FROM Invoice i WHERE i.buyer.id = :buyerId ORDER BY i.issueDate DESC")
	List<Invoice> findInvoicesByBuyerSortedByIssueDate(@Param("buyerId") Long buyerId);
	List<Invoice> findByTotalAmount(BigDecimal totalAmount);
	//nove metode
	List<Invoice> findByNoteContainingIgnoreCase(String note);
	List<Invoice> findByBuyerId(Long buyerId);
	List<Invoice> findByBuyerCompanyNameContainingIgnoreCase(String companyName);
	List<Invoice> findByBuyerPib(String pib);
	List<Invoice> findByBuyerEmailContainingIgnoreCase(String email);
	List<Invoice> findByBuyerPhoneNumber(String phoneNumber);
	List<Invoice> findByRelatedSales_Id(Long relatedSalesId);
	List<Invoice> findByRelatedSales_TotalPrice(BigDecimal totalPrice);
	List<Invoice> findByRelatedSales_TotalPriceGreaterThan(BigDecimal totalPrice);
	List<Invoice> findByRelatedSales_TotalPriceLessThan(BigDecimal totalPrice);
	List<Invoice> findByPayment_Id(Long paymentId);
	List<Invoice> findByPayment_Amount(BigDecimal amount);
	List<Invoice> findByPayment_PaymentDate(LocalDateTime paymentDate);
	List<Invoice> findByPayment_PaymentDateBetween(LocalDateTime paymentDateStart, LocalDateTime paymentDateEnd);
	List<Invoice> findByPayment_Method(PaymentMethod method);
	List<Invoice> findByPayment_ReferenceNumberContainingIgnoreCase(String referenceNumber);
	List<Invoice> findBySalesOrder_Id(Long salesOrderId);
	boolean existsBySalesOrder_orderNumber(String orderNumber);
	List<Invoice> findBySalesOrder_OrderDate(LocalDateTime orderDate);
	List<Invoice> findBySalesOrder_OrderDateBetween(LocalDateTime orderDateStart, LocalDateTime orderDateEnd);
	List<Invoice> findBySalesOrder_TotalAmount(BigDecimal totalAmount);
	List<Invoice> findBySalesOrder_TotalAmountGreaterThan(BigDecimal totalAmount);
	List<Invoice> findBySalesOrder_TotalAmountLessThan(BigDecimal totalAmount);
	List<Invoice> findBySalesOrder_Status(OrderStatus status);
	List<Invoice> findBySalesOrder_NoteContainingIgnoreCase(String note);
	List<Invoice> findByCreatedBy_Id(Long createdById);
	List<Invoice> findByCreatedBy_EmailContainingIgnoreCase(String createdByEmail);
	List<Invoice> findByCreatedBy_FirstNameContainingIgnoreCaseAndCreatedBy_LastNameContainingIgnoreCase(String createdByFirstName, String createdByLastName);
	boolean existsByBuyer_Pib(String pib);
	boolean existsSalesOrder_OrderNumber(String orderNumber);
}
