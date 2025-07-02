package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.response.InventoryItemsResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryItemsMapper {

	private final ProductRepository productRepository;
	
	public InventoryItems toEntity(InventoryItemsRequest request, Inventory inventory) {
	    Product product = productRepository.findById(request.productId())
	        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + request.productId()));

	    InventoryItems items = new InventoryItems();
	    items.setInventory(inventory); // koristi već postojeći objekat
	    items.setProduct(product);
	    items.setQuantity(request.quantity());
	    items.setItemCondition(request.condition());
	    return items;
	}
	
	public InventoryItemsResponse toResponse(InventoryItems items) {
		InventoryItemsResponse response = new InventoryItemsResponse();
		if(items.getInventory() != null) {
			response.setInventoryId(items.getInventory().getId());
		}
		if(items.getProduct() != null) {
			response.setProductId(items.getProduct().getId());
			response.setProductName(items.getProduct().getName());
		}
		response.setQuantity(items.getQuantity());
		response.setItemCondition(items.getItemCondition());
		response.setDifference(items.getDifference());
		if (items.getProduct() != null) {
		    response.setUnitMeasure(items.getProduct().getUnitMeasure());
		}
		return response;
	}
	
	public List<InventoryItemsResponse> toResponseList(List<InventoryItems> items){
		if(items == null || items.isEmpty()) {
			return Collections.emptyList();
		}
		return items.stream().map(this::toResponse).collect(Collectors.toList());
	}
	
}
