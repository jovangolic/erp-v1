package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.BatchMapper;
import com.jovan.erp_v1.model.Batch;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.BatchRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.BatchRequest;
import com.jovan.erp_v1.response.BatchResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchService implements IBatchService {

	private final BatchRepository batchRepository;
	private final BatchMapper batchMapper;
	private final StorageRepository storageRepository;
	private final ShelfRepository shelfRepository;
    private final SupplyRepository supplyRepository;
    private final ProductRepository productRepository;

    @Transactional
	@Override
	public BatchResponse create(BatchRequest request) {
		validateCodeUnique(request.code());
		Product product = validateProductId(request.productId());
		validateInteger(request.quantityProduced());
		DateValidator.validateNotNull(request.productionDate(), "Production-date");
		DateValidator.validateNotInPast(request.expiryDate(), "Expiry-date");
		Batch b = batchMapper.toEntity(request, product);
		Batch saved = batchRepository.save(b);
		return new BatchResponse(saved);
	}

    @Transactional
	@Override
	public BatchResponse update(Long id, BatchRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
    	Batch b = batchRepository.findById(id).orElseThrow(() -> new ValidationException("Batch not found wtih id "+id));
    	Product product = b.getProduct();
    	if(request.productId() != null && (product.getId() == null || !request.productId().equals(b.getId()))) {
    		product = validateProductId(request.productId());
    	}
    	if (request.code() == null || request.code().trim().isEmpty()) {
    	    throw new ValidationException("Code must not be null or empty");
    	}
    	if (!b.getCode().equals(request.code()) && batchRepository.existsByCode(request.code())) {
    	    throw new ValidationException("Batch with code '" + request.code() + "' already exists.");
    	}
    	validateInteger(request.quantityProduced());
		DateValidator.validateNotNull(request.productionDate(), "Production-date");
		DateValidator.validateNotInPast(request.expiryDate(), "Expiry-date");
		batchMapper.toEntityUpdate(b, request, product);
		Batch saved = batchRepository.save(b);
		return new BatchResponse(saved);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!batchRepository.existsById(id)) {
			throw new ValidationException("Batch ID must not be null");
		}
		batchRepository.deleteById(id);
	}

	@Override
	public BatchResponse findOne(Long id) {
		Batch b = batchRepository.findById(id).orElseThrow(() -> new ValidationException("Batch not found wtih id "+id));
		return new BatchResponse(b);
	}

	@Override
	public List<BatchResponse> findAll() {
		List<Batch> items = batchRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Batch list is empty");
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
    public List<BatchResponse> getExpiredBatches() {
        LocalDate today = LocalDate.now();
        return batchMapper.toResponseList(
                batchRepository.findByExpiryDateLessThanEqual(today)
        );
    }

    @Override
    public List<BatchResponse> getActiveBatches() {
        LocalDate today = LocalDate.now();
        return batchMapper.toResponseList(
                batchRepository.findByExpiryDateGreaterThan(today)
        );
    }

    @Override
    public List<BatchResponse> getUpcomingBatches(Integer daysAhead) {
    	validateInteger(daysAhead);
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(daysAhead);
        return batchMapper.toResponseList(
                batchRepository.findByExpiryDateBetween(today, targetDate)
        );
    }

    @Override
    public List<BatchResponse> getBatchesProducedBetween(LocalDate startDate, LocalDate endDate) {
    	DateValidator.validateRange(startDate, endDate);
        return batchMapper.toResponseList(
                batchRepository.findByProductionDateBetween(startDate, endDate)
        );
    }

    @Override
    public List<BatchResponse> getBatchesExpiringBetween(LocalDate startDate, LocalDate endDate) {
    	DateValidator.validateRange(startDate, endDate);
        return batchMapper.toResponseList(
                batchRepository.findByExpiryDateBetween(startDate, endDate)
        );
    }

	@Override
	public boolean existsByCode(String code) {
		validateString(code);
		return batchRepository.existsByCode(code);
	}

	@Override
	public List<BatchResponse> findByCodeContainingIgnoreCase(String code) {
		validateString(code);
		List<Batch> items = batchRepository.findByCodeContainingIgnoreCase(code);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for code %s, found", code);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByQuantityProduced(Integer quantityProduced) {
		validateInteger(quantityProduced);
		List<Batch> items = batchRepository.findByQuantityProduced(quantityProduced);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for quantity-produced %d, found", quantityProduced);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByQuantityProducedGreaterThan(Integer quantityProduced) {
		validateInteger(quantityProduced);
		List<Batch> items = batchRepository.findByQuantityProducedGreaterThan(quantityProduced);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for quantity-produced greater than %d, found", quantityProduced);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByQuantityProducedLessThan(Integer quantityProduced) {
		validateIntegerNonNegative(quantityProduced);
		List<Batch> items = batchRepository.findByQuantityProducedLessThan(quantityProduced);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for quantity-produced less than %d, found", quantityProduced);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDate(LocalDate productionDate) {
		DateValidator.validateNotNull(productionDate, "Production date");
		List<Batch> items = batchRepository.findByProductionDate(productionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for production date %s, found", productionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDateBefore(LocalDate productionDate) {
		DateValidator.validateNotInFuture(productionDate, "Production date-before");
		List<Batch> items = batchRepository.findByProductionDateBefore(productionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for production date before %s, found", productionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDateAfter(LocalDate productionDate) {
		DateValidator.validateNotInPast(productionDate, "Production date-after");
		List<Batch> items = batchRepository.findByProductionDateAfter(productionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch production date after %s, found", productionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDateBetween(LocalDate startDate, LocalDate endDate) {
		DateValidator.validateRange(startDate, endDate);
		List<Batch> items = batchRepository.findByProductionDateBetween(startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for production date between %s and %s, found",
					startDate.format(formatter), endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDate(LocalDate expiryDate) {
		DateValidator.validateNotNull(expiryDate, "Expiry-date");
		List<Batch> items = batchRepository.findByExpiryDate(expiryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for expiry date %s, found", expiryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateBefore(LocalDate expiryDate) {
		DateValidator.validateNotInFuture(expiryDate, "Expiry-date before");
		List<Batch> items = batchRepository.findByExpiryDateBefore(expiryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for expiry date before %s, found", expiryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateAfter(LocalDate expiryDate) {
		DateValidator.validateNotInPast(expiryDate, "Expiry-date-after");
		List<Batch> items = batchRepository.findByExpiryDateAfter(expiryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for expiry date-after %s, found", expiryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateBetween(LocalDate expiryDateStart, LocalDate expiryDateEnd) {
		DateValidator.validateRange(expiryDateStart, expiryDateEnd);
		List<Batch> items = batchRepository.findByExpiryDateBetween(expiryDateStart, expiryDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for expiry date between %s and %s, found",
					expiryDateStart.format(formatter), expiryDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDateEquals(LocalDate today) {
		DateValidator.validateNotNull(today, "Production date");
		List<Batch> items = batchRepository.findByProductionDateEquals(today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for production date equals %s, found", today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateLessThanEqual(LocalDate today) {
		DateValidator.validateNotInFuture(today, "Expiry-date");
		List<Batch> items = batchRepository.findByExpiryDateLessThanEqual(today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for expiry date less than equal %s, found", today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDateGreaterThanEqual(LocalDate today) {
		DateValidator.validateNotInPast(today, "Production-date");
		List<Batch> items = batchRepository.findByProductionDateGreaterThanEqual(today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for production date greater than %s, found", today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateGreaterThanEqual(LocalDate expiryDate) {
		DateValidator.validateNotInPast(expiryDate, "Expiry-date");
		List<Batch> items = batchRepository.findByExpiryDateGreaterThanEqual(expiryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for expiry date greater than %s, found", expiryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDateLessThanEqual(LocalDate productionDate) {
		DateValidator.validateNotInFuture(productionDate, "Productioin-date");
		List<Batch> items = batchRepository.findByProductionDateLessThanEqual(productionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for production date less than %s, found", productionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateGreaterThan(LocalDate today) {
		DateValidator.validateNotInPast(today, "Expiry date");
		List<Batch> items = batchRepository.findByExpiryDateGreaterThan(today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for expiry date greater than %s, found", today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateIsNotNull() {
		List<Batch> items = batchRepository.findByExpiryDateIsNotNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Batch for expiry date is not null, found");
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDateIsNull() {
		List<Batch> items = batchRepository.findByProductionDateIsNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Batch for production date is null, found");
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductionDateIsNotNull() {
		List<Batch> items = batchRepository.findByProductionDateIsNotNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Batach for production-date is not null, found");
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateIsNull() {
		List<Batch> items = batchRepository.findByExpiryDateIsNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Batch for expiry date is null, found");
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByExpiryDateAfterAndProductId(LocalDate date, Long productId) {
		DateValidator.validateNotInPast(date, "Expiry-date");
		validateProductId(productId);
		List<Batch> items = batchRepository.findByExpiryDateAfterAndProductId(date, productId);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for expiry-date-after %s and product-id %d, found",
					date.format(formatter),productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductId(Long productId) {
		validateProductId(productId);
		List<Batch> items = batchRepository.findByProductId(productId);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product-id %d, found", productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductCurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<Batch> items = batchRepository.findByProductCurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product current-quantity %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductCurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<Batch> items = batchRepository.findByProductCurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product current-quantity greater than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductCurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<Batch> items = batchRepository.findByProductCurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product current-quantity less than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductCurrentQuantityBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<Batch> items = batchRepository.findByProductCurrentQuantityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product current quantity between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductIdAndExpiryDateLessThanEqual(Long productId, LocalDate today) {
		validateProductId(productId);
		DateValidator.validateNotInPast(today, "Today's date");
		List<Batch> items = batchRepository.findByProductIdAndExpiryDateLessThanEqual(productId, today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for product-id %d and expiry date less than eqaul %s, found", 
					productId,today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductIdAndProductionDateAfter(Long productId, LocalDate date) {
		validateProductId(productId);
		DateValidator.validateNotInPast(date, "Production date");
		List<Batch> items = batchRepository.findByProductIdAndProductionDateAfter(productId, date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for product-id %d and production date-after %s, found",
					productId,date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProductIdAndExpiryDateBetween(Long productId, LocalDate startDate,
			LocalDate endDate) {
		validateProductId(productId);
		DateValidator.validateRange(startDate, endDate);
		List<Batch> items = batchRepository.findByProductIdAndExpiryDateBetween(productId, startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for product-id %d and expirey date between %s and %s, found", 
					productId,startDate.format(formatter), endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_NameContainingIgnoreCase(String productName) {
		validateString(productName);
		List<Batch> items = batchRepository.findByProduct_NameContainingIgnoreCase(productName);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's name %s, found", productName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<Batch> items = batchRepository.findByProduct_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's unit-measure %s, found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<Batch> items = batchRepository.findByProduct_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's supplier-type %s, found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<Batch> items = batchRepository.findByProduct_StorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's storage-type %s, found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_GoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<Batch> items = batchRepository.findByProduct_GoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's goods-type %s, found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StorageId(Long storageId) {
		validateStorageId(storageId);
		List<Batch> items = batchRepository.findByProduct_StorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's storage-id %d, found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StorageNameContainingIgnoreCase(String storageName) {
		validateString(storageName);
		List<Batch> items = batchRepository.findByProduct_StorageNameContainingIgnoreCase(storageName);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's storage-location %s, found", storageName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StorageLocationContainingIgnoreCase(String storageLocation) {
		validateString(storageLocation);
		List<Batch> items = batchRepository.findByProduct_StorageNameContainingIgnoreCase(storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's storage-location %s, found", storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StorageCapacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<Batch> items = batchRepository.findByProduct_StorageCapacity(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's storage capacity %s, found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StorageCapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<Batch> items = batchRepository.findByProduct_StorageCapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's storage capacity greater than %s, found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StorageCapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<Batch> items = batchRepository.findByProduct_StorageCapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's storage capacity less than %s, found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StorageStatus(StorageStatus status) {
		validateStorageStatus(status);
		List<Batch> items = batchRepository.findByProduct_StorageStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's storage-status %s, found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_StoragehasShelvesForIsNull() {
		List<Batch> items = batchRepository.findByProduct_StoragehasShelvesForIsNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Batch for storage without shelves, found");
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_SupplyId(Long supplyId) {
		validateSupplyId(supplyId);
		List<Batch> items = batchRepository.findByProduct_SupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's supply-id %d, found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_ShelfId(Long shelfId) {
		validateShelfId(shelfId);
		List<Batch> items = batchRepository.findByProduct_ShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's shelf-id %d, found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_ShelfRowCount(Integer rowCount) {
		validateInteger(rowCount);
		List<Batch> items = batchRepository.findByProduct_ShelfRowCount(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's shelf row %d, found", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByProduct_ShelfCols(Integer cols) {
		validateInteger(cols);
		List<Batch> items = batchRepository.findByProduct_ShelfCols(cols);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for product's shelf cols %d, found", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public boolean existsByRowCountAndStorageId(Integer rows, Long storageId) {
		validateInteger(rows);
		validateStorageId(storageId);
		return batchRepository.existsByRowCountAndStorageId(rows, storageId);
	}

	@Override
	public boolean existsByColsAndStorageId(Integer cols, Long storageId) {
		validateInteger(cols);
		validateStorageId(storageId);
		return batchRepository.existsByColsAndStorageId(cols, storageId);
	}

	@Override
	public boolean existsByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId) {
		validateInteger(rows);
		validateInteger(cols);
		validateStorageId(storageId);
		return batchRepository.existsByRowCountAndColsAndStorageId(rows, cols, storageId);
	}

	@Override
	public BatchResponse findByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId) {
		validateInteger(rows);
		validateInteger(cols);
		validateStorageId(storageId);
		Batch b = batchRepository.findByRowCountAndColsAndStorageId(rows, cols, storageId).orElseThrow(() -> new ValidationException("Batch not found"));
		return new BatchResponse(b);
	}

	@Override
	public List<BatchResponse> findByRowCountAndStorageId(Integer rows, Long storageId) {
		validateInteger(rows);
		validateStorageId(storageId);
		List<Batch> items = batchRepository.findByRowCountAndStorageId(rows, storageId);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for shelf rows %d and storage-id %d, found", rows,storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<BatchResponse> findByColsAndStorageId(Integer cols, Long storageId) {
		validateInteger(cols);
		validateStorageId(storageId);
		List<Batch> items = batchRepository.findByColsAndStorageId(cols, storageId);
		if(items.isEmpty()) {
			String msg = String.format("No Batch for shelf cols %d and storage-id %d, found", cols,storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(batchMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateInteger(Integer num) {
		if(num == null || num <= 0) {
			throw new ValidationException("Number must be positive");
		}
	}
	
	private void validateStorageStatus(StorageStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
    }

    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new ValidationException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }

    private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new ValidationException("Min i Max ne smeju biti null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Min mora biti >= 0, a Max mora biti > 0");
        }

        if (min.compareTo(max) > 0) {
            throw new ValidationException("Min ne može biti veći od Max");
        }
    }

    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Mora biti pozitivan broj");
        }
    }

    private void validateStorageType(StorageType type) {
        if (type == null) {
            throw new ValidationException("Unit za StorageType ne sme biti null");
        }
    }
    
    private void validateUnitMeasure(UnitMeasure unitMeasure) {
    	Optional.ofNullable(unitMeasure)
    		.orElseThrow(() -> new ValidationException("UnitMeasure unitMeasure must not be null"));
    }
    
    private void validateSupplierType(SupplierType supplierType) {
    	Optional.ofNullable(supplierType)
    		.orElseThrow(() -> new ValidationException("SupplierType supplierType must not be null"));
    }
    
    private void validateGoodsType(GoodsType goodsType) {
    	Optional.ofNullable(goodsType)
    		.orElseThrow(() -> new ValidationException("GoodsType goodsType must not be null"));
    }
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
    
    private void validateIntegerNonNegative(Integer num) {
    	if (num == null || num < 0) {
    	    throw new ValidationException("Number must be zero or positive");
    	}
    }
    
    private Shelf validateShelfId(Long shelfId) {
    	if(shelfId == null) {
    		throw new ValidationException("Shelf ID must not be null");
    	}
    	return shelfRepository.findById(shelfId).orElseThrow(() -> new ValidationException("Shelf not found with id "+shelfId));
    }
    
    private Product validateProductId(Long productId) {
    	if(productId == null) {
    		throw new ValidationException("Product ID must not be null");
    	}
    	return productRepository.findById(productId).orElseThrow(() -> new ValidationException("Product not found with id "+productId));
    }
    
    private Storage validateStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new ValidationException("Storage ID must not be null");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
    }
    
    private Supply validateSupplyId(Long supplyId) {
    	if(supplyId == null) {
    		throw new ValidationException("Supply ID must not be null");
    	}
    	return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
    }
    
    private void validateCodeUnique(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new ValidationException("Code must not be null or empty");
        }
        if (batchRepository.existsByCode(code)) {
            throw new ValidationException("Batch with code '" + code + "' already exists.");
        }
    }
}
