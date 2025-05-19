package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.mapper.PaymentMapper;
import com.jovan.erp_v1.request.PaymentRequest;
import com.jovan.erp_v1.response.PaymentResponse;
import com.jovan.erp_v1.service.IPaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class PaymentController {

	
	private final IPaymentService paymentService;
	private final PaymentMapper paymentMapper;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/create/new-payment")
	public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request){
		PaymentResponse response = paymentService.createPayment(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentRequest request){
		PaymentResponse response = paymentService.updatePayment(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deletePayment(@PathVariable Long id){
		paymentService.deletePayment(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/payment/{id}")
	public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id){
		PaymentResponse response = paymentService.getPaymentById(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<List<PaymentResponse>> getAllPayments(){
		List<PaymentResponse> responses = paymentService.getAllPayments();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/payment/buyer/{buyerId}")
	public ResponseEntity<List<PaymentResponse>> getPaymentsByBuyer(@PathVariable Long buyerId){
		List<PaymentResponse> response = paymentService.getPaymentsByBuyer(buyerId);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/payment/by-status")
	public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@RequestParam("status") PaymentStatus status){
		List<PaymentResponse> responses = paymentService.getPaymentsByStatus(status);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/payment/by-method")
	public ResponseEntity<List<PaymentResponse>> getPaymentsByMethod(@RequestParam("method") PaymentMethod method){
		List<PaymentResponse> responses = paymentService.getPaymentsByMethod(method);
		return ResponseEntity.ok(responses);
	}
	
}
