package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.InvoiceTypeStatus;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.request.InvoiceRequest;
import com.jovan.erp_v1.response.InvoiceResponse;
import com.jovan.erp_v1.save_as.InvoiceSaveAsRequest;
import com.jovan.erp_v1.search_request.InvoiceSearchRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatByBuyerRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatByPaymentRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatBySalesOrderRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatBySalesRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountByBuyerStatDTO;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountByPaymentStatDTO;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountBySalesOrderStatDTO;
import com.jovan.erp_v1.statistics.invoice.InvoiceTotalAmountBySalesStatDTO;

public interface IInvoiceService {

	InvoiceResponse createInvoice(InvoiceRequest request);
	InvoiceResponse updateInvoice(Long id, InvoiceRequest request);
    InvoiceResponse getInvoiceById(Long id);
    List<InvoiceResponse> getAllInvoices();
    void deleteInvoice(Long id);
    InvoiceResponse findByInvoiceNumber(String invocieNumber);
	List<InvoiceResponse> findByStatus(InvoiceStatus status);
	List<InvoiceResponse> findByIssueDateBetween(LocalDateTime start, LocalDateTime end);
	List<InvoiceResponse> findByDueDateBefore(LocalDateTime date);
	List<InvoiceResponse> findByPaymentStatus( PaymentStatus status);
	boolean existsByInvoiceNumber(String invoiceNumber);
	List<InvoiceResponse> findByBuyerIdOrderByIssueDateDesc( Long buyerId);
	List<InvoiceResponse> findByBuyerIdAndStatus( Long buyerId,  InvoiceStatus status);
	List<InvoiceResponse> findByInvoiceNumberContainingIgnoreCase(String fragment);
	List<InvoiceResponse> findInvoicesByBuyerSortedByIssueDate(Long buyerId);
	List<InvoiceResponse> findByTotalAmount(BigDecimal totalAmount);
	//dodate nove metode
	List<InvoiceResponse> findByBuyerId(Long buyerId);
	List<InvoiceResponse> findByBuyerCompanyNameContainingIgnoreCase(String companyName);
	List<InvoiceResponse> findByBuyerPib(String pib);
	List<InvoiceResponse> findByBuyerEmailContainingIgnoreCase(String email);
	List<InvoiceResponse> findByBuyerPhoneNumber(String phoneNumber);
	List<InvoiceResponse> findByNoteContainingIgnoreCase(String note);
	List<InvoiceResponse> findByRelatedSales_Id(Long relatedSalesId);
	List<InvoiceResponse> findByRelatedSales_TotalPrice(BigDecimal totalPrice);
	List<InvoiceResponse> findByRelatedSales_TotalPriceGreaterThan(BigDecimal totalPrice);
	List<InvoiceResponse> findByRelatedSales_TotalPriceLessThan(BigDecimal totalPrice);
	List<InvoiceResponse> findByPayment_Id(Long paymentId);
	List<InvoiceResponse> findByPayment_Amount(BigDecimal amount);
	List<InvoiceResponse> findByPayment_PaymentDate(LocalDateTime paymentDate);
	List<InvoiceResponse> findByPayment_PaymentDateBetween(LocalDateTime paymentDateStart, LocalDateTime paymentDateEnd);
	List<InvoiceResponse> findByPayment_Method(PaymentMethod method);
	List<InvoiceResponse> findByPayment_ReferenceNumberContainingIgnoreCase(String referenceNumber);
	List<InvoiceResponse> findBySalesOrder_Id(Long salesOrderId);
	boolean existsBySalesOrder_OrderNumber(String orderNumber);
	List<InvoiceResponse> findBySalesOrder_OrderDate(LocalDateTime orderDate);
	List<InvoiceResponse> findBySalesOrder_OrderDateBetween(LocalDateTime orderDateStart, LocalDateTime orderDateEnd);
	List<InvoiceResponse> findBySalesOrder_TotalAmount(BigDecimal totalAmount);
	List<InvoiceResponse> findBySalesOrder_TotalAmountGreaterThan(BigDecimal totalAmount);
	List<InvoiceResponse> findBySalesOrder_TotalAmountLessThan(BigDecimal totalAmount);
	List<InvoiceResponse> findBySalesOrder_Status(OrderStatus status);
	List<InvoiceResponse> findBySalesOrder_NoteContainingIgnoreCase(String note);
	List<InvoiceResponse> findByCreatedBy_Id(Long createdById);
	List<InvoiceResponse> findByCreatedBy_EmailContainingIgnoreCase(String createdByEmail);
	List<InvoiceResponse> findByCreatedBy_FirstNameContainingIgnoreCaseAndCreatedBy_LastNameContainingIgnoreCase(String createdByFirstName, String createdByLastName);
	
	//nove metode
	
	List<InvoiceTotalAmountByBuyerStatDTO> getInvoiceStatisticsByBuyer(InvoiceStatByBuyerRequest request);
	List<InvoiceTotalAmountBySalesStatDTO> getInvoiceStatisticsBySales(InvoiceStatBySalesRequest request);
	List<InvoiceTotalAmountByPaymentStatDTO> getInvoiceStatisticsByPayment(InvoiceStatByPaymentRequest request);
	List<InvoiceTotalAmountBySalesOrderStatDTO> getInvoiceStatisticsBySalesOrder(InvoiceStatBySalesOrderRequest request);
	InvoiceResponse trackInvoiceByBuyer( Long buyerId);
	InvoiceResponse trackInvoiceBySales( Long salesId);
	InvoiceResponse trackInvoiceByPayment( Long paymentId);
	InvoiceResponse trackInvoiceBySalesOrder( Long salesOrderId);
	InvoiceResponse trackInvoice(Long id);
	InvoiceResponse confirmInvoice(Long id);
	InvoiceResponse cancelInvoice(Long id);
	InvoiceResponse closeInvoice(Long id);
	InvoiceResponse changeStatus(Long id, InvoiceTypeStatus status);
	List<InvoiceResponse> findByReports(Long id, String note);
	InvoiceResponse saveInvoice(InvoiceRequest request);
	InvoiceResponse saveAs(InvoiceSaveAsRequest request);
	List<InvoiceResponse> saveAll(List<InvoiceRequest> requests);
	List<InvoiceResponse> generalSearch(InvoiceSearchRequest request);
}
