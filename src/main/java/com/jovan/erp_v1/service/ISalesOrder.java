package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.SalesOrderRequest;
import com.jovan.erp_v1.response.SalesOrderResponse;

public interface ISalesOrder {
	
	SalesOrderResponse createOrder(SalesOrderRequest request);
	
	SalesOrderResponse updateOrder(Long id, SalesOrderRequest request);

	String generateOrderNumber();

	List<SalesOrderResponse> getAllOrders();

    SalesOrderResponse getOrderById(Long id);	
    
    void deleteSales(Long id);
}
