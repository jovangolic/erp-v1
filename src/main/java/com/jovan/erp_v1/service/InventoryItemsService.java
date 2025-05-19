package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.InventoryItemsNotFoundException;
import com.jovan.erp_v1.exception.InventoryNotFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.repository.InventoryItemsRepository;
import com.jovan.erp_v1.repository.InventoryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.response.InventoryItemsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemsService implements IInventoryItemsService {

	private final InventoryItemsRepository inventoryItemsRepository;
	private final InventoryRepository inventoryRepository;
	private final ProductRepository productRepository;

	@Override
	public InventoryItemsResponse create(InventoryItemsRequest request) {
		InventoryItems items = new InventoryItems();
		Inventory inventory = inventoryRepository.findById(request.inventoryId()).orElseThrow(() -> new InventoryNotFoundException("Inventory not found "));
		items.setInventory(inventory);
		Product product = productRepository.findById(request.productId()).orElseThrow(() -> new ProductNotFoundException("Product not found "));
		items.setProduct(product);
		items.setItemCondition(request.condition());
		items.setQuantity(request.quantity());
		items.setDifference(calculateDifference(request.quantity(), request.condition()));
		InventoryItems saved = inventoryItemsRepository.save(items);
		return new InventoryItemsResponse(saved);
	}

	@Override
	public InventoryItemsResponse update(Long id, InventoryItemsRequest request) {
		InventoryItems items = inventoryItemsRepository.findById(id).orElseThrow(() -> new InventoryItemsNotFoundException("Inventory-Items not found with id: "+id));
		Inventory inventory = inventoryRepository.findById(request.inventoryId()).orElseThrow(() -> new InventoryNotFoundException("Inventory not found "));
		items.setInventory(inventory);
		Product product = productRepository.findById(request.productId()).orElseThrow(() -> new ProductNotFoundException("Product not found "));
		items.setProduct(product);
		items.setItemCondition(request.condition());
		items.setQuantity(request.quantity());
		items.setDifference(calculateDifference(request.quantity(), request.condition()));
		InventoryItems updated = inventoryItemsRepository.save(items);
		return new InventoryItemsResponse(updated);
	}

	@Override
	public void delete(Long id) {
		if(!inventoryItemsRepository.existsById(id)) {
			throw new InventoryItemsNotFoundException("Inventory-Items not found with id: "+id);
		}
		inventoryItemsRepository.deleteById(id);
		
	}

	@Override
	public List<InventoryItemsResponse> getByQuantity(Double quantity) {
		return inventoryItemsRepository.findByQuantity(quantity).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByItemCondition(Integer itemCondition) {
		return inventoryItemsRepository.findByItemCondition(itemCondition).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByInventoryId(Long inventoryId) {
		Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new InventoryNotFoundException("Inventory not found"));
		return inventoryItemsRepository.findByInventoryId(inventory.getId()).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByProductId(Long productId) {
		Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
		return  inventoryItemsRepository.findByProductId(product.getId()).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByProductName(String productName) {
		return inventoryItemsRepository.findByProductName(productName).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public InventoryItemsResponse findById(Long id) {
		InventoryItems items = inventoryItemsRepository.findById(id).orElseThrow(() -> new InventoryItemsNotFoundException("Inventory-Items not found with id: "+id));
		return new InventoryItemsResponse(items);
	}

	@Override
	public List<InventoryItemsResponse> findAllInventories() {
		return inventoryItemsRepository.findAll().stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findItemsWithDifference(Double threshold) {
		return inventoryItemsRepository.findByDifferenceGreaterThan(threshold).stream()
	            .map(InventoryItemsResponse::new)
	            .collect(Collectors.toList());
	}
	
	private double calculateDifference(Double quantity, Integer condition) {
	    return quantity - condition;
	}
}
