package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemSalesMapper extends AbstractMapper<ItemSalesRequest> {

	
	private final SalesRepository salesRepository;
	private final ProcurementRepository procurementRepository;
	private final GoodsRepository goodsRepository;
	private final SalesOrderRepository salesOrderRepository;
	
	public ItemSales toEntity(ItemSalesRequest request) {
		Objects.requireNonNull(request, "ItemSalesRequest must not be null");
		validateIdForCreate(request, ItemSalesRequest::id);
		return buildItemSalesFromRequest(new ItemSales(), request);
	}
	
	public ItemSales toCreateEntity(ItemSalesRequest request, Goods goods, Sales sales, Procurement procurement, SalesOrder salesOrder) {
		Objects.requireNonNull(request, "ItemSalesRequest must not be null");
		ItemSales items = new ItemSales();
		items.setGoods(goods);
		items.setSales(sales);
		items.setProcurement(procurement);
		items.setSalesOrder(salesOrder);
		items.setQuantity(request.quantity());
		items.setUnitPrice(request.unitPrice());
		return items;
	}
	
	public ItemSales toUpdateEntity(ItemSales sales, ItemSalesRequest request) {
		Objects.requireNonNull(request, "ItemSalesRequest must not be null");
		Objects.requireNonNull(sales, "ItemSales must not be null");
		validateIdForUpdate(request, ItemSalesRequest::id);
		return buildItemSalesFromRequest(sales, request);
	}
	
	private ItemSales buildItemSalesFromRequest(ItemSales sales, ItemSalesRequest request) {
		sales.setGoods(fetchGoods(request.goodsId()));
		sales.setSales(fetchSales(request.salesId()));
		sales.setProcurement(fetchProcurement(request.procurementId()));
		sales.setSalesOrder(fetchSalesOrder(request.salesOrderId()));
		sales.setQuantity(request.quantity());
		sales.setUnitPrice(request.unitPrice());
		return sales;
	}
	
	public ItemSalesResponse toResponse(ItemSales sales) {
		Objects.requireNonNull(sales, "ItemSales must not be null");
		return new ItemSalesResponse(sales);
	}
	
	public List<ItemSalesResponse> toResponseList(List<ItemSales> sales){
		if(sales == null || sales.isEmpty()) {
			return Collections.emptyList();
		}
		return sales.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
	private Sales fetchSales(Long salesId) {
		if(salesId == null) {
			throw new SalesNotFoundException("Sales ID must not me null");
		}
		return salesRepository.findById(salesId).orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + salesId));
	}
	
	private Procurement fetchProcurement(Long procurementId) {
		if(procurementId == null) {
			throw new ProcurementNotFoundException("Procurement ID must not be null");
		}
		return procurementRepository.findById(procurementId).orElseThrow(() -> new ProcurementNotFoundException("Procurement not found wwith id: " + procurementId));
	}
	
	private Goods fetchGoods(Long goodsId) {
		if(goodsId == null ) {
			throw new GoodsNotFoundException("Goods ID must not be null");
		}
		return goodsRepository.findById(goodsId).orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + goodsId));
	}
	
	private SalesOrder fetchSalesOrder(Long salesOrderId) {
		if(salesOrderId == null) {
			throw new SalesNotFoundException("SalesOrder ID must not be null");
		}
		return salesOrderRepository.findById(salesOrderId).orElseThrow(() -> new SalesOrderNotFoundException("Sales-Order not found with id: " + salesOrderId));
	}
}
