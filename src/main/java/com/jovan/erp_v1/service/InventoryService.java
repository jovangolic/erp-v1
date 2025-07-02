package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.exception.InventoryNotFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
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
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
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
		User storageEmployee = validateStorageEmployee(request.storageEmployeeId());
		User storageForeman = validateStorageForeman(request.storageForemanId());
		DateValidator.validateNotNull(request.date(), "Date must not be null");
		validateBoolean(request.aligned(), "Must not be null");
		validateInventoryStatus(request.status());
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
		if (request.inventoryItems() == null || request.inventoryItems().isEmpty()) {
		    throw new IllegalArgumentException("Inventory must contain at least one item.");
		}
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
		User storageEmployee = validateStorageEmployee(request.storageEmployeeId());
		User storageForeman = validateStorageForeman(request.storageForemanId());
		DateValidator.validateNotNull(request.date(), "Date must not be null");
		validateBoolean(request.aligned(), "Must not be null");
		validateInventoryStatus(request.status());
		inventory.getInventoryItems().clear(); //dodao ovu liniju koda prilikom azuriranja
		inventory.setStorageEmployee(storageEmployee);
		inventory.setStorageForeman(storageForeman);
		inventory.setDate(request.date());
		inventory.setAligned(request.aligned());
		inventory.setStatus(request.status());
		Inventory updated = inventoryRepository.save(inventory);
		if (request.inventoryItems() == null || request.inventoryItems().isEmpty()) {
		    throw new IllegalArgumentException("Inventory must contain at least one item.");
		}
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
		validateInventoryStatus(status);
		return inventoryRepository.findByStatus(status).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployeeId(Long storageEmployeeId) {
		validateStorageEmployee(storageEmployeeId);
		return inventoryRepository.findByStorageEmployeeId(storageEmployeeId).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageForemanId(Long storageForemanId) {
		validateStorageForeman(storageForemanId);
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
		DateValidator.validateNotNull(date, "Date must not be null");
		return inventoryRepository.findByDate(date).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByDateRange(LocalDate startDate, LocalDate endDate) {
		DateValidator.validateRange(startDate, endDate);
		return inventoryRepository.findByDateBetween(startDate, endDate).stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void changeStatus(Long inventoryId, InventoryStatus newStatus) {
		Inventory inventory = validateInventory(inventoryId);
		validateInventoryStatus(newStatus);
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
					.orElseThrow(() -> new ProductNotFoundException("Product not found: " + req.productId()));
			InventoryItems item = new InventoryItems();
			item.setInventory(inventory);
			item.setProduct(product);
			item.setQuantity(req.quantity());
			item.setItemCondition(req.condition());
			return item;
		}).toList();
	}
	
	private Inventory validateInventory(Long inventoryId) {
		if(inventoryId == null) {
			throw new IllegalArgumentException("Inventory ID must not be null");
		}
		return inventoryRepository.findById(inventoryId).
				orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + inventoryId));
	}
	
	private User validateStorageEmployee(Long storageEmployeeId) {
		if(storageEmployeeId == null) {
			throw new UsernameNotFoundException("StorageEmployee must not be null");
		}
		return userRepository.findById(storageEmployeeId).orElseThrow(() -> new UsernameNotFoundException("StorageEmployee Id "+storageEmployeeId+" not found"));
	}
	
	private User validateStorageForeman(Long storageForemanId) {
		if(storageForemanId == null) {
			throw new UsernameNotFoundException("StorageEmployee must not be null");
		}
		return userRepository.findById(storageForemanId).orElseThrow(() -> new UsernameNotFoundException("StorageForemanId Id "+storageForemanId+" not found"));
	}
	
	private void validateBoolean(Boolean value, String fieldName) {
	    if (value == null) {
	        throw new IllegalArgumentException(fieldName + " must be provided.");
	    }
	}
	
	private void validateInventoryStatus(InventoryStatus status) {
		if(status == null) {
			throw new IllegalArgumentException("InventoryStatus status must not be null");
		}
	}
}
