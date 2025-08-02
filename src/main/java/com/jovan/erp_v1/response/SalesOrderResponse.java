package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.model.SalesOrder;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalesOrderResponse {

	
	private Long id;
    private String orderNumber;
    private BuyerResponse buyerResponse;
    private List<ItemSalesForSalesOrderResponse> itemSales;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private InvoiceForSalesOrderResponse invoiceForSalesOrderResponse;
    private OrderStatus status;
    private String note;
    
    public SalesOrderResponse(SalesOrder order) {
    	this.id = order.getId();
    	this.orderNumber = order.getOrderNumber();
    	this.buyerResponse = order.getBuyer() != null ? new BuyerResponse(order.getBuyer()) : null;
    	this.itemSales = order.getItems().stream()
    			.map(ItemSalesForSalesOrderResponse::new)
    			.collect(Collectors.toList());
    	this.orderDate = order.getOrderDate();
    	this.totalAmount = order.getTotalAmount();
    	this.invoiceForSalesOrderResponse = order.getInvoice() != null ? new InvoiceForSalesOrderResponse(order.getInvoice()) : null;
    	this.status = order.getStatus();
    }
    
}
