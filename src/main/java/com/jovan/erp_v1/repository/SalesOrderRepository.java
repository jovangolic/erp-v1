package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.SalesOrder;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

  List<SalesOrder> findByBuyer(Buyer buyer);

  Optional<SalesOrder> findByInvoice(Invoice invoice);

  List<SalesOrder> findByInvoice_Id(Long invoiceId);

  List<SalesOrder> findByStatus(OrderStatus status);

  List<SalesOrder> findByBuyer_Id(Long buyerId);

  @Query("SELECT s FROM SalesOrder s WHERE s.orderDate BETWEEN :start AND :end")
  List<SalesOrder> findByOrderDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query("SELECT COUNT(s) FROM SalesOrder s WHERE YEAR(s.orderDate) = :year")
  int countByOrderDateYear(@Param("year") int year);

  Optional<SalesOrder> findById(Long id);

  Optional<SalesOrder> findByOrderNumber(String orderNumber);

  Integer countByStatus(OrderStatus status);

  @Query("SELECT s.status, COUNT(s) FROM SalesOrder s GROUP BY s.status")
  List<Object[]> countOrdersByStatus();

  // nove metode
  boolean existsByOrderNumber(String orderNumber);

  List<SalesOrder> findByBuyer_CompanyNameContainingIgnoreCase(String companyName);

  List<SalesOrder> findByBuyer_PibContainingIgnoreCase(String pib);

  List<SalesOrder> findByBuyer_Address(String address);

  List<SalesOrder> findByBuyer_ContactPerson(String contactPerson);

  List<SalesOrder> findByBuyer_EmailContainingIgnoreCase(String email);

  List<SalesOrder> findByBuyer_PhoneNumberContainingIgnoreCase(String phoneNumber);

  boolean existsByBuyer_Pib(String pib);

  boolean existsByInvoice_InvoiceNumber(String invoiceNumber);

  List<SalesOrder> findByInvoice_InvoiceNumberContainingIgnoreCase(String invoiceNumber);

  List<SalesOrder> findByInvoice_TotalAmount(BigDecimal totalAmount);

  List<SalesOrder> findByInvoice_TotalAmountGreaterThan(BigDecimal totalAmount);

  List<SalesOrder> findByInvoice_TotalAmountLessThan(BigDecimal totalAmount);

  // issueDate
  List<SalesOrder> findByInvoice_IssueDate(LocalDateTime issueDate);

  List<SalesOrder> findByInvoice_IssueDateAfter(LocalDateTime date);

  List<SalesOrder> findByInvoice_IssueDateBefore(LocalDateTime date);

  List<SalesOrder> findByInvoice_IssueDateBetween(LocalDateTime start, LocalDateTime end);

  // dueDate
  List<SalesOrder> findByInvoice_DueDate(LocalDateTime dueDate);

  List<SalesOrder> findByInvoice_DueDateAfter(LocalDateTime date);

  List<SalesOrder> findByInvoice_DueDateBefore(LocalDateTime date);

  List<SalesOrder> findByInvoice_DueDateBetween(LocalDateTime start, LocalDateTime end);

  List<SalesOrder> findByInvoice_NoteContainingIgnoreCase(String note);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.buyer.id = :buyerId")
  List<SalesOrder> findByInvoice_Buyer_Id(@Param("buyerId") Long buyerId);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.relatedSales.id = :relatedSalesId")
  List<SalesOrder> findByInvoice_RelatedSales_Id(Long relatedSalesId);

  // payment
  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.payment.id = :paymentId")
  List<SalesOrder> findByInvoice_Payment_Id(@Param("paymentId") Long paymentId);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.payment.amount = :amount")
  List<SalesOrder> findByInvoice_Payment_Amount(@Param("amount") BigDecimal amount);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.payment.amount >= :amount")
  List<SalesOrder> findByInvoice_Payment_AmountGreaterThan(@Param("amount") BigDecimal amount);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.payment.amount <= :amount")
  List<SalesOrder> findByInvoice_Payment_AmountLessThan(@Param("amount") BigDecimal amount);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.payment.method = :method")
  List<SalesOrder> findByInvoice_Payment_Method(@Param("method") PaymentMethod method);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.payment.status = :status")
  List<SalesOrder> findByInvoice_Payment_Status(@Param("status") PaymentStatus status);

  @Query("SELECT so FROM SalesOrder so WHERE LOWER(so.invoice.payment.referenceNumber) LIKE LOWER(CONCAT('%', :referenceNumber, '%'))")
  List<SalesOrder> findByInvoice_Payment_ReferenceNumberLikeIgnoreCase(
      @Param("referenceNumber") String referenceNumber);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.payment.paymentDate = :paymentDate")
  List<SalesOrder> findByInvoice_Payment_PaymentDate(@Param("paymentDate") LocalDateTime paymentDate);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.payment.paymentDate BETWEEN :startDate AND :endDate")
  List<SalesOrder> findByInvoice_Payment_PaymentDateBetween(@Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

  boolean existsByInvoice_Payment_ReferenceNumberLikeIgnoreCase(String referenceNumber);

  // user
  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.createdBy.id = :userId")
  List<SalesOrder> findByInvoice_CreatedBy_Id(@Param("userId") Long userId);

  @Query("SELECT so FROM SalesOrder so WHERE LOWER(so.invoice.createdBy.email) LIKE LOWER(CONCAT('%', :email, '%'))")
  List<SalesOrder> findByInvoice_CreatedBy_EmailLikeIgnoreCase(@Param("email") String email);

  @Query("SELECT so FROM SalesOrder so WHERE so.invoice.createdBy.address = :address")
  List<SalesOrder> findByInvoice_CreatedBy_Address(@Param("address") String address);

  @Query("SELECT so FROM SalesOrder so WHERE LOWER(so.invoice.createdBy.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
  List<SalesOrder> findByInvoice_CreatedBy_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);

  @Query("SELECT so FROM SalesOrder so WHERE LOWER(so.invoice.createdBy.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(so.invoice.createdBy.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
  List<SalesOrder> findByInvoice_CreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(
      @Param("firstName") String firstName, @Param("lastName") String lastName);
}
