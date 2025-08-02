package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.request.SalesOrderRequest;
import com.jovan.erp_v1.response.SalesOrderResponse;

public interface ISalesOrder {

    SalesOrderResponse createOrder(SalesOrderRequest request);
    SalesOrderResponse updateOrder(Long id, SalesOrderRequest request);
    String generateOrderNumber();
    List<SalesOrderResponse> getAllOrders();
    SalesOrderResponse getOrderById(Long id);
    void deleteSales(Long id);

    // nove metode
    List<SalesOrderResponse> findByBuyer_Id(Long buyerId);
    List<SalesOrderResponse> findByBuyer_CompanyNameContainingIgnoreCase(String companyName);
    List<SalesOrderResponse> findByBuyer_PibContainingIgnoreCase(String pib);
    List<SalesOrderResponse> findByBuyer_Address(String address);
    List<SalesOrderResponse> findByBuyer_ContactPerson(String contactPerson);
    List<SalesOrderResponse> findByBuyer_EmailContainingIgnoreCase(String email);
    List<SalesOrderResponse> findByBuyer_PhoneNumberContainingIgnoreCase(String phoneNumber);
    List<SalesOrderResponse> findByInvoice_InvoiceNumberContainingIgnoreCase(String invoiceNumber);
    List<SalesOrderResponse> findByInvoice_TotalAmount(BigDecimal totalAmount);
    List<SalesOrderResponse> findByInvoice_TotalAmountGreaterThan(BigDecimal totalAmount);
    List<SalesOrderResponse> findByInvoice_TotalAmountLessThan(BigDecimal totalAmount);
    List<SalesOrderResponse> findByInvoice_TotalAmountBetween(BigDecimal min, BigDecimal max);
    // issueDate
    List<SalesOrderResponse> findByInvoice_IssueDate(LocalDateTime issueDate);
    List<SalesOrderResponse> findByInvoice_IssueDateAfter(LocalDateTime date);
    List<SalesOrderResponse> findByInvoice_IssueDateBefore(LocalDateTime date);
    List<SalesOrderResponse> findByInvoice_IssueDateBetween(LocalDateTime start, LocalDateTime end);
    // dueDate
    List<SalesOrderResponse> findByInvoice_DueDate(LocalDateTime dueDate);
    List<SalesOrderResponse> findByInvoice_DueDateAfter(LocalDateTime date);
    List<SalesOrderResponse> findByInvoice_DueDateBefore(LocalDateTime date);
    List<SalesOrderResponse> findByInvoice_DueDateBetween(LocalDateTime start, LocalDateTime end);
    List<SalesOrderResponse> findByInvoice_NoteContainingIgnoreCase(String note);
    List<SalesOrderResponse> findByInvoice_Buyer_Id(Long buyerId);
    List<SalesOrderResponse> findByInvoice_RelatedSales_Id(Long relatedSalesId);
    // payment
    List<SalesOrderResponse> findByInvoice_Payment_Id(Long paymentId);
    List<SalesOrderResponse> findByInvoice_Payment_Amount(BigDecimal amount);
    List<SalesOrderResponse> findByInvoice_Payment_AmountGreaterThan(BigDecimal amount);
    List<SalesOrderResponse> findByInvoice_Payment_AmountLessThan(BigDecimal amount);
    List<SalesOrderResponse> findByInvoice_Payment_Method(PaymentMethod method);
    List<SalesOrderResponse> findByInvoice_Payment_Status(PaymentStatus status);
    List<SalesOrderResponse> findByInvoice_Payment_ReferenceNumberLikeIgnoreCase(String referenceNumber);
    List<SalesOrderResponse> findByInvoice_Payment_PaymentDate(LocalDateTime paymentDate);
    List<SalesOrderResponse> findByInvoice_Payment_PaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    // user
    List<SalesOrderResponse> findByInvoice_CreatedBy_Id(Long userId);
    List<SalesOrderResponse> findByInvoice_CreatedBy_EmailLikeIgnoreCase(String email);
    List<SalesOrderResponse> findByInvoice_CreatedBy_Address(String address);
    List<SalesOrderResponse> findByInvoice_CreatedBy_PhoneNumberLikeIgnoreCase(String phoneNumber);
    List<SalesOrderResponse> findByInvoice_CreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(String firstName,
            String lastName);
}
