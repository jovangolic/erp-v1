package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.request.InvoiceRequest;
import com.jovan.erp_v1.response.InvoiceResponse;

public interface IInvoiceService {

	InvoiceResponse createInvoice(InvoiceRequest request);
	InvoiceResponse updateInvoice(Long id, InvoiceRequest request);
    InvoiceResponse getInvoiceById(Long id);
    List<InvoiceResponse> getAllInvoices();
    void deleteInvoice(Long id);
    List<InvoiceResponse> getByInvoiceStatus(InvoiceStatus status);
    List<InvoiceResponse> getByBuyerAndStatus(Buyer buyer, InvoiceStatus status);
    List<InvoiceResponse> getByTotalAmount(BigDecimal totalAmount);
    List<InvoiceResponse> getByBuyerId(Long buyerId);
    List<InvoiceResponse> getBySalesId(Long salesId);
    List<InvoiceResponse> getByPaymentId(Long paymentId);
    List<InvoiceResponse> getByIssueDateBetween(LocalDateTime start, LocalDateTime end);
    List<InvoiceResponse> getByDueDateBefore(LocalDateTime date);
    List<InvoiceResponse> searchByInvoiceNumberFragment(String fragment);
    boolean existsByInvoiceNumber(String invoiceNumber);
    List<InvoiceResponse> getInvoicesByBuyerSortedByIssueDate(Long buyerId);
}
