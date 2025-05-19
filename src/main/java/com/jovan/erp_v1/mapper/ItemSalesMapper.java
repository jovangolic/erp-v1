package com.jovan.erp_v1.mapper;

import java.util.List;
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

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemSalesMapper {

	
	private final SalesRepository salesRepository;
	private final ProcurementRepository procurementRepository;
	private final GoodsRepository goodsRepository;
	private final SalesOrderRepository salesOrderRepository;
	
	public ItemSales toEntity(ItemSalesRequest request) {
		ItemSales itemSales = new ItemSales();
		Sales sales = salesRepository.findById(request.salesId()).orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + request.salesId()));
		Procurement procurement = procurementRepository.findById(request.procurementId()).orElseThrow(() -> new ProcurementNotFoundException("Procurement not found wwith id: " + request.procurementId()));
		Goods goods = goodsRepository.findById(request.goodsId()).orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + request.goodsId()));
		SalesOrder salesOrder = salesOrderRepository.findById(request.salesOrderId()).orElseThrow(() -> new SalesOrderNotFoundException("Sales-Order not found with id: " + request.salesOrderId()));
		itemSales.setId(request.id());
		itemSales.setGoods(goods);
		itemSales.setSales(sales);
		itemSales.setProcurement(procurement);
		itemSales.setSalesOrder(salesOrder);
		itemSales.setQuantity(request.quantity());
		itemSales.setUnitPrice(request.unitPrice());
		return itemSales;
	}
	
	public ItemSalesResponse toResponse(ItemSales sales) {
		ItemSalesResponse response = new ItemSalesResponse();
		response.setId(sales.getId());
		response.setQuantity(sales.getQuantity());
		response.setUnitPrice(sales.getUnitPrice());
		if(sales.getSales() != null) {
			response.setTotalSalePrice(sales.getSales().getTotalPrice());
		}
		if(sales.getProcurement() != null) {
			response.setTotalProcurementCost(sales.getProcurement().getTotalCost());
		}
		if(sales.getGoods() != null) {
			response.setGoodsName(sales.getGoods().getName());
		}
		if (sales.getSalesOrder() != null) {
			response.setSalesOrderId(sales.getSalesOrder().getId());
			response.setOrderNumber(sales.getSalesOrder().getOrderNumber());
		}
		return response;
	}
	
	public List<ItemSalesResponse> toResponseList(List<ItemSales> sales){
		return sales.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
}
