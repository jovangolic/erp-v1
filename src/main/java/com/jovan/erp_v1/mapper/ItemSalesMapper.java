package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class ItemSalesMapper extends AbstractMapper<ItemSalesRequest> {

	public ItemSales toEntity(ItemSalesRequest request, Goods goods,Sales sales,Procurement procurement,SalesOrder so) {
		Objects.requireNonNull(request, "ItemSalesRequest must not be null");
		Objects.requireNonNull(goods, "Goods must not be null");
		Objects.requireNonNull(sales, "Sales must not be null");
		Objects.requireNonNull(procurement, "Procurement must not be null");
		Objects.requireNonNull(so, "SalesOrder must not be null");
		validateIdForCreate(request, ItemSalesRequest::id);
		ItemSales item = new ItemSales();
		item.setId(request.id());
		item.setGoods(goods);
		item.setSales(sales);
		item.setProcurement(procurement);
		item.setSalesOrder(so);
		item.setQuantity(request.quantity());
		item.setUnitPrice(request.unitPrice());
		return item;
	}
	
	public ItemSales toCreateEntity(ItemSalesRequest request, Goods goods, Sales sales, Procurement procurement, SalesOrder salesOrder) {
		Objects.requireNonNull(request, "ItemSalesRequest must not be null");
		Objects.requireNonNull(goods, "Goods must not be null");
		Objects.requireNonNull(sales, "Sales must not be null");
		Objects.requireNonNull(procurement, "Procurement must not be null");
		Objects.requireNonNull(salesOrder, "SalesOrder must not be null");
		ItemSales items = new ItemSales();
		items.setGoods(goods);
		items.setSales(sales);
		items.setProcurement(procurement);
		items.setSalesOrder(salesOrder);
		items.setQuantity(request.quantity());
		items.setUnitPrice(request.unitPrice());
		return items;
	}
	
	public ItemSales toUpdateEntity(ItemSales item, ItemSalesRequest request, Goods goods, Sales sales, Procurement procurement, SalesOrder salesOrder) {
		Objects.requireNonNull(item, "ItemSales must not be null");
		Objects.requireNonNull(request, "ItemSalesRequest must not be null");
		Objects.requireNonNull(goods, "Goods must not be null");
		Objects.requireNonNull(sales, "Sales must not be null");
		Objects.requireNonNull(procurement, "Procurement must not be null");
		Objects.requireNonNull(salesOrder, "SalesOrder must not be null");
		validateIdForUpdate(request, ItemSalesRequest::id);
		return buildItemSalesFromRequest(item, request,goods,sales,procurement,salesOrder);
	}
	
	private ItemSales buildItemSalesFromRequest(ItemSales item, ItemSalesRequest request, Goods goods, Sales sales, Procurement procurement, SalesOrder salesOrder) {
		item.setGoods(goods);
		item.setSales(sales);
		item.setProcurement(procurement);
		item.setSalesOrder(salesOrder);
		item.setQuantity(request.quantity());
		item.setUnitPrice(request.unitPrice());
		return item;
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
}
