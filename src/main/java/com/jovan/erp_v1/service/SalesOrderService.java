package com.jovan.erp_v1.service;


import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.InvoiceNotFoundException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.mapper.SalesOrderMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.InvoiceRepository;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.request.SalesOrderRequest;
import com.jovan.erp_v1.response.SalesOrderResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesOrderService implements ISalesOrder {

	private final SalesOrderRepository salesOrderRepository;
	private final SalesOrderMapper salesOrderMapper;
	private final BuyerRepository buyerRepository;
	private final InvoiceRepository invoiceRepository;
	private final GoodsRepository goodsRepository;
	private final SalesRepository salesRepository;
	private final ProcurementRepository procurementRepository;
	
	@Transactional
	@Override
	public SalesOrderResponse createOrder(SalesOrderRequest request) {
		SalesOrder order = salesOrderMapper.toEntity(request);
		String orderNumber = generateOrderNumber();
		order.setOrderNumber(orderNumber);
		order.setOrderDate(request.orderDate());
		order.setStatus(request.status());
		SalesOrder saved = salesOrderRepository.save(order);
		return salesOrderMapper.toResponse(saved);
	}
	
	@Transactional
	@Override
	public SalesOrderResponse updateOrder(Long id, SalesOrderRequest request) {
		SalesOrder salesOrder = salesOrderRepository.findById(id)
		        .orElseThrow(() -> new SalesOrderNotFoundException("Sales order not found with id: " + id));
		    salesOrder.setOrderDate(request.orderDate());
		    salesOrder.setTotalAmount(request.totalAmount());
		    salesOrder.setStatus(request.status());
		    salesOrder.setNote(request.note());
		    // Povezivanje Buyer-a
		    Buyer buyer = buyerRepository.findById(request.buyerId())
		        .orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + request.buyerId()));
		    salesOrder.setBuyer(buyer);
		    // Povezivanje Invoice-a
		    Invoice invoice = invoiceRepository.findById(request.invoiceId())
		        .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + request.invoiceId()));
		    salesOrder.setInvoice(invoice);
		    List<ItemSales> itemSalesList = mapToItemSalesList(request.items(), salesOrder);
		    salesOrder.setItems(itemSalesList);
		    
		    SalesOrder saved = salesOrderRepository.save(salesOrder);
		    return salesOrderMapper.toResponse(saved);
	}
	
	@Override
    public String generateOrderNumber() {
        // Npr: SO-2025-000123
        long count = salesOrderRepository.count();
        return String.format("SO-%d-%06d", Year.now().getValue(), count + 1);
    }

	@Override
	public List<SalesOrderResponse> getAllOrders() {
		return salesOrderRepository.findAll().stream()
				.map(SalesOrderResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public SalesOrderResponse getOrderById(Long id) {
		SalesOrder salesId = salesOrderRepository.findById(id).orElseThrow(() ->  new SalesNotFoundException("Sales not found with id: " + id));
		return new SalesOrderResponse(salesId);
	}
	
	@Transactional
	@Override
	public void deleteSales(Long id) {
		if(!salesRepository.existsById(id)) {
			throw new SalesOrderNotFoundException("SalesOrder not found with id: " + id);
		}
		salesOrderRepository.deleteById(id);
	}
	
	private List<ItemSales> mapToItemSalesList(List<ItemSalesRequest> items, SalesOrder salesOrder) {
	    return items.stream()
	        .map(item -> {
	            ItemSales itemSales = new ItemSales();
	            itemSales.setQuantity(item.quantity());
	            itemSales.setUnitPrice(item.unitPrice());

	            Goods goods = goodsRepository.findById(item.goodsId())
	                .orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + item.goodsId()));
	            itemSales.setGoods(goods);

	            Procurement procurement = procurementRepository.findById(item.procurementId())
	                .orElseThrow(() -> new ProcurementNotFoundException("Procurement not found with id: " + item.procurementId()));
	            itemSales.setProcurement(procurement);
	            Sales sales = salesRepository.findById(item.salesId()).orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + item.salesId()));
	            itemSales.setSales(sales); // ili .setSalesOrder(salesOrder) ako si to ime koristio
	            return itemSales;
	        })
	        .collect(Collectors.toList());
	}
	
}