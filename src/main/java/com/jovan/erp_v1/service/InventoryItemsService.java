package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InventoryItemsStatus;
import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.InventoryItemsNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.InventoryItemsMapper;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.InventoryItemsRepository;
import com.jovan.erp_v1.repository.InventoryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.specification.InventoryItemsSpecification;
import com.jovan.erp_v1.request.InventoryItemCalculateRequest;
import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.response.InventoryItemCalculateResponse;
import com.jovan.erp_v1.response.InventoryItemStorageCapacityResponse;
import com.jovan.erp_v1.response.InventoryItemsResponse;
import com.jovan.erp_v1.response.InventorySummaryResponse;
import com.jovan.erp_v1.response.StorageCapacityAndInventorySummaryResponse;
import com.jovan.erp_v1.response.StorageCapacityAndInventorySummaryResponseFull;
import com.jovan.erp_v1.response.StorageCapacityResponse;
import com.jovan.erp_v1.response.StorageItemSummaryResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.InventoryItemsSaveAsRequest;
import com.jovan.erp_v1.search_request.InventoryItemsSearchRequest;
import com.jovan.erp_v1.statistics.inventory_items.InventoryStatRequest;
import com.jovan.erp_v1.statistics.inventory_items.InventoryStatResponse;
import com.jovan.erp_v1.statistics.inventory_items.ItemConditionByProductStatDTO;
import com.jovan.erp_v1.statistics.inventory_items.QuantityByProductStatDTO;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryItemsService implements IInventoryItemsService {

	private final InventoryItemsSpecification specification;
	private final InventoryItemsRepository inventoryItemsRepository;
	private final InventoryRepository inventoryRepository;
	private final ProductRepository productRepository;
	private final InventoryItemsMapper inventoryItemsMapper;
	private final ShelfRepository shelfRepository;
	private final StorageRepository storageRepository;
	private final SupplyRepository supplyRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public InventoryItemsResponse create(InventoryItemsRequest request) {
		Inventory inventory = validateInventory(request.inventoryId());
		Product product = validateProduct(request.productId());
		validateBigDecimal(request.quantity());
		validateBigDecimal(request.condition());
		InventoryItems items = inventoryItemsMapper.toEntity(request, inventory, product);
		items.setDifference(calculateDifference(request.quantity(), request.condition()));
		InventoryItems saved = inventoryItemsRepository.save(items);
		return new InventoryItemsResponse(saved);
	}

	@Transactional
	@Override
	public InventoryItemsResponse update(Long id, InventoryItemsRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		InventoryItems items = inventoryItemsRepository.findById(id)
				.orElseThrow(() -> new InventoryItemsNotFoundException("Inventory-Items not found with id: " + id));
		Inventory inventory = validateInventory(request.inventoryId());
		Product product = validateProduct(request.productId());
		validateBigDecimal(request.quantity());
		validateBigDecimal(request.condition());
		inventoryItemsMapper.toEntityUpdate(items, request, inventory, product);
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
		List<InventoryItems> items = inventoryItemsRepository.findByQuantity(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems found for quantity %s", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByItemCondition(BigDecimal itemCondition) {
		validateBigDecimal(itemCondition);
		List<InventoryItems> items = inventoryItemsRepository.findByItemCondition(itemCondition);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems found for item-condition %s", itemCondition);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByInventoryId(Long inventoryId) {
		validateInventory(inventoryId);
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryId(inventoryId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems found for inventory-id %d", inventoryId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByProductId(Long productId) {
		validateProduct(productId);
		List<InventoryItems> items = inventoryItemsRepository.findByProductId(productId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems found for product-id %d", productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> getByProductName(String productName) {
		validateString(productName);
		List<InventoryItems> items = inventoryItemsRepository.findByProductName(productName);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems found for product-name %s", productName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
		List<InventoryItems> items = inventoryItemsRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("List of InventoryItems is empty");
		}
		return items.stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findItemsWithDifference(BigDecimal threshold) {
		validateBigDecimal(threshold);
		List<InventoryItems> items = inventoryItemsRepository.findByDifferenceGreaterThan(threshold);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems found with difference greter than %s", threshold);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(InventoryItemsResponse::new)
				.collect(Collectors.toList());
	}
	
	//nove metode
	
	@Override
	public List<InventoryItemsResponse> findByDifference(BigDecimal difference) {
		validateBigDecimal(difference);
		List<InventoryItems> items = inventoryItemsRepository.findByDifference(difference);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for difference %s is found", difference);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByDifferenceLessThan(BigDecimal difference) {
		validateBigDecimalNonNegative(difference);
		List<InventoryItems> items = inventoryItemsRepository.findByDifferenceLessThan(difference);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for difference less than %s is found", difference);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByQuantityGreaterThan(BigDecimal quantity) {
		validateBigDecimal(quantity);
		List<InventoryItems> items = inventoryItemsRepository.findByQuantityGreaterThan(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for quantity greater than %s is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByQuantityLessThan(BigDecimal quantity) {
		validateBigDecimalNonNegative(quantity);
		List<InventoryItems> items = inventoryItemsRepository.findByQuantityLessThan(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for quantity less than %s is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByItemConditionGreaterThan(BigDecimal itemCondition) {
		validateBigDecimal(itemCondition);
		List<InventoryItems> items = inventoryItemsRepository.findByItemConditionGreaterThan(itemCondition);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for item-condition greater than %s is found", itemCondition);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByItemConditionLessThan(BigDecimal itemCondition) {
		validateBigDecimalNonNegative(itemCondition);
		List<InventoryItems> items = inventoryItemsRepository.findByItemConditionLessThan(itemCondition);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for item-condition less than %s is found", itemCondition);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByItemConditionAndQuantity(BigDecimal itemCondition, BigDecimal quantity) {
		validateBigDecimal(itemCondition);
		validateBigDecimal(quantity);
		List<InventoryItems> items = inventoryItemsRepository.findByItemConditionAndQuantity(itemCondition, quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for item-condition %s and quantity %s , is found", 
					itemCondition, quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemCalculateResponse> findInventoryItemsForCalculation(Long inventoryId) {
		List<InventoryItemCalculateRequest> items = inventoryItemsRepository.findInventoryItemsForCalculation(inventoryId);
	    return items.stream()
	        .map(item -> {
	            BigDecimal quantity = item.quantity();
	            BigDecimal condition = item.itemCondition();
	            BigDecimal difference = quantity.subtract(condition).max(BigDecimal.ZERO);
	            return new InventoryItemCalculateResponse(item.id(), quantity, condition, difference);
	        })
	        .toList();
	}

	@Override
	public List<InventoryItemCalculateResponse> findItemsForShortageAllowed(Long inventoryId) {
		List<InventoryItemCalculateRequest> items = inventoryItemsRepository.findItemsForShortageAllowed(inventoryId);
	    return items.stream()
	        .map(item -> {
	            BigDecimal quantity = item.quantity();
	            BigDecimal condition = item.itemCondition();
	            BigDecimal difference = (quantity != null && condition != null)
	                ? quantity.subtract(condition)
	                : BigDecimal.ZERO;
	            return new InventoryItemCalculateResponse(item.id(), quantity, condition, difference);
	        })
	        .toList();
	}

	@Override
	public List<InventoryItemsResponse> findByInventory_StorageEmployee_Id(Long storageEmployeeId) {
		validateUserId(storageEmployeeId);
		List<InventoryItems> items = inventoryItemsRepository.findByInventory_StorageEmployee_Id(storageEmployeeId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for inventory's storage employee-id %d is found", storageEmployeeId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventory_StorageForeman_Id(Long storageForemanId) {
		validateUserId(storageForemanId);
		List<InventoryItems> items = inventoryItemsRepository.findByInventory_StorageForeman_Id(storageForemanId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for inventory's storage foreman-id %d is found", storageForemanId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryDate(LocalDate date) {
		DateValidator.validateNotInFuture(date, "Inventory date");
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryDate(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No InventoryItems bound for inventory's date %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryDateBetween(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No InventoryItems bound for inventory date between %s and %s is found",
					start.format(formatter), end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryDateAfter(LocalDate date) {
		DateValidator.validateNotInPast(date, "Inventory date after");
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryDateAfter(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No InventoryItems bound for inventory's date after %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryDateBefore(LocalDate date) {
		DateValidator.validateNotInFuture(date, "Inventory date before");
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No InventoryItems bound for inventory's date before %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventory_Status(InventoryStatus status) {
		validateInventoryStatus(status);
		List<InventoryItems> items = inventoryItemsRepository.findByInventory_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for inventory's status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public Boolean existsByInventory_Aligned(Boolean aligned) {
		return inventoryItemsRepository.existsByInventory_Aligned(aligned);
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryAlignedFalse() {
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryAlignedFalse();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems for inventory aligned equal to false, is found");
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryStatusAndInventoryStorageEmployeeId(InventoryStatus status,
			Long storageEmployeeId) {
		validateInventoryStatus(status);
		validateUserId(storageEmployeeId);
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryStatusAndInventoryStorageEmployeeId(status, storageEmployeeId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for inventory's status %s and storage employee-id %d is found", 
					status,storageEmployeeId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryStatusAndInventoryStorageForemanId(InventoryStatus status,
			Long storageForemanId) {
		validateInventoryStatus(status);
		validateUserId(storageForemanId);
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryStatusAndInventoryStorageForemanId(status, storageForemanId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for inventory's status %s and storage foreman-id %d is found", 
					status,storageForemanId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public Boolean existsByInventoryAlignedFalseAndInventoryStorageEmployeeId(Long employeeId) {
		validateUserId(employeeId);
		return inventoryItemsRepository.existsByInventoryAlignedFalseAndInventoryStorageEmployeeId(employeeId);
	}

	@Override
	public List<InventoryItemsResponse> findItemsWithNonZeroDifference() {
		List<InventoryItems> items = inventoryItemsRepository.findItemsWithNonZeroDifference();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems with non-zero difference is found");
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryStatusAndInventoryAlignedFalse(InventoryStatus status) {
		validateInventoryStatus(status);
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryStatusAndInventoryAlignedFalse(status);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for inventory's status %s and aligned equal false, is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByInventoryDateAndInventoryStorageForemanId(LocalDate date,
			Long foremanId) {
		DateValidator.validatePastOrPresent(date, "inventory date");
		validateUserId(foremanId);
		List<InventoryItems> items = inventoryItemsRepository.findByInventoryDateAndInventoryStorageForemanId(date, foremanId);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No InventoryItems bound for inventory's date %s and storage foreman-id %d, is found",
					date.format(formatter), foremanId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventorySummaryResponse> fetchInventorySummaries() {
		List<InventorySummaryResponse> items = inventoryItemsRepository.fetchInventorySummaries();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems for inventory summaries is found");
		}
		return items.stream()
				.map(item -> {
					Long inventoryId = item.inventoryId();
				    Long itemCount = item.itemCount();
				    BigDecimal totalQuantity = item.totalQuantity();
				    return new InventorySummaryResponse(inventoryId, itemCount, totalQuantity);
				})
				.toList();
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);;
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_CurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's current quantity %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's current quantity greater than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's current quantity less than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's unit-measure %s is found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's supplier-type %s is found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_StorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_StorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's storage-type %s is found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_GoodsType(GoodsType type) {
		validateGoodsType(type);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_GoodsType(type);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's goods-type %s is found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_StorageId(Long storageId) {
		validateStorageId(storageId);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_StorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's storage-id %d, is found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_StorageNameContainingIgnoreCase(String storageName) {
		validateString(storageName);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_StorageNameContainingIgnoreCase(storageName);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's storage name %s is found",
					storageName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_StorageLocationContainingIgnoreCase(String storageLocation) {
		validateString(storageLocation);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_StorageLocationContainingIgnoreCase(storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's storage location %s is found",
					storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_StorageCapacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_StorageCapacity(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's storage capacity %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_StorageCapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_StorageCapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's storage capacity greater than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_StorageCapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_StorageCapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's storage capacity less than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_Storage_Status(StorageStatus status) {
		validateStorageStatus(status);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_Storage_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No Inventoryitems bound for product's storage status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemStorageCapacityResponse> fetchUsedCapacitiesForItems() {
		List<InventoryItemStorageCapacityResponse> items = inventoryItemsRepository.fetchUsedCapacitiesForItems();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems found for used capacity items");
		}
		return items.stream()
				.map(item -> {
					Long inventoryItemId = item.inventoryItemId();
				    Long productId = item.productId();
				    Long storageId = item.storageId();
				    BigDecimal usedCapacity = item.usedCapacity();
				    return new InventoryItemStorageCapacityResponse(inventoryItemId, productId, storageId, usedCapacity);
				})
				.toList();
	}

	@Override
	public List<StorageCapacityResponse> fetchAllStorageCapacities() {
		List<StorageCapacityResponse> items = inventoryItemsRepository.fetchAllStorageCapacities();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No ImvemtoryItems found for all storage capacities");
		}
		return items.stream()
				.map(item -> {
					Long storageId = item.storageId();
				    BigDecimal usedCapacity = item.usedCapacity();
				    return new StorageCapacityResponse(storageId, usedCapacity);
				})
				.toList();
	}

	@Override
	public List<StorageItemSummaryResponse> fetchItemQuantitiesPerStorage() {
		List<StorageItemSummaryResponse> items = inventoryItemsRepository.fetchItemQuantitiesPerStorage();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems found for item quantities per storage");
		}
		return items.stream()
				.map(item -> {
					Long storageId = item.storageId();
					BigDecimal totalItemQuantity = item.totalItemQuantity();
					return new StorageItemSummaryResponse(storageId, totalItemQuantity);
				})
				.toList();
	}

	@Override
	public List<StorageCapacityAndInventorySummaryResponseFull> fetchStorageCapacityAndInventorySummary() {
	    List<StorageCapacityAndInventorySummaryResponse> items =
	            inventoryItemsRepository.fetchStorageCapacityAndInventorySummary();
	    if (items.isEmpty()) {
	        throw new NoDataFoundException("No InventoryItems found for storage capacity and inventory summary");
	    }
	    return items.stream()
	            .map(item -> {
	                BigDecimal available = item.capacity().subtract(item.usedCapacity());
	                return new StorageCapacityAndInventorySummaryResponseFull(
	                    item.storageId(),
	                    item.capacity(),
	                    item.usedCapacity(),
	                    available,
	                    item.totalItemQuantity()
	                );
	            })
	            .toList();
	}

	@Override
	public List<StorageCapacityAndInventorySummaryResponseFull> fetchDetailedStorageStats() {
		List<StorageCapacityAndInventorySummaryResponse> items = inventoryItemsRepository.fetchDetailedStorageStats();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems found for detailed storage stats");
		}
		return items.stream()
	            .map(item -> {
	                BigDecimal available = item.capacity().subtract(item.usedCapacity());
	                return new StorageCapacityAndInventorySummaryResponseFull(
	                    item.storageId(),
	                    item.capacity(),
	                    item.usedCapacity(),
	                    available,
	                    item.totalItemQuantity()
	                );
	            })
	            .toList();
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_SupplyId(Long supplyId) {
		validateSupplyId(supplyId);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_SupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's supply id %d, is found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_SupplyQuantity(BigDecimal quantity) {
		validateBigDecimal(quantity);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_SupplyQuantity(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's supply %s, is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_SupplyQuantityGreaterThan(BigDecimal quantity) {
		validateBigDecimal(quantity);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_SupplyQuantityGreaterThan(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's supply quantity greater than %s, is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_SupplyQuantityLessThan(BigDecimal quantity) {
		validateBigDecimalNonNegative(quantity);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_SupplyQuantityLessThan(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems bound for product's supply quantity less than %s, is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_SupplyUpdates(LocalDateTime updates) {
		DateValidator.validateNotNull(updates, "Date updates");
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_SupplyUpdates(updates);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No InventoryItems bound for product's supply updates %s, is found",
					updates.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_SupplyUpdatesBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_SupplyUpdatesBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Inventoryitems bound for product's supply date between %s and %s is found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_SupplyStorageId(Long storageId) {
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_SupplyStorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems with product, for supply storage-id %d. is found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_ShelfId(Long shelfId) {
		validateShelfId(shelfId);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_ShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems with product, for shelf-id %d. is found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_ShelfRowCount(Integer rowCount) {
		validateInteger(rowCount);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_ShelfRowCount(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for product with shelf row-count %d is found", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_ShelfCols(Integer cols) {
		validateInteger(cols);
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_ShelfCols(cols);
		if(items.isEmpty()) {
			String msg = String.format("No InventoryItems for product with shelf cols %d is found", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findInventoryItemsWithoutShelf() {
		List<InventoryItems> items = inventoryItemsRepository.findInventoryItemsWithoutShelf();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems without shelf is found");
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InventoryItemsResponse> findByProduct_ShelfIsNotNull() {
		List<InventoryItems> items = inventoryItemsRepository.findByProduct_ShelfIsNotNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems for product, where shelf is not null, found");
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public InventoryItemsResponse trackInventoryItems(Long id) {
		InventoryItems items = inventoryItemsRepository.trackInventoryItems(id).orElseThrow(() -> new ValidationException("InventoryItems not found with id "+id));		
		return new InventoryItemsResponse(items);
	}

	@Transactional(readOnly = true)
	@Override
	public InventoryItemsResponse trackInventoryItemsByInventory(Long inventoryId) {
		List<InventoryItems> items = inventoryItemsRepository.trackInventoryItemsByInventory(inventoryId);
		if(items.isEmpty()) {
			throw new NoDataFoundException("InventoryItems with inventory-id "+inventoryId+" not found");
		}
		InventoryItems ii = items.get(0);
		return new InventoryItemsResponse(ii);
	}

	@Transactional(readOnly = true)
	@Override
	public InventoryItemsResponse trackInventoryItemsByProduct(Long productId) {
		List<InventoryItems> items = inventoryItemsRepository.trackInventoryItemsByProduct(productId);
		if(items.isEmpty()) {
			throw new NoDataFoundException("InventoryItems with product-id "+productId+" not found");
		}
		InventoryItems ii = items.get(0);
		return new InventoryItemsResponse(ii);
	}

	@Transactional(readOnly = true)
	@Override
	public InventoryItemsResponse confirmInventoryItems(Long id) {
		InventoryItems items = inventoryItemsRepository.findById(id).orElseThrow(() -> new ValidationException("InventoryItems not found with id "+id));
		items.setConfirmed(true);
		items.setStatus(InventoryItemsStatus.CONFIRMED);
		return new InventoryItemsResponse(inventoryItemsRepository.save(items));
	}

	@Transactional
	@Override
	public InventoryItemsResponse cancelInventoryItems(Long id) {
		InventoryItems items = inventoryItemsRepository.findById(id).orElseThrow(() -> new ValidationException("InventoryItems not found with id "+id));
		if(items.getStatus() != InventoryItemsStatus.NEW && items.getStatus() != InventoryItemsStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED inventoryItems can be cancelled");
		}
		items.setStatus(InventoryItemsStatus.CANCELLED);
		return new InventoryItemsResponse(inventoryItemsRepository.save(items));
	}

	@Transactional
	@Override
	public InventoryItemsResponse closeInventoryItems(Long id) {
		InventoryItems items = inventoryItemsRepository.findById(id).orElseThrow(() -> new ValidationException("InventoryItems not found with id "+id));
		if(items.getStatus() != InventoryItemsStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED inventoryItems can be closed");
		}
		items.setStatus(InventoryItemsStatus.CLOSED);;
		return new InventoryItemsResponse(inventoryItemsRepository.save(items));
	}

	@Transactional
	@Override
	public InventoryItemsResponse changeStatus(Long id, InventoryItemsStatus status) {
		InventoryItems items = inventoryItemsRepository.findById(id).orElseThrow(() -> new ValidationException("InventoryItems not found with id "+id));
		validateInventoryItemsStatus(status);
		if(items.getStatus() == InventoryItemsStatus.CONFIRMED) {
			throw new ValidationException("Closed inventoryItems cannot change status");
		}
		if(status == InventoryItemsStatus.CONFIRMED) {
			if(items.getStatus() != InventoryItemsStatus.NEW) {
				throw new ValidationException("Only NEW inventoryItems can be confirmed");
			}
			items.setConfirmed(true);
		}
		items.setStatus(status);;
		return new InventoryItemsResponse(inventoryItemsRepository.save(items));
	}

	@Override
	public List<QuantityByProductStatDTO> countQuantityByProduct() {
		List<QuantityByProductStatDTO> items = inventoryItemsRepository.countQuantityByProduct();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems for product quanitiy, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					BigDecimal quantity = item.getQuantity();
					Long productId = item.getProductId();
					String productName = item.getProductName();
					return new QuantityByProductStatDTO(count, quantity, productId, productName);
				})
				.toList();
	}

	@Override
	public List<ItemConditionByProductStatDTO> countItemConditionByProduct() {
		List<ItemConditionByProductStatDTO> items = inventoryItemsRepository.countItemConditionByProduct();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems for product itemCondition, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					BigDecimal itemCondition = item.getItemCondition();
					Long productId = item.getProductId();
					String productName = item.getProductName();
					return new ItemConditionByProductStatDTO(count, itemCondition, productId, productName);
				})
				.toList();
	}

	@Override
	public BigDecimal getTotalDifferenceByProduct(Long productId) {
		if(productId == null) {
			throw new NoDataFoundException("Product ID must not be null");
		}
		BigDecimal totalDiff = inventoryItemsRepository.getTotalDifferenceByProduct(productId);
		if(totalDiff == null) {
			String msg = String.format("No confirmed inventory items found for product ID %d", productId);
	        throw new NoDataFoundException(msg);
		}
		return totalDiff;
	}

	@Transactional(readOnly = true)
	@Override
	public InventoryStatResponse getInventoryStatistics(InventoryStatRequest request) {
		List<InventoryItems> filteredItems = specification.findByFilters(request);
		if(filteredItems.isEmpty()) {
			return InventoryStatResponse.builder()
					.inventoryId(request.inventoryId())
					.totalItemsCount(0L)
					.totalQuantity(BigDecimal.ZERO)
					.totalItemCondition(BigDecimal.ZERO)
                    .totalDifference(BigDecimal.ZERO)
                    .productStats(List.of())
					.build();
		}
		//racunanje statistike agregacijom
		BigDecimal totalQuantity = filteredItems.stream()
				.map(InventoryItems::getQuantity)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalItemCondition = filteredItems.stream()
				.map(InventoryItems::getItemCondition)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalDifference = filteredItems.stream()
				.map(InventoryItems::getDifference)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return InventoryStatResponse.builder()
				.inventoryId(null)
                .totalItemsCount((long) filteredItems.size())
                .totalQuantity(totalQuantity)
                .totalItemCondition(totalItemCondition)
                .totalDifference(totalDifference)
                .productStats(filteredItems)
                .build();
	}

	@Transactional
	@Override
	public InventoryItemsResponse saveInventoryItems(InventoryItemsRequest request) {
		Product product = validateProduct(request.productId());
		Inventory inventory = validateInventory(request.inventoryId());
		BigDecimal totalDiff = calculateDifference(request.quantity(), request.condition());
		InventoryItems items = InventoryItems.builder()
				.inventory(inventory)
				.product(product)
				.quantity(request.quantity())
				.itemCondition(request.condition())
				.difference(totalDiff)
				.confirmed(request.confirmed())
				.status(request.status())
				.build();
		return new InventoryItemsResponse(inventoryItemsRepository.save(items));
	}
	
	private final AbstractSaveAsService<InventoryItems, InventoryItemsResponse> saveAsHelper = new AbstractSaveAsService<InventoryItems, InventoryItemsResponse>() {
		
		@Override
		protected InventoryItemsResponse toResponse(InventoryItems entity) {
			return new InventoryItemsResponse(entity);
		}
		
		@Override
		protected JpaRepository<InventoryItems, Long> getRepository() {
			return inventoryItemsRepository;
		}
		
		@Override
		protected InventoryItems copyAndOverride(InventoryItems source, Map<String, Object> overrides) {
			BigDecimal quantity = (BigDecimal) overrides.getOrDefault("quantity", source.getQuantity());
		    BigDecimal itemCondition = (BigDecimal) overrides.getOrDefault("itemCondition", source.getItemCondition());
		    BigDecimal totalDiff = calculateDifference(quantity, itemCondition);
		    return InventoryItems.builder()
		            .inventory((Inventory) overrides.getOrDefault("inventory", source.getInventory()))
		            .product((Product) overrides.getOrDefault("product", source.getProduct()))
		            .quantity(quantity)
		            .itemCondition(itemCondition)
		            .difference(totalDiff)
		            .confirmed((Boolean) overrides.getOrDefault("confirmed", source.getConfirmed()))
		            .status((InventoryItemsStatus) overrides.getOrDefault("status", source.getStatus()))
		            .build();
		}
	};

	@Transactional
	@Override
	public InventoryItemsResponse saveAs(InventoryItemsSaveAsRequest request) {
		Inventory inventory = validateInventory(request.inventoryId());
	    Product product = validateProduct(request.productId());
	    Map<String, Object> overrides = new HashMap<>();
	    overrides.put("inventory", inventory);
	    overrides.put("product", product);
	    overrides.put("quantity", request.quantity());
	    overrides.put("itemCondition", request.itemCondition());
	    overrides.put("status", request.status());
	    overrides.put("confirmed", request.confirmed());
	    return saveAsHelper.saveAs(request.sourceId(), overrides);
	}
	
	private final AbstractSaveAllService<InventoryItems, InventoryItemsResponse> saveAllHelper = new AbstractSaveAllService<InventoryItems, InventoryItemsResponse>() {
		
		@Override
		protected Function<InventoryItems, InventoryItemsResponse> toResponse() {
			return InventoryItemsResponse::new;
		}
		
		@Override
		protected JpaRepository<InventoryItems, Long> getRepository() {
			return inventoryItemsRepository;
		}
		
		@Override
		protected void beforeSaveAll(List<InventoryItems> entities) {
		    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		    log.info("User '{}' is saving {} inventory items", currentUser, entities.size());
		}
	};

	@Transactional
	@Override
	public List<InventoryItemsResponse> saveAll(List<InventoryItemsRequest> requests) {
		if (requests == null || requests.isEmpty()) {
	        throw new ValidationException("InventoryItems request list must not be empty.");
	    }
		Map<Long, Inventory> inventoryCache = new HashMap<>();
	    Map<Long, Product> productCache = new HashMap<>();
	    List<InventoryItems> items = requests.stream()
	        .map(item -> {
	            if (item.quantity() == null || item.condition() == null) {
	                throw new ValidationException("Quantity and item condition must not be null for product ID: " + item.productId());
	            }
	            if (item.condition().compareTo(item.quantity()) > 0) {
	                throw new ValidationException("Item condition cannot be greater than quantity for product ID: " + item.productId());
	            }
	            Inventory inventory = inventoryCache.computeIfAbsent(item.inventoryId(), this::validateInventory);
	            Product product = productCache.computeIfAbsent(item.productId(), this::validateProduct);
	            BigDecimal totalDiff = calculateDifference(item.quantity(), item.condition());
	            return InventoryItems.builder()
	                    .inventory(inventory)
	                    .product(product)
	                    .quantity(item.quantity())
	                    .itemCondition(item.condition())
	                    .difference(totalDiff)
	                    .confirmed(item.confirmed())
	                    .status(item.status())
	                    .build();
	        })
	        .toList();
	    return saveAllHelper.saveAll(items);
	}

	@Override
	public List<InventoryItemsResponse> generalSearch(InventoryItemsSearchRequest request) {
		Specification<InventoryItems> spec = InventoryItemsSpecification.fromRequest(request);
		List<InventoryItems> items = inventoryItemsRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No InventoryItems found for given criteria");
		}
		return items.stream().map(inventoryItemsMapper::toResponse).collect(Collectors.toList());
	}

	private BigDecimal calculateDifference(BigDecimal quantity, BigDecimal condition) {
		if (quantity == null || condition == null) {
	        throw new IllegalArgumentException("Quantity and condition must not be null.");
	    }
		if(quantity.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Quantity must be at least 0");
		}
		return quantity.subtract(condition).max(BigDecimal.ZERO);
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

	private Shelf validateShelfId(Long shelfId) {
		if(shelfId == null) {
			throw new ValidationException("Shelf ID must not be null");
		}
		return shelfRepository.findById(shelfId).orElseThrow(() -> new ValidationException("Shelf not found with id "+shelfId));
	}
	
	private Storage validateStorageId(Long storageId) {
		if(storageId == null) {
			throw new ValidationException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
	
	private void validateStorageStatus(StorageStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
	}
	
	private void validateGoodsType(GoodsType type) {
		Optional.ofNullable(type)
			.orElseThrow(() -> new ValidationException("GoodsType type must not be null"));
	}
	
	private void validateStorageType(StorageType storageType) {
		Optional.ofNullable(storageType)
			.orElseThrow(() -> new ValidationException("StorageType storageType must not be null"));
	}
	
	private void validateSupplierType(SupplierType supplierType) {
		Optional.ofNullable(supplierType)
			.orElseThrow(() -> new ValidationException("SupplierType supplierType must not be null"));
	}
	
	private void validateUnitMeasure(UnitMeasure unitMeasure) {
		Optional.ofNullable(unitMeasure)
			.orElseThrow(() -> new ValidationException("UnitMeasure unitMeasure must not be null"));
	}
	
	private void validateInventoryStatus(InventoryStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("InventoryStatus status must not be null"));
	}
	
	private Supply validateSupplyId(Long supplyId) {
		if(supplyId == null) {
			throw new ValidationException("Supply ID must not be null");
		}
		return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
	}
	
	private User validateUserId(Long userId) {
		if(userId == null) {
			throw new ValidationException("User ID must not be null");
		}
		return userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found with id "+userId));
	}

	private void validateInventoryItemsStatus(InventoryItemsStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("InventoryItemsStatus status must not be null"));
	}
}
