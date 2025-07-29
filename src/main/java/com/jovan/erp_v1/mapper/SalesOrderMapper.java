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
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.InvoiceRepository;
import com.jovan.erp_v1.request.SalesOrderRequest;
import com.jovan.erp_v1.response.SalesOrderResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalesOrderMapper {

	
	private final BuyerRepository buyerRepository;
    private final InvoiceRepository invoiceRepository;
    private final ItemSalesMapper itemSalesMapper;

    public SalesOrder toEntity(SalesOrderRequest request) {
        SalesOrder order = new SalesOrder();
        order.setId(request.id());
        //order.setOrderNumber(request.orderNumber());
        order.setOrderDate(request.orderDate());
        order.setTotalAmount(request.totalAmount());
        order.setNote(request.note());
        order.setStatus(request.status());
        // Buyer
        if (request.buyerId() != null) {
            Buyer buyer = buyerRepository.findById(request.buyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found with id: " + request.buyerId()));
            order.setBuyer(buyer);
        }
        // Invoice
        if (request.invoiceId() != null) {
            Invoice invoice = invoiceRepository.findById(request.invoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + request.invoiceId()));
            order.setInvoice(invoice);
        }
        // Items
        order.setItems(new ArrayList<>());
        return order;
    }

    public SalesOrderResponse toResponse(SalesOrder order) {
    	Objects.requireNonNull(order, "SalesOrder");
        return new SalesOrderResponse(order); // koristiš već postojeći konstruktor
    }
    
    public List<SalesOrderResponse> toResponseList(List<SalesOrder> orders){
    	if(orders == null || orders.isEmpty()) {
    		return Collections.emptyList();
    	}
    	return orders.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
