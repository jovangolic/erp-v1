package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.ProductionOrderErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.ProductionOrderMapper;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ProductionOrderRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.ProductionOrderRequest;
import com.jovan.erp_v1.response.ProductionOrderResponse;
import com.jovan.erp_v1.util.DateValidator;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionOrderService implements IProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final ProductionOrderMapper productionOrderMapper;
    private final WorkCenterRepository workCenterRepository;
    private final ProductRepository productRepository;
    private final ShelfRepository shelfRepository;
    private final StorageRepository storageRepository;
    private final SupplyRepository supplyRepository;

    @Transactional
    @Override
    public ProductionOrderResponse create(ProductionOrderRequest request) {
        validateProductionOrderRequest(request);
        Product product = fetchProduct(request.productId());
        WorkCenter wc = fetchWorkCenter(request.workCenterId());
        ProductionOrder order = productionOrderMapper.toEntity(request,product,wc);
        ProductionOrder saved = productionOrderRepository.save(order);
        return productionOrderMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public ProductionOrderResponse update(Long id, ProductionOrderRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        ProductionOrder o = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ProductionOrderErrorException("ProductionOrder not found with id " + id));
        validateProductionOrderRequest(request);
        Product product = o.getProduct();
        if(request.productId() != null && (product.getId() == null || !request.productId().equals(product.getId()))) {
        	product = fetchProduct(request.productId());
        }
        WorkCenter wc = o.getWorkCenter();
        if(request.workCenterId() != null && (wc.getId() == null || !request.workCenterId().equals(wc.getId()))) {
        	wc = fetchWorkCenter(request.workCenterId());
        }
        productionOrderMapper.toUpdateEntity(o, request,product,wc);
        return productionOrderMapper.toResponse(productionOrderRepository.save(o));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!productionOrderRepository.existsById(id)) {
            throw new ProductionOrderErrorException("ProductionOrder not found with id " + id);
        }
        productionOrderRepository.deleteById(id);
    }

    @Override
    public ProductionOrderResponse findOne(Long id) {
        ProductionOrder o = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ProductionOrderErrorException("ProductionOrder not found with id " + id));
        return new ProductionOrderResponse(o);
    }

    @Override
    public List<ProductionOrderResponse> findAll() {
        return productionOrderRepository.findAll().stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProductionOrderResponse findByOrderNumber(String orderNumber) {
    	validateOrderNumberExists(orderNumber);
        ProductionOrder o = productionOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ProductionOrderErrorException("OrderNumber not found for ProductionOrder"));
        return new ProductionOrderResponse(o);
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_Id(Long productId) {
    	fetchProduct(productId);
        return productionOrderRepository.findByProduct_Id(productId).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_NameContainingIgnoreCase(String name) {
    	validateString(name);
        return productionOrderRepository.findByProduct_NameContainingIgnoreCase(name).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity) {
    	validateBigDecimal(currentQuantity);
        return productionOrderRepository.findByProduct_CurrentQuantity(currentQuantity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByStatus(ProductionOrderStatus status) {
    	validateProductionOrderStatus(status);
        return productionOrderRepository.findByStatus(status).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_Id(Long workCenterId) {
    	fetchWorkCenter(workCenterId);
        return productionOrderRepository.findByWorkCenter_Id(workCenterId).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_NameContainingIgnoreCase(String name) {
    	validateString(name);
        return productionOrderRepository.findByWorkCenter_NameContainingIgnoreCase(name).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_LocationContainingIgnoreCase(String location) {
    	validateString(location);
        return productionOrderRepository.findByWorkCenter_LocationContainingIgnoreCase(location).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_Capacity(Integer capacity) {
    	validateInteger(capacity);
        return productionOrderRepository.findByWorkCenter_Capacity(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_CapacityGreaterThan(Integer capacity) {
    	validateInteger(capacity);
        return productionOrderRepository.findByWorkCenter_CapacityGreaterThan(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByWorkCenter_CapacityLessThan(Integer capacity) {
    	validateInteger(capacity);
        return productionOrderRepository.findByWorkCenter_CapacityLessThan(capacity).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByQuantityPlanned(Integer quantityPlanned) {
    	validateInteger(quantityPlanned);
        return productionOrderRepository.findByQuantityPlanned(quantityPlanned).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByQuantityProduced(Integer quantityProduced) {
    	validateInteger(quantityProduced);
        return productionOrderRepository.findByQuantityProduced(quantityProduced).stream()
                .map(ProductionOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionOrderResponse> findByStartDateBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and End dates must be provided");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("StartDate must not be after end date");
        }
        List<ProductionOrder> orders = productionOrderRepository.findByStartDateBetween(start, end);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> findByStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("StartDate must be provided");
        }
        List<ProductionOrder> orders = productionOrderRepository.findByStartDate(startDate);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> findByEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("EndDate must be provided");
        }
        List<ProductionOrder> orders = productionOrderRepository.findByEndDate(endDate);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> findByStartDateGreaterThanEqual(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("StartDate must be provided");
        }
        List<ProductionOrder> orders = productionOrderRepository.findByStartDateGreaterThanEqual(startDate);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> findOrdersWithStartDateAfterOrEqual(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("StartDate must be provided");
        }
        List<ProductionOrder> orders = productionOrderRepository.findOrdersWithStartDateAfterOrEqual(startDate);
        return productionOrderMapper.toResponseList(orders);
    }

    @Override
    public List<ProductionOrderResponse> searchOrders(
            String productName,
            String workCenterName,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            ProductionOrderStatus status) {
        List<ProductionOrder> filteredOrders = productionOrderRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productName != null && !productName.isBlank()) {
                predicates
                        .add(cb.like(cb.lower(root.get("product").get("name")), "%" + productName.toLowerCase() + "%"));
            }
            if (workCenterName != null && !workCenterName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("workCenter").get("name")),
                        "%" + workCenterName.toLowerCase() + "%"));
            }
            if (startDateFrom != null && startDateTo != null) {
                predicates.add(cb.between(root.get("startDate"), startDateFrom, startDateTo));
            } else if (startDateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDateFrom));
            } else if (startDateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), startDateTo));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
        return filteredOrders.stream().map(ProductionOrderResponse::new).collect(Collectors.toList());
    }
    
    @Override
	public BigDecimal countAvailableCapacity(Long storageId) {
    	Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        return storage.countAvailableCapacity();
	}

	@Override
	public boolean hasCapacityFor(Long storageId, BigDecimal amount) {
		Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        return storage.hasCapacityFor(amount);
	}

	@Override
	public void allocateCapacity(Long storageId, BigDecimal amount) {
		Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        storage.allocateCapacity(amount);
        storageRepository.save(storage); 
	}

	@Override
	public void releaseCapacity(Long storageId, BigDecimal amount) {
		Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ValidationException("Storage not found"));
        storage.releaseCapacity(amount);
        storageRepository.save(storage);
	}
	
	@Override
	public List<ProductionOrderResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product current-quantity greater than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product current-quantity less than %s is found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product unit-measure %s is found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product supplier-type %s is found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_StorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_StorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product storage-type %s is found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_StorageId(Long storageId) {
		fetchStorageId(storageId);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_StorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product storage-id %d is found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_StorageNameContainingIgnoreCase(String storageName) {
		validateString(storageName);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_StorageNameContainingIgnoreCase(storageName);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product storage-name %s is found", storageName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_StorageLocationContainingIgnoreCase(String storageLocation) {
		validateString(storageLocation);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_StorageLocationContainingIgnoreCase(storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product storage-location %s is found", storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_StorageCapacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_StorageCapacity(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product bound to storage capacity %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_StorageCapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_StorageCapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product bound to storage capacity greater than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_StorageCapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_StorageCapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product bound to storage capacity less than %s is found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_SupplyId(Long supplyId) {
		fetchSupplyId(supplyId);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_SupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product bound to supply-id %d is found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_SupplyQuantity(BigDecimal quantity) {
		validateBigDecimal(quantity);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_SupplyQuantity(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product boubd to supply quantity %s is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_SupplyQuantityGreaterThan(BigDecimal quantity) {
		validateBigDecimal(quantity);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_SupplyQuantityGreaterThan(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product boubd to supply quantity greater than %s is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_SupplyQuantityLessThan(BigDecimal quantity) {
		validateBigDecimalNonNegative(quantity);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_SupplyQuantityLessThan(quantity);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product boubd to supply quantity less than %s is found", quantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_SupplyUpdates(LocalDateTime updates) {
		DateValidator.validateNotNull(updates, "Date updates");
		List<ProductionOrder> items = productionOrderRepository.findByProduct_SupplyUpdates(updates);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No ProductionOrder for product bound to supply date %s is found", updates.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_SupplyUpdatesBetween(LocalDateTime updatesStart,
			LocalDateTime updatesEnd) {
		DateValidator.validateRange(updatesStart, updatesEnd);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_SupplyUpdatesBetween(updatesStart, updatesEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No ProductionOrder for product bound to supply date between %s and %s is found", 
					updatesStart.format(formatter), updatesEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_ShelfId(Long shelfId) {
		fetchShelfId(shelfId);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_ShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product with shelf-id %d is found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_ShelfRowCount(Integer rowCount) {
		validateInteger(rowCount);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_ShelfRowCount(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product with shelf row %d is found", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_ShelfCols(Integer cols) {
		validateInteger(cols);
		List<ProductionOrder> items = productionOrderRepository.findByProduct_ShelfCols(cols);
		if(items.isEmpty()) {
			String msg = String.format("No ProductionOrder for product shelf cols %d is found", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ProductionOrderResponse> findByProduct_ShelfIsNull() {
		List<ProductionOrder> items = productionOrderRepository.findByProduct_ShelfIsNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No ProductionOrder for product where shelf doesn't exist, is found");
		}
		return items.stream().map(productionOrderMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
    
    private void validateInteger(Integer num) {
    	if(num == null || num < 0) {
    		throw new IllegalArgumentException("Number must be positive");
    	}
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new IllegalArgumentException("String must not be null nor empty");
    	}
    }
    
    private void validateProductionOrderStatus(ProductionOrderStatus status) {
    	if(status == null) {
    		throw new IllegalArgumentException("ProductionOrderStatus status must not be null");
    	}
    }
    
    private WorkCenter fetchWorkCenter(Long workCenterId) {
    	if(workCenterId == null) {
    		throw new WorkCenterErrorException("WorkCenter ID must not be null");
    	}
    	return workCenterRepository.findById(workCenterId).orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id "+workCenterId));
    }
    
    private Product fetchProduct(Long productId) {
    	if(productId == null) {
    		throw new ProductNotFoundException("Product ID must not be null");
    	}
    	return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id "+productId));
    }
    
    private Storage fetchStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new ValidationException("Storage ID must not be null");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
    }
    
    private Shelf fetchShelfId(Long shelfId) {
    	if(shelfId == null) {
    		throw new ValidationException("Shelf ID must not be null");
    	}
    	return shelfRepository.findById(shelfId).orElseThrow(() -> new ValidationException("Shelf not found with id "+shelfId));
    }
    
    private Supply fetchSupplyId(Long supplyId) {
    	if(supplyId == null) {
    		throw new ValidationException("Supply ID must not be null");
    	}
    	return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
    }
    
    private void validateOrderNumberExists(String orderNumber) {
    	if(orderNumber == null) {
    		throw new IllegalArgumentException("OrderNumber must not be null");
    	}
    	if(!productionOrderRepository.existsByOrderNumber(orderNumber)) {
    		throw new ProductionOrderErrorException("OrderNumber not found "+orderNumber);
    	}
    }
    
    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new IllegalArgumentException("Number must be positive");
    	}
    }
    
    private void validateProductionOrderRequest(ProductionOrderRequest request) {
    	validateOrderNumberExists(request.orderNumber());
    	validateInteger(request.quantityPlanned());
    	validateInteger(request.quantityProduced());
    	DateValidator.validateRange(request.startDate(), request.endDate());
    	validateProductionOrderStatus(request.status());
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
}
