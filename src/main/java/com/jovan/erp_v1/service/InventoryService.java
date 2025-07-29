package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.exception.InventoryNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.InventoryMapper;
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
	private final InventoryMapper inventoryMapper;

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
		Inventory savedInventory = inventoryRepository.save(inventory);
		if (request.inventoryItems() == null || request.inventoryItems().isEmpty()) {
		    throw new IllegalArgumentException("Inventory must contain at least one item.");
		}
		List<InventoryItems> items = mapInventoryItemRequestsToEntities(request.inventoryItems(), savedInventory);
		inventoryItemsRepository.saveAll(items);
		savedInventory.setInventoryItems(items);
		return new InventoryResponse(savedInventory);
	}

	@Transactional
	@Override
	public InventoryResponse update(Long id, InventoryRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Inventory inventory = inventoryRepository.findById(id)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));
		User storageEmployee = validateStorageEmployee(request.storageEmployeeId());
		User storageForeman = validateStorageForeman(request.storageForemanId());
		DateValidator.validateNotNull(request.date(), "Date must not be null");
		validateBoolean(request.aligned(), "Must not be null");
		validateInventoryStatus(request.status());
		inventory.getInventoryItems().clear();
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
		List<Inventory> items = inventoryRepository.findByStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory found for status %s", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployeeId(Long storageEmployeeId) {
		validateStorageEmployee(storageEmployeeId);
		List<Inventory> items = inventoryRepository.findByStorageEmployeeId(storageEmployeeId);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory found for storage employee id %d", storageEmployeeId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageForemanId(Long storageForemanId) {
		validateStorageForeman(storageForemanId);
		List<Inventory> items = inventoryRepository.findByStorageForemanId(storageForemanId);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory found for storage foreman id %d", storageForemanId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
		List<Inventory> items = inventoryRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Inventory list is empty");
		}
		return items.stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByDate(LocalDate date) {
		DateValidator.validateNotNull(date, "Date must not be null");
		List<Inventory> items = inventoryRepository.findByDate(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg  =String.format("No Inventory found for given date %s", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByDateRange(LocalDate startDate, LocalDate endDate) {
		DateValidator.validateRange(startDate, endDate);
		List<Inventory> items = inventoryRepository.findByDateBetween(startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Inventory found for date between %s and %s",
					startDate.format(formatter),endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
		List<Inventory> items = inventoryRepository.findPendingInventories();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inventory found for pending");
		}
		return items.stream()
				.map(InventoryResponse::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<InventoryResponse> findByDateAfter(LocalDate date) {
		DateValidator.validateNotInPast(date, "Date after");
		List<Inventory> items = inventoryRepository.findByDateAfter(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Inventory found for date after %s", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByDateBefore(LocalDate date) {
		DateValidator.validateNotInFuture(date, "Date before");
		List<Inventory> items = inventoryRepository.findByDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Inventory found for date before %s", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployee_FullNameContainingIgnoreCase(String firstName,
			String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<Inventory> items = inventoryRepository.findByStorageEmployee_FullNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for employee first-name %s and last-name %s is found", 
					firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findBystorageForeman_FullNameContainingIgnoreCase(String firstName,
			String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<Inventory> items = inventoryRepository.findBystorageForeman_FullNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for foreman first-name %s and last-name %s is found", 
					firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployee_EmailILikegnoreCase(String email) {
		validateString(email);
		List<Inventory> items = inventoryRepository.findByStorageEmployee_EmailLikeIgnoreCase(email);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for employee email %s is found", email);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployee_Address(String address) {
		validateString(address);
		List<Inventory> items = inventoryRepository.findByStorageEmployee_Address(address);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for employee address %s is found", address);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployee_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<Inventory> items = inventoryRepository.findByStorageEmployee_PhoneNumberLikeIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for employee phone-number %s is found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageForeman_Address(String address) {
		validateString(address);
		List<Inventory> items = inventoryRepository.findByStorageForeman_Address(address);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for foreman address %s is found", address);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageForeman_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		List<Inventory> items = inventoryRepository.findByStorageForeman_PhoneNumberLikeIgnoreCase(phoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for foreman phone-number %s is found", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageForeman_EmailLikeIgnoreCase(String email) {
		validateString(email);
		List<Inventory> items = inventoryRepository.findByStorageForeman_EmailLikeIgnoreCase(email);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for foreman email %s is found", email);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStatusAndStorageEmployeeFullNameContainingIgnoreCase(InventoryStatus status,
			String firstName, String lastName) {
		validateInventoryStatus(status);
		validateString(firstName);
		validateString(lastName);
		List<Inventory> items = inventoryRepository.findByStatusAndStorageEmployeeFullNameContainingIgnoreCase(status, firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for status %s, and first-name %s, last-name $s is found", 
					status,firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStatusAndStorageForemanFullNameContainingIgnoreCase(InventoryStatus status,
			String firstName, String lastName) {
		validateInventoryStatus(status);
		validateString(firstName);
		validateString(lastName);
		List<Inventory> items = inventoryRepository.findByStatusAndStorageForemanFullNameContainingIgnoreCase(status, firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for status %s, and first-name %s, last-name $s is found", 
					status,firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public Long countByStorageForemanId(Long foremanId) {
		validateStorageForeman(foremanId);
		return inventoryRepository.countByStorageForemanId(foremanId);
	}

	@Override
	public Boolean existsByStatus(InventoryStatus status) {
		validateInventoryStatus(status);
		return inventoryRepository.existsByStatus(status);
	}
	
	@Override
	public List<InventoryResponse> findInventoryByStorageForemanIdAndDateRange(Long foremanId, LocalDate startDate,
			LocalDate endDate) {
		validateStorageForeman(foremanId);
		DateValidator.validateRange(startDate, endDate);
		List<Inventory> items = inventoryRepository.findInventoryByStorageForemanIdAndDateRange(foremanId, startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Inventory for foreman-id %d and date between %s and %s is found", 
					foremanId,startDate.format(formatter), endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployeeIdAndStatus(Long employeeId, InventoryStatus status) {
		validateStorageEmployee(employeeId);
		validateInventoryStatus(status);
		List<Inventory> items = inventoryRepository.findByStorageEmployeeIdAndStatus(employeeId, status);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for employee-id %d and status %s is found", 
					employeeId,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageForemanIdAndStatus(Long foremanId, InventoryStatus status) {
		validateStorageForeman(foremanId);
		validateInventoryStatus(status);
		List<Inventory> items = inventoryRepository.findByStorageForemanIdAndStatus(foremanId, status);
		if(items.isEmpty()) {
			String msg = String.format("No Inventory for foreman-id %d and status %s is found",
					foremanId, status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate,
			LocalDate endDate) {
		validateStorageEmployee(employeeId);
		DateValidator.validateRange(startDate, endDate);
		List<Inventory> items = inventoryRepository.findByStorageEmployeeIdAndDateBetween(employeeId, startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Inventory for employee-id %d and date between %s and %s is found",
					employeeId,startDate.format(formatter), endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryResponse> findByStorageForemanIdAndDateBetween(Long foremanId, LocalDate startDate,
			LocalDate endDate) {
		validateStorageForeman(foremanId);
		DateValidator.validateRange(startDate, endDate);
		List<Inventory> items = inventoryRepository.findByStorageForemanIdAndDateBetween(foremanId, startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Inventory for foreman-id %d and date between %s and %s is found", 
					foremanId,startDate.format(formatter),endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
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

	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}

}
