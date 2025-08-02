package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.request.SalesOrderRequest;
import com.jovan.erp_v1.response.SalesOrderResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalesOrderMapper extends AbstractMapper<SalesOrderRequest> {

    public SalesOrder toEntity(SalesOrderRequest request,Buyer buyer, Invoice invoice) {
    	Objects.requireNonNull(request, "SalesOrderRequest must not be null");
    	Objects.requireNonNull(buyer, "Buyer must not be null");
    	Objects.requireNonNull(invoice, "Invoice must not be null");
    	validateIdForCreate(request, SalesOrderRequest::id);
        SalesOrder order = new SalesOrder();
        order.setId(request.id());
        order.setOrderNumber(request.orderNumber());
        order.setTotalAmount(request.totalAmount());
        order.setNote(request.note());
        order.setStatus(request.status());
        order.setBuyer(buyer);
        order.setInvoice(invoice);
        // Items
        order.setItems(new ArrayList<>());
        return order;
    }
    
    public SalesOrder toEntityUpdate(SalesOrder so,SalesOrderRequest request,Buyer buyer, Invoice invoice) {
    	Objects.requireNonNull(so, "SalesOrder must not be null");
    	Objects.requireNonNull(request, "SalesOrderRequest must not be null");
    	Objects.requireNonNull(buyer, "Buyer must not be null");
    	Objects.requireNonNull(invoice, "Invoice must not be null");
    	validateIdForUpdate(request, SalesOrderRequest::id);
    	so.setOrderNumber(request.orderNumber());
        so.setTotalAmount(request.totalAmount());
        so.setNote(request.note());
        so.setStatus(request.status());
        so.setBuyer(buyer);
        so.setInvoice(invoice);
        so.setItems(new ArrayList<>());
    	return so;
    }

    public SalesOrderResponse toResponse(SalesOrder order) {
    	Objects.requireNonNull(order, "SalesOrder");
        return new SalesOrderResponse(order); 
    }
    
    public List<SalesOrderResponse> toResponseList(List<SalesOrder> orders){
    	if(orders == null || orders.isEmpty()) {
    		return Collections.emptyList();
    	}
    	return orders.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
