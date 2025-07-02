package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.StorageEmployeeNotFoundException;
import com.jovan.erp_v1.exception.StorageForemanNotFoundException;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.model.User;

import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.InventoryRequest;
import com.jovan.erp_v1.response.InventoryItemsResponse;
import com.jovan.erp_v1.response.InventoryResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryMapper {

	private final UserRepository userRepository;
	private final InventoryItemsMapper inventoryItemsMapper;

	public Inventory toEntity(InventoryRequest request) {
		Inventory inventory = new Inventory();
		inventory.setId(request.id());
		User employeeId = userRepository.findById(request.storageEmployeeId())
				.orElseThrow(() -> new StorageEmployeeNotFoundException(
						"Storage-Employee not found " + request.storageEmployeeId()));

		User foremanId = userRepository.findById(request.storageForemanId())
				.orElseThrow(() -> new StorageForemanNotFoundException(
						"Storage-Foreman not found: " + request.storageForemanId()));
		inventory.setStorageEmployee(employeeId);
		inventory.setStorageForeman(foremanId);
		inventory.setDate(request.date());
		inventory.setAligned(request.aligned());
		inventory.setStatus(request.status());
		if (request.inventoryItems() != null) {
			List<InventoryItems> items = request.inventoryItems().stream()
					.map(itemReq -> inventoryItemsMapper.toEntity(itemReq, inventory))
					.collect(Collectors.toList());
			items.forEach(i -> i.setInventory(inventory));
			inventory.setInventoryItems(items);
		}
		return inventory;
	}

	public InventoryResponse toResponse(Inventory inventory) {
		InventoryResponse response = new InventoryResponse();
		response.setId(inventory.getId());
		if (inventory.getStorageEmployee() != null) {
			response.setStorageEmployeeId(inventory.getStorageEmployee().getId());
			response.setStorageEmployeeName(inventory.getStorageEmployee().getUsername());
		}
		if (inventory.getStorageForeman() != null) {
			response.setStorageForemanId(inventory.getStorageForeman().getId());
			response.setStorageForemanName(inventory.getStorageForeman().getUsername());
		}
		response.setDate(inventory.getDate());
		response.setAligned(inventory.getAligned());
		if (inventory.getInventoryItems() != null) {
			List<InventoryItemsResponse> responses = inventory.getInventoryItems().stream()
					.map(inventoryItemsMapper::toResponse)
					.collect(Collectors.toList());
			response.setInventoryItems(responses);
		}
		// response.setStatus(inventory.getStatus() != null ?
		// inventory.getStatus().name() : null);
		if (inventory.getStatus() != null) {
			response.setStatus(inventory.getStatus());
		} else {
			response.setStatus(null);
		}
		return response;
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
