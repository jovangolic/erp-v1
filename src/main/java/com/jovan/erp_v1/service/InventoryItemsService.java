package com.jovan.erp_v1.service;

import java.math.BigDecimal;
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
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemsService implements IInventoryItemsService {

	private final InventoryItemsRepository inventoryItemsRepository;
	private final InventoryRepository inventoryRepository;
	private final ProductRepository productRepository;

	@Transactional
	@Override
	public InventoryItemsResponse create(InventoryItemsRequest request) {
		InventoryItems items = new InventoryItems();
		Inventory inventory = validateInventory(request.inventoryId());
		Product product = validateProduct(request.productId());
		validateBigDecimal(request.quantity());
		validateInteger(request.condition());
		items.setInventory(inventory);
		items.setProduct(product);
		items.setItemCondition(request.condition());
		items.setQuantity(request.quantity());
		items.setDifference(calculateDifference(request.quantity(), request.condition()));
		InventoryItems saved = inventoryItemsRepository.save(items);
		return new InventoryItemsResponse(saved);
	}

	@Transactional
	@Override
	public InventoryItemsResponse update(Long id, InventoryItemsRequest request) {
		InventoryItems items = inventoryItemsRepository.findById(id)
				.orElseThrow(() -> new InventoryItemsNotFoundException("Inventory-Items not found with id: " + id));
		Inventory inventory = validateInventory(request.inventoryId());
		Product product = validateProduct(request.productId());
		validateBigDecimal(request.quantity());
		validateInteger(request.condition());
		items.setInventory(inventory);
		items.setProduct(product);
		items.setItemCondition(request.condition());
		items.setQuantity(request.quantity());
		items.setDifference(calculateDifference(request.quantity(), request.condition()));
		InventoryItems updated = inventoryItemsRepository.save(items);
		return new InventoryItemsResponse(updated);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if (!inventoryItemsRepository.existsById(id)) {
			throw new InventoryItemsNotFoundException("Inventory-Items not found with id: " + id);
		}
		inventoryItemsRepository.deleteById(id);

	}

	@Override
	public List<InventoryItemsResponse> getByQuantity(BigDecimal quantity) {
		validateBigDecimal(quantity);
		return inventoryItemsRepository.findByQuantity(quantity).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByItemCondition(Integer itemCondition) {
		validateInteger(itemCondition);
		return inventoryItemsRepository.findByItemCondition(itemCondition).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByInventoryId(Long inventoryId) {
		Inventory inventory = inventoryRepository.findById(inventoryId)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found"));
		return inventoryItemsRepository.findByInventoryId(inventory.getId()).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByProductId(Long productId) {
		Product product = validateProduct(productId);
		return inventoryItemsRepository.findByProductId(product.getId()).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByProductName(String productName) {
		validateString(productName);
		return inventoryItemsRepository.findByProductName(productName).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public InventoryItemsResponse findById(Long id) {
		InventoryItems items = inventoryItemsRepository.findById(id)
				.orElseThrow(() -> new InventoryItemsNotFoundException("Inventory-Items not found with id: " + id));
		return new InventoryItemsResponse(items);
	}

	@Override
	public List<InventoryItemsResponse> findAllInventories() {
		return inventoryItemsRepository.findAll().stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findItemsWithDifference(BigDecimal threshold) {
		validateBigDecimal(threshold);
		return inventoryItemsRepository.findByDifferenceGreaterThan(threshold).stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	private BigDecimal calculateDifference(BigDecimal quantity, Integer condition) {
		if (quantity == null || condition == null) {
	        throw new IllegalArgumentException("Quantity and condition must not be null.");
	    }
		if(quantity.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Quantity must be at least 0");
		}
		return quantity.subtract(BigDecimal.valueOf(condition));
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException("Character must not be null");
		}
	}
	
	private Product validateProduct(Long productId) {
		if(productId == null) {
			throw new ProductNotFoundException("Product must not be null");
		}
		return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product ID "+productId +" not found"));
	}
	
	private void validateInteger(Integer num) {
		if(num == null || num < 0) {
			throw new IllegalArgumentException("Number must be positive.");
		}
	}
	
	private Inventory validateInventory(Long inventoryId) {
		if(inventoryId == null) {
			throw new InventoryItemsNotFoundException("Inventory must not be null");
		}
		return inventoryRepository.findById(inventoryId).orElseThrow(() -> new InventoryItemsNotFoundException("Inventory ID "+inventoryId +" not found"));
	}
}
