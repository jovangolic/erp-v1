package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.model.SalesOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalesOrderResponse {

	
	private Long id;
    private String orderNumber;
    private Long buyerId;
    private String buyerCompanyName;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String invoiceNumber;
    private OrderStatus status;
    
    public SalesOrderResponse(SalesOrder order) {
    	this.id = order.getId();
    	this.orderNumber = order.getOrderNumber();
    	this.buyerId = order.getBuyer().getId();
    	this.buyerCompanyName = order.getBuyer().getCompanyName();
    	this.orderDate = order.getOrderDate();
    	this.totalAmount = order.getTotalAmount();
    	this.invoiceNumber = order.getInvoice().getInvoiceNumber();
    	this.status = order.getStatus();
    }
    
}
