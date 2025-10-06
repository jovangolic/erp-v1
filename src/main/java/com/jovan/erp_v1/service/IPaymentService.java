package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.PaymentRequest;
import com.jovan.erp_v1.response.PaymentResponse;
import com.jovan.erp_v1.response.TransactionResponse;

public interface IPaymentService {

	PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByBuyer(Long buyerId);

    List<PaymentResponse> getPaymentsByStatus(PaymentStatus status);

    List<PaymentResponse> getPaymentsByMethod(PaymentMethod method);

    void deletePayment(Long id);

    PaymentResponse updatePayment(Long id, PaymentRequest request);
    //nove metode
  	 List<PaymentResponse> findByAmount(BigDecimal amount);
  	 List<PaymentResponse> findByAmountGreaterThan(BigDecimal amount);
  	 List<PaymentResponse> findByAmountLessThan(BigDecimal amount);
  	 List<PaymentResponse> findByPaymentDate(LocalDateTime paymentDate);
  	 List<PaymentResponse> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName);
  	 List<PaymentResponse> findByBuyer_PibContainingIgnoreCase(String pib);
  	 List<PaymentResponse> findByBuyer_AddressContainingIgnoreCase(String buyerAddress);
  	 List<PaymentResponse> findByBuyer_EmailContainingIgnoreCase(String buyerEmail);
  	 List<PaymentResponse> findByBuyer_PhoneNumber(String buyerPhoneNumber);
  	 List<PaymentResponse> findByRelatedSales_Id(Long relatedSalesId);
  	 List<PaymentResponse> findByRelatedSales_CreatedAt(LocalDateTime createdAt);
  	 List<PaymentResponse> findByRelatedSales_TotalPriceGreaterThan(BigDecimal totalPrice);
  	 List<PaymentResponse> findByRelatedSales_TotalPriceLessThan(BigDecimal totalPrice);
  	 List<PaymentResponse> findByRelatedSales_TotalPrice(BigDecimal totalPrice);
  	 List<PaymentResponse> findByRelatedSales_SalesDescriptionContainingIgnoreCase(String salesDescription);
  	 List<PaymentResponse> findByRelatedSales_Buyer_Id( Long buyerId);
  	 List<PaymentResponse> findByBuyer_IdAndStatus(Long buyerId, PaymentStatus status);
  	 List<PaymentResponse> findByPaymentDateBetweenAndMethod(LocalDateTime start, LocalDateTime end, PaymentMethod method);
  	 List<PaymentResponse> findByRelatedSales_SalesDescriptionContainingIgnoreCaseAndBuyer_Id(String description, Long buyerId);
  	 Long countByBuyer_Id(Long buyerId);
	
  	 //metoda za placanje master-card karticom
  	TransactionResponse processMasterCardPayment(String accountNumber, BigDecimal amount, String currency, String token, User user);
}
