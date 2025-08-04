package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.InventoryRequest;
import com.jovan.erp_v1.response.InventoryResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryMapper extends AbstractMapper<InventoryRequest> {

	private final InventoryItemsMapper inventoryItemsMapper;

	public Inventory toEntity(InventoryRequest request, User employee, User foreman, Map<Long, Product> productsById) {
		Objects.requireNonNull(request, "InventoryRequest must not be null");
		Objects.requireNonNull(employee, "Employee must not be null");
		Objects.requireNonNull(foreman, "Foreman must not be null");
		validateIdForCreate(request, InventoryRequest::id);
		Inventory inventory = new Inventory();
		inventory.setId(request.id());
		inventory.setStorageEmployee(employee);
		inventory.setStorageForeman(foreman);
		inventory.setAligned(request.aligned());
		inventory.setStatus(request.status());
		if (request.inventoryItems() != null) {
			List<InventoryItems> items = request.inventoryItems().stream()
					.map(itemReq -> {
						Product product = productsById.get(itemReq.productId());
		                if (product == null) {
		                    throw new NoDataFoundException("Product with ID " + itemReq.productId() + " not found in map");
		                }
						return inventoryItemsMapper.toEntity(itemReq, inventory, product);
					})
					.collect(Collectors.toList());
			items.forEach(i -> i.setInventory(inventory));
			inventory.setInventoryItems(items);
		}
		return inventory;
	}

	public InventoryResponse toResponse(Inventory inventory) {
		Objects.requireNonNull(inventory, "Inventory must not be null");
		return new InventoryResponse(inventory);
	}

	public List<InventoryResponse> toResponseList(List<Inventory> inventories) {
		if(inventories == null || inventories.isEmpty()) {
			return Collections.emptyList();
		}
		return inventories.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
}
