package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.request.PaymentRequest;
import com.jovan.erp_v1.response.PaymentResponse;

public interface IPaymentService {

	PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByBuyer(Long buyerId);

    List<PaymentResponse> getPaymentsByStatus(PaymentStatus status);

    List<PaymentResponse> getPaymentsByMethod(PaymentMethod method);

    void deletePayment(Long id);

    PaymentResponse updatePayment(Long id, PaymentRequest request);
	
}
