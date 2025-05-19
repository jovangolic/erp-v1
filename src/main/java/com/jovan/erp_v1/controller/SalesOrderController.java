package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.mapper.SalesOrderMapper;
import com.jovan.erp_v1.request.SalesOrderRequest;
import com.jovan.erp_v1.response.SalesOrderResponse;
import com.jovan.erp_v1.service.ISalesOrder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/salesOrders")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class SalesOrderController {

	
	private final ISalesOrder salesOrder;
	private final SalesOrderMapper salesOrderMapper;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-sales-order")
	public ResponseEntity<SalesOrderResponse> create(@Valid @RequestBody SalesOrderRequest request){
		SalesOrderResponse response = salesOrder.createOrder(request);
		return ResponseEntity.ok(response);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{id}")
	public ResponseEntity<SalesOrderResponse> update(@PathVariable Long id, @Valid @RequestBody SalesOrderRequest request){
		SalesOrderResponse response = salesOrder.updateOrder(id, request);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/generate-order-number")
    public ResponseEntity<String> generateOrderNumber() {
        String orderNumber = salesOrder.generateOrderNumber();
        return ResponseEntity.ok(orderNumber);
    }
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long id){
		salesOrder.deleteSales(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<SalesOrderResponse> getSalesOrderById(@PathVariable Long id){
		SalesOrderResponse response = salesOrder.getOrderById(id);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/get-all")
	public ResponseEntity<List<SalesOrderResponse>> getAllSalesOrders(){
		List<SalesOrderResponse> responses = salesOrder.getAllOrders();
		return ResponseEntity.ok(responses);
	}
}
