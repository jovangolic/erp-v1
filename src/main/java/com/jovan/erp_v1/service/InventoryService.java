package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.InventoryTypeStatus;
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
import com.jovan.erp_v1.repository.specification.InventorySpecification;
import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.request.InventoryRequest;
import com.jovan.erp_v1.response.InventoryResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.InventoryItemSaveAsRequest;
import com.jovan.erp_v1.save_as.InventorySaveAsWithItemsRequest;
import com.jovan.erp_v1.search_request.InventorySearchRequest;
import com.jovan.erp_v1.statistics.inventory.InventoryEmployeeStatDTO;
import com.jovan.erp_v1.statistics.inventory.InventoryForemanStatDTO;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
	
	@Override
	public InventoryResponse approveInventory(Long id) {
		Inventory inv = inventoryRepository.findById(id).orElseThrow(() -> new ValidationException("Inventory not found wit h id "+id));
		if(!InventoryStatus.PENDING_APPROVAL.equals(inv.getStatus())) {
			throw new ValidationException("Inventory must be in PENDING_APPROVAL status to approve");
		}
		inv.setStatus(InventoryStatus.APPROVED);
        inv.setAligned(true); 
        inv.setModifiedAt(LocalDateTime.now());
        Inventory saved = inventoryRepository.save(inv);
        return new InventoryResponse(saved);
	}
	
	@Transactional(readOnly = true)
	@Override
	public InventoryResponse trackInventory(Long id) {
		List<Inventory> inv = inventoryRepository.trackInventory(id);
		if(inv.isEmpty()) {
			throw new NoDataFoundException("Inventory with id "+id+" not found");
		}
		Inventory item = inv.get(0);
		return new InventoryResponse(item);
	}

	@Transactional
	@Override
	public InventoryResponse confirmInventory(Long id) {
		Inventory inv = inventoryRepository.findById(id).orElseThrow(() -> new ValidationException("Inventory not found with id "+id));
		validateAndNormalizeInventoryItems(inv);
		inv.setConfirmed(true);
		inv.setTypeStatus(InventoryTypeStatus.CONFIRMED);
		inv.getInventoryItems().stream()
			.forEach(i -> i.setConfirmed(true));
		return new InventoryResponse(inventoryRepository.save(inv));
	}

	@Transactional
	@Override
	public InventoryResponse cancelInventory(Long id) {
		Inventory inv = inventoryRepository.findById(id).orElseThrow(() -> new ValidationException("Inventory not found with id "+id));
		if(inv.getTypeStatus() != InventoryTypeStatus.NEW && inv.getTypeStatus() != InventoryTypeStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED inventories can be cancelled");
		}
		inv.setTypeStatus(InventoryTypeStatus.CANCELLED);
		return new InventoryResponse(inventoryRepository.save(inv));
	}

	@Transactional
	@Override
	public InventoryResponse closeInventory(Long id) {
		Inventory inv = inventoryRepository.findById(id).orElseThrow(() -> new ValidationException("Inventory not found with id "+id));
		if(inv.getTypeStatus() != InventoryTypeStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED inventories can be closed");
		}
		inv.setTypeStatus(InventoryTypeStatus.CLOSED);
		return new InventoryResponse(inventoryRepository.save(inv));
	}

	@Transactional
	@Override
	public InventoryResponse changeStatus(Long id, InventoryTypeStatus typeStatus) {
		Inventory inv = inventoryRepository.findById(id).orElseThrow(() -> new ValidationException("Inventory not found with id "+id));
		validateInventoryTypeStatus(typeStatus);
		if(inv.getTypeStatus() == InventoryTypeStatus.CLOSED) {
			throw new ValidationException("Closed inventories cannot change status");
		}
		if(typeStatus == InventoryTypeStatus.CONFIRMED) {
			if(inv.getTypeStatus() != InventoryTypeStatus.NEW) {
				throw new ValidationException("Only NEW inventories can be confirmed");
			}
			inv.setConfirmed(true);
			inv.getInventoryItems().forEach(i -> i.setConfirmed(true));;
		}
		inv.setTypeStatus(typeStatus);
		return new InventoryResponse(inventoryRepository.save(inv));
	}

	@Transactional
	@Override
	public InventoryResponse saveInventory(InventoryRequest request) {
		User storageEmployee = validateStorageEmployee(request.storageEmployeeId());
	    User storageForeman = validateStorageForeman(request.storageForemanId());
	    List<InventoryItems> items = request.inventoryItems().stream()
	            .map(req -> {
	                if (req.condition() == null || req.quantity() == null) {
	                    throw new ValidationException("Quantity and item condition must not be null for item ID: " + req.id());
	                }
	                if (req.condition().compareTo(req.quantity()) > 0) {
	                    throw new ValidationException("Item condition cannot be greater than quantity for item ID: " + req.id());
	                }
	                BigDecimal diff = validateAndCalculateDifference(req.quantity(), req.condition(), req.id());
	                //upisivanje u InventoryItems podatke
	                return InventoryItems.builder()
	                        .product(validateProductId(req.productId()))
	                        .quantity(req.quantity())
	                        .itemCondition(req.condition())
	                        .difference(diff)
	                        .confirmed(false)
	                        .build();
	            })
	            .toList();
		Inventory inv = Inventory.builder()
				.storageEmployee(storageEmployee)
				.storageForeman(storageForeman)
				.date(LocalDate.now())
				.aligned(request.aligned())
				.inventoryItems(items)
				.status(request.status())
				.build();
		//Povezivanje svakog InventoryItems sa roditeljem
		items.forEach(i -> i.setInventory(inv));
		Inventory saved = inventoryRepository.save(inv);
		return new InventoryResponse(saved);
	}
	
	private final AbstractSaveAsService<Inventory, InventoryResponse> saveAsHelper = new AbstractSaveAsService<Inventory, InventoryResponse>() {
		
		@Override
		protected InventoryResponse toResponse(Inventory entity) {
			return new InventoryResponse(entity);
		}
		
		@Override
		protected JpaRepository<Inventory, Long> getRepository() {
			return inventoryRepository;
		}
		
		@Override
		protected Inventory copyAndOverride(Inventory source, Map<String, Object> overrides) {
			List<InventoryItems> copiedItems = new ArrayList<InventoryItems>();
		    // Ako request ima inventoryItems, koristim njih.
		    if(overrides.containsKey("inventoryItems")) {
		    	@SuppressWarnings("unchecked")
		        List<InventoryItemSaveAsRequest> newItems = (List<InventoryItemSaveAsRequest>) overrides.get("inventoryItems");
		    	copiedItems = newItems.stream()
		    			.map(itemReq -> InventoryItems.builder()
		    					.product(validateProductId(itemReq.productId()))
		    					.quantity(itemReq.quantity())
		    					.itemCondition(itemReq.itemCondition())
		    					.difference(itemReq.quantity().subtract(itemReq.itemCondition()))
		    					.confirmed(false)
		    					.build())
		    			.toList();
		    }
		    else {
		    	//kopiram iz postojeceg izvornog inventara
		    	copiedItems = source.getInventoryItems().stream()
		    			.map(req -> InventoryItems.builder()
		    					.product(req.getProduct())
		    					.quantity(req.getQuantity())
		    					.itemCondition(req.getItemCondition())
		    					.difference(req.getDifference())
		    					.confirmed(false)
		    					.build())
		    			.toList();
		    }
		    return Inventory.builder()
		            .storageEmployee(validateStorageEmployee((Long) overrides.getOrDefault("storageEmployeeId", source.getStorageEmployee().getId())))
		            .storageForeman(validateStorageForeman((Long) overrides.getOrDefault("storageForemanId", source.getStorageForeman().getId())))
		            .aligned((Boolean) overrides.getOrDefault("aligned", source.getAligned()))
		            .status((InventoryStatus) overrides.getOrDefault("status", source.getStatus()))
		            .typeStatus((InventoryTypeStatus) overrides.getOrDefault("typeStatus", source.getTypeStatus()))
		            .confirmed((Boolean) overrides.getOrDefault("confirmed", source.getConfirmed()))
		            .inventoryItems(copiedItems)
		            .build();
		}
	};

	@Transactional
	@Override
	public InventoryResponse saveAs(InventorySaveAsWithItemsRequest request) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		User storageEmployee = validateStorageEmployee(request.storageEmployeeId());
	    User storageForeman = validateStorageForeman(request.storageForemanId());
	    //provera da li je kolekcija null ili prazna
	    if (request.inventoryItems() == null || request.inventoryItems().isEmpty()) {
	        throw new ValidationException("Inventory must contain at least one item.");
	    }
	    List<InventoryItems> items = request.inventoryItems().stream()
	    		.map(req -> {
	    			if (req.itemCondition() == null || req.quantity() == null) {
	                    throw new ValidationException("Quantity and item condition must not be null for item");
	                }
	                if (req.itemCondition().compareTo(req.quantity()) > 0) {
	                    throw new ValidationException("Item condition cannot be greater than quantity for item");
	                }
	                BigDecimal diff = req.quantity().subtract(req.itemCondition()).max(BigDecimal.ZERO);
	    			return InventoryItems.builder()
	    					.product(validateProductId(req.productId()))
	    					.quantity(req.quantity())
	                        .itemCondition(req.itemCondition())
	                        .difference(diff)
	                        .confirmed(false)
	    					.build();
	    		})
	    		.toList();
	    if(request.storageEmployeeId() != null) overrides.put("Employee ID", storageEmployee);
	    if(request.storageForemanId() != null) overrides.put("Foreman ID", storageForeman);
	    if(request.aligned() != null) overrides.put("Aligned", request.aligned());
	    if(request.inventoryItems() != null) overrides.put("Inventory-items", items);
	    if(request.status() != null && !request.inventoryItems().isEmpty()) overrides.put("inventoryItems", items);
	    if(request.typeStatus() != null) overrides.put("Type-status", request.typeStatus());
	    if(request.confirmed() != null) overrides.put("Confirmed", request.confirmed());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}
	
	private final AbstractSaveAllService<Inventory, InventoryResponse> saveAllHelper = new AbstractSaveAllService<Inventory, InventoryResponse>() {
		
		@Override
		protected Function<Inventory, InventoryResponse> toResponse() {
			return InventoryResponse::new;
		}
		
		@Override
		protected JpaRepository<Inventory, Long> getRepository() {
			return inventoryRepository;
		}
		
		@Override
		protected void beforeSaveAll(List<Inventory> entities) {
		    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		    log.info("User '{}' is saving {} inventory ", currentUser, entities.size());
		}
	};

	@Transactional
	@Override
	public List<InventoryResponse> saveAll(List<InventoryRequest> requests) {
		if (requests == null || requests.isEmpty()) {
	        throw new ValidationException("Inventory request list must not be empty.");
	    }
		List<Inventory> inventories = requests.stream()
				.map(item -> {
					User storageEmployee = validateStorageEmployee(item.storageEmployeeId());
					User storageForeman = validateStorageForeman(item.storageForemanId());
					if(item.inventoryItems() == null || item.inventoryItems().isEmpty()) {
						throw new ValidationException("Inventory must contain at least one item.");
					}
					List<InventoryItems> inventoryItems = item.inventoryItems().stream()
							.map(itemReq -> {
								if (itemReq.condition() == null || itemReq.quantity() == null) {
					                throw new ValidationException("Quantity and item condition must not be null for product ID: " + itemReq.productId());
					            }
					            if (itemReq.condition().compareTo(itemReq.quantity()) > 0) {
					                throw new ValidationException("Item condition cannot be greater than quantity for product ID: " + itemReq.productId());
					            }
					            BigDecimal difference = itemReq.quantity().subtract(itemReq.condition()).max(BigDecimal.ZERO);
					            return InventoryItems.builder()
					                    .product(validateProductId(itemReq.productId()))
					                    .quantity(itemReq.quantity())
					                    .itemCondition(itemReq.condition())
					                    .difference(difference)
					                    .confirmed(false)
					                    .build();
							})
							.toList();
					Inventory inventory = Inventory.builder()
			                .storageEmployee(storageEmployee)
			                .storageForeman(storageForeman)
			                .aligned(item.aligned())
			                .status(item.status())
			                .typeStatus(item.typeStatus() != null ? item.typeStatus() : InventoryTypeStatus.NEW)
			                .confirmed(false)
			                .date(LocalDate.now())
			                .inventoryItems(inventoryItems)
			                .build();
					inventoryItems.forEach(it -> it.setInventory(inventory));
			        return inventory;
				})
				.toList();
		List<Inventory> savedInventories = inventoryRepository.saveAll(inventories);
		return saveAllHelper.saveAll(savedInventories);
	}

	@Override
	public List<InventoryResponse> generalSearch(InventorySearchRequest request) {
		Specification<Inventory> spec = InventorySpecification.fromRequest(request);
		List<Inventory> items = inventoryRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inventories for given criteria found");
		}
		return items.stream().map(inventoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryEmployeeStatDTO> countInventoryByEmployee() {
		List<InventoryEmployeeStatDTO> items = inventoryRepository.countInventoryByEmployee();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inventories count for employee, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Long employeeId = item.getEmployeeId();
					InventoryStatus status = item.getStatus();
				    InventoryTypeStatus typeStatus = item.getTypeStatus();
				    return new InventoryEmployeeStatDTO(count, employeeId, status, typeStatus);
				})
				.toList();
	}

	@Override
	public List<InventoryForemanStatDTO> countInventoryByForeman() {
		List<InventoryForemanStatDTO> items = inventoryRepository.countInventoryByForeman();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inventories count for foreman, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Long foremanId = item.getForemanId();
					InventoryStatus status = item.getStatus();
				    InventoryTypeStatus typeStatus = item.getTypeStatus();
				    return new InventoryForemanStatDTO(count, foremanId, status, typeStatus);
				})
				.toList();
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

	private void validateInventoryTypeStatus(InventoryTypeStatus typeStatus) {
		Optional.ofNullable(typeStatus)
			.orElseThrow(() -> new ValidationException("InventoryTypeStatus typeStatus must not be null"));
	}
	
	private void validateAndNormalizeInventoryItems(Inventory inventory) {
		inventory.getInventoryItems().stream().forEach(item -> {
			if(item.getQuantity() == null || item.getItemCondition() == null) {
				throw new ValidationException("Quantity and item condition must not be null for item with ID: "+item.getId());
			}
			//ako je stanje vece od proverene(inventurisane) kolicine
			if(item.getItemCondition().compareTo(item.getQuantity()) > 0) {
				throw new ValidationException("Item condition cannot be greater than quantity for item with ID: " + item.getId());
			}
			BigDecimal difference = item.calculateDifference();
			item.setDifference(difference);	
		});
	}
	
	private Product validateProductId(Long id) {
		if(id == null) {
			throw new ValidationException("Product ID must not be null");
		}
		return productRepository.findById(id).orElseThrow(() -> new ValidationException("Product not found with id "+id));
	}
	
	private BigDecimal validateAndCalculateDifference(BigDecimal quantity, BigDecimal condition, Long itemId) {
	    if (quantity == null || condition == null) {
	        throw new ValidationException("Quantity and condition must not be null for item ID: " + itemId);
	    }
	    if (condition.compareTo(quantity) > 0) {
	        throw new ValidationException("Condition cannot exceed quantity for item ID: " + itemId);
	    }
	    return quantity.subtract(condition).max(BigDecimal.ZERO);
	}
}
