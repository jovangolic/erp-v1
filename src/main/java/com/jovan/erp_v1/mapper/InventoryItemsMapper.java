package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.response.InventoryItemsResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class InventoryItemsMapper extends AbstractMapper<InventoryItemsRequest> {

	public InventoryItems toEntity(InventoryItemsRequest request, Inventory inventory, Product product) {
		Objects.requireNonNull(request, "InventoryItemsRequest must not be null");
		Objects.requireNonNull(inventory, "Inventory must not be null");
		Objects.requireNonNull(product, "Product must not be null");
		validateIdForCreate(request, InventoryItemsRequest::id);
	    InventoryItems items = new InventoryItems();
	    items.setInventory(inventory); 
	    items.setProduct(product);
	    items.setQuantity(request.quantity());
	    items.setItemCondition(request.condition());
	    return items;
	}
	
	public InventoryItems toEntityUpdate(InventoryItems items, InventoryItemsRequest request, Inventory inventory, Product product) {
		Objects.requireNonNull(items, "InventoryItems must not be null");
		Objects.requireNonNull(request, "InventoryItemsRequest must not be null");
		Objects.requireNonNull(inventory, "Inventory must not be null");
		Objects.requireNonNull(product, "Product must not be null");
		validateIdForUpdate(request, InventoryItemsRequest::id);
		return buildInventoryItemsForRequest(items, request, inventory, product);
	}
	
	private InventoryItems buildInventoryItemsForRequest(InventoryItems items, InventoryItemsRequest request, Inventory inventory, Product product) {
		items.setInventory(inventory);
		items.setProduct(product);
		items.setQuantity(request.quantity());
		items.setItemCondition(request.condition());
		return items;
	}
	
	public InventoryItemsResponse toResponse(InventoryItems items) {
		Objects.requireNonNull(items, "InventoryItems must not be null");
		return new InventoryItemsResponse(items);
	}
	
	public List<InventoryItemsResponse> toResponseList(List<InventoryItems> items){
		if(items == null || items.isEmpty()) {
			return Collections.emptyList();
		}
		return items.stream().map(this::toResponse).collect(Collectors.toList());
	}
	
}
