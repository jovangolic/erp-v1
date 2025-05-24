package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.exception.InventoryNotFoundException;
import com.jovan.erp_v1.exception.StorageEmployeeNotFoundException;
import com.jovan.erp_v1.exception.StorageForemanNotFoundException;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.InventoryItemsRepository;
import com.jovan.erp_v1.repository.InventoryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.request.InventoryRequest;
import com.jovan.erp_v1.response.InventoryResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {

	private final InventoryRepository inventoryRepository;
	private final UserRepository userRepository;
	private final InventoryItemsRepository inventoryItemsRepository;
	private final ProductRepository productRepository;

	@Transactional
	@Override
	public InventoryResponse create(InventoryRequest request) {
		Inventory inventory = new Inventory();
		User storageEmployee = userRepository.findById(request.storageEmployeeId())
				.orElseThrow(() -> new StorageEmployeeNotFoundException(
						"Storage-Employee not found: " + request.storageEmployeeId()));
		User storageForeman = userRepository.findById(request.storageForemanId())
				.orElseThrow(() -> new StorageForemanNotFoundException(
						"Storage-Foreman not found: " + request.storageForemanId()));
		inventory.getInventoryItems().clear();
		inventoryItemsRepository.deleteAllByInventoryId(request.id());
		inventory.setStorageEmployee(storageEmployee);
		inventory.setStorageForeman(storageForeman);
		inventory.setDate(request.date());
		inventory.setAligned(request.aligned());
		inventory.setStatus(request.status());
		// Sačuvaj osnovni inventory (da dobije ID)
		Inventory savedInventory = inventoryRepository.save(inventory);
		// Mapiraj stavke i poveži sa tim inventarom
		List<InventoryItems> items = mapInventoryItemRequestsToEntities(request.inventoryItems(), savedInventory);
		// Sačuvaj sve stavke
		inventoryItemsRepository.saveAll(items);
		// Postavi u savedInventory da bi response imao popunjenu listu (opciono)
		savedInventory.setInventoryItems(items);
		return new InventoryResponse(savedInventory);
	}

	@Transactional
	@Override
	public InventoryResponse update(Long id, InventoryRequest request) {
		Inventory inventory = inventoryRepository.findById(id)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));
		User storageEmployee = userRepository.findById(request.storageEmployeeId())
				.orElseThrow(() -> new StorageEmployeeNotFoundException(
						"Storage-Employee not found: " + request.storageEmployeeId()));
		User storageForeman = userRepository.findById(request.storageForemanId())
				.orElseThrow(() -> new StorageForemanNotFoundException(
						"Storage-Foreman not found: " + request.storageForemanId()));
		inventory.setStorageEmployee(storageEmployee);
		inventory.setStorageEmployee(storageEmployee);
		inventory.setStorageForeman(storageForeman);
		inventory.setDate(request.date());
		inventory.setAligned(request.aligned());
		inventory.setStatus(request.status());
		Inventory updated = inventoryRepository.save(inventory);
		List<InventoryItems> items = mapInventoryItemRequestsToEntities(request.inventoryItems(), updated);
		inventoryItemsRepository.saveAll(items);
		updated.setInventoryItems(items);
		return new InventoryResponse(updated);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if (!inventoryRepository.existsById(id)) {
			throw new InventoryNotFoundException("Inventory not found with id: " + id);
		}
		inventoryRepository.deleteById(id);
	}

	@Override
	public List<InventoryResponse> findInventoryByStatus(InventoryStatus status) {
		return inventoryRepository.findByStatus(status).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployeeId(Long storageEmployeeId) {
		return inventoryRepository.findByStorageEmployeeId(storageEmployeeId).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageForemanId(Long storageForemanId) {
		return inventoryRepository.findByStorageForemanId(storageForemanId).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public InventoryResponse findById(Long id) {
		Inventory inventory = inventoryRepository.findById(id)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));
		return new InventoryResponse(inventory);
	}

	@Override
	public List<InventoryResponse> findAll() {
		return inventoryRepository.findAll().stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByDate(LocalDate date) {
		return inventoryRepository.findByDate(date).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByDateRange(LocalDate startDate, LocalDate endDate) {
		return inventoryRepository.findByDateBetween(startDate, endDate).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public void changeStatus(Long inventoryId, InventoryStatus newStatus) {
		Inventory inventory = inventoryRepository.findById(inventoryId)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + inventoryId));

		inventory.setStatus(newStatus);
		inventoryRepository.save(inventory);
	}

	@Override
	public List<InventoryResponse> findPendingInventories() {
		return inventoryRepository.findPendingInventories().stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	private List<InventoryItems> mapInventoryItemRequestsToEntities(List<InventoryItemsRequest> requests,
			Inventory inventory) {
		return requests.stream().map(req -> {
			Product product = productRepository.findById(req.productId())
					.orElseThrow(() -> new RuntimeException("Product not found: " + req.productId()));

			InventoryItems item = new InventoryItems();
			item.setInventory(inventory);
			item.setProduct(product);
			item.setQuantity(req.quantity());
			item.setItemCondition(req.condition());
			return item;
		}).toList();
	}
}
