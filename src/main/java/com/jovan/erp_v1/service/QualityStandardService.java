package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.QualityStandardMapper;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.QualityStandard;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.QualityStandardRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.specification.QualityStandardSpec;
import com.jovan.erp_v1.request.QualityStandardRequest;
import com.jovan.erp_v1.response.QualityStandardResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QualityStandardService implements IQualityStandardService {

	private final QualityStandardRepository qualityStandardRepository;
	private final QualityStandardMapper qualityStandardMapper;
	private final StorageRepository storageRepository;
	private final ProductRepository productRepository;
	private final ShelfRepository shelfRepository;
	private final SupplyRepository supplyRepository;

	@Transactional
	@Override
	public QualityStandardResponse create(QualityStandardRequest request) {
		Product product = validateProductId(request.productId());
		validateString(request.description());
		validateMinAndMax(request.minValue(), request.maxValue());
		validateUnit(request.unit());
		QualityStandard qs = qualityStandardMapper.toEntity(request, product);
		QualityStandard  saved = qualityStandardRepository.save(qs);
		return new QualityStandardResponse(saved);
	}

	@Transactional
	@Override
	public QualityStandardResponse update(Long id, QualityStandardRequest request) {
		if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		QualityStandard qs = qualityStandardRepository.findById(id).orElseThrow(() -> new ValidationException("QualityStandard not found with id "+id));
		Product product = qs.getProduct();
		if(request.productId() != null && (product.getId() == null || !request.productId().equals(product.getId()))) {
			product = validateProductId(request.productId());
		}
		validateString(request.description());
		validateMinAndMax(request.minValue(), request.maxValue());
		validateUnit(request.unit());
		qualityStandardMapper.toEntityUpdate(qs, request, product);
		QualityStandard saved = qualityStandardRepository.save(qs);
		return new QualityStandardResponse(saved);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!qualityStandardRepository.existsById(id)) {
			throw new ValidationException("QualityStandard not found with id "+id);
		}
		qualityStandardRepository.deleteById(id);
	}

	@Override
	public QualityStandardResponse findOne(Long id) {
		QualityStandard qs = qualityStandardRepository.findById(id).orElseThrow(() -> new ValidationException("QualityStandard not found with id "+id));
		return new QualityStandardResponse(qs);
	}

	@Override
	public List<QualityStandardResponse> findAll() {
		List<QualityStandard> items = qualityStandardRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("QualityStandard list is empty");
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByUnit(Unit unit) {
		validateUnit(unit);
		List<QualityStandard> items = qualityStandardRepository.findByUnit(unit);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for unit %s, found", unit);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByDescriptionContainingIgnoreCase(String description) {
		validateString(description);
		List<QualityStandard> items = qualityStandardRepository.findByDescriptionContainingIgnoreCase(description);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for description %s, found", description);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByMinValue(BigDecimal minValue) {
		validateBigDecimal(minValue);
		List<QualityStandard> items = qualityStandardRepository.findByMinValue(minValue);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for min-value %s, found", minValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByMinValueGreaterThan(BigDecimal minValue) {
		validateBigDecimal(minValue);
		List<QualityStandard> items = qualityStandardRepository.findByMinValueGreaterThan(minValue);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for min-value greater than %s, found", minValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByMinValueLessThan(BigDecimal minValue) {
		validateBigDecimalNonNegative(minValue);
		List<QualityStandard> items = qualityStandardRepository.findByMinValueLessThan(minValue);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for min-value less than %s, found", minValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByMinValueBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<QualityStandard> items = qualityStandardRepository.findByMinValueBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for min-value between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByMaxValue(BigDecimal maxValue) {
		validateBigDecimal(maxValue);
		List<QualityStandard> items = qualityStandardRepository.findByMaxValue(maxValue);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for max-value %s, found", maxValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByMaxValueGreaterThan(BigDecimal maxValue) {
		validateBigDecimal(maxValue);
		List<QualityStandard> items = qualityStandardRepository.findByMaxValueGreaterThan(maxValue);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for max-value greater than %s, found", maxValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByMaxValueLessThan(BigDecimal maxValue) {
		validateBigDecimalNonNegative(maxValue);
		List<QualityStandard> items = qualityStandardRepository.findByMaxValueLessThan(maxValue);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for max-value less than %s, found", maxValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByMaxValueBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<QualityStandard> items = qualityStandardRepository.findByMaxValueBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for max-value between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public long countByMinValueIsNotNull() {
		return qualityStandardRepository.countByMinValueIsNotNull();
	}

	@Override
	public long countByMaxValueIsNotNull() {
		return qualityStandardRepository.countByMaxValueIsNotNull();
	}

	@Override
	public long countByMinValue(BigDecimal minValue) {
		validateBigDecimalNonNegative(minValue);
		return qualityStandardRepository.countByMinValue(minValue);
	}

	@Override
	public long countByMaxValue(BigDecimal maxValue) {
		validateBigDecimal(maxValue);
		return qualityStandardRepository.countByMaxValue(maxValue);
	}

	@Override
	public List<QualityStandardResponse> findByProduct_Id(Long productId) {
		validateProductId(productId);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_Id(productId);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product-id %d, found", productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_CurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's current-quantity %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's current-quantity greater than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's current quantity less than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_CurrentQuantityBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_CurrentQuantityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's current capacity between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_NameContainingIgnoreCase(String productName) {
		validateString(productName);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_NameContainingIgnoreCase(productName);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's name %s, found", productName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's unit-measure %s, found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's supplier-type %s, found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-type %s, found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_GoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_GoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's goods-type %s, found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageId(Long storageId) {
		validateStorageId(storageId);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-id %d, found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCase(String storageName) {
		validateString(storageName);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageNameContainingIgnoreCase(storageName);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-name %s, found", storageName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCase(String storageLocation) {
		validateString(storageLocation);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageLocationContainingIgnoreCase(storageLocation);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-location %s, found", storageLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageCapacity(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageCapacity(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-capacity %s, found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageCapacityGreaterThan(BigDecimal capacity) {
		validateBigDecimal(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageCapacityGreaterThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-capacity greater than 5s, found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageCapacityLessThan(BigDecimal capacity) {
		validateBigDecimalNonNegative(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageCapacityLessThan(capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-capacity less than %s, found", capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageCapacityBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageCapacityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage capacity between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProductStorageCapacityBetweenAndStatus(BigDecimal min, BigDecimal max,
			StorageStatus status) {
		validateMinAndMax(min, max);
		validateStorageStatus(status);
		List<QualityStandard> items = qualityStandardRepository.findByProductStorageCapacityBetweenAndStatus(min, max, status);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage capacity between %s and %s, and storage-status %s, found", 
					min,max,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProductStorageCapacityStatusAndType(BigDecimal min, BigDecimal max,
			StorageStatus status, StorageType type) {
		validateMinAndMax(min, max);
		validateStorageStatus(status);
		validateStorageType(type);
		List<QualityStandard> items = qualityStandardRepository.findByProductStorageCapacityStatusAndType(min, max, status, type);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage capacity between %s and %s, storage-status %s, storage-type %s, found",
					min,max,status,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCaseAndCapacity(String storageName,
			BigDecimal capacity) {
		validateString(storageName);
		validateBigDecimal(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageNameContainingIgnoreCaseAndCapacity(storageName, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-name %s and capacity %s, found", storageName,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(
			String storageName, BigDecimal capacity) {
		validateString(storageName);
		validateBigDecimal(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(storageName, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-name %s and capacity greater than %s, found", storageName,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCaseAndCapacityLessThan(
			String storageName, BigDecimal capacity) {
		validateString(storageName);
		validateBigDecimal(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageNameContainingIgnoreCaseAndCapacityLessThan(storageName, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-name %s and capacity less than %s, found", storageName,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCaseAndCapacityBetween(
			String storageName, BigDecimal min, BigDecimal max) {
		validateString(storageName);
		validateMinAndMax(min, max);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageNameContainingIgnoreCaseAndCapacityBetween(storageName, min, max);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-name %s and capacity between %s and %s, found", 
					storageName,min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCaseAndCapacity(
			String storageLocation, BigDecimal capacity) {
		validateString(storageLocation);
		validateBigDecimal(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageLocationContainingIgnoreCaseAndCapacity(storageLocation, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-location %s and capacity %s, found", 
					storageLocation,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(
			String storageLocation, BigDecimal capacity) {
		validateString(storageLocation);
		validateBigDecimal(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(storageLocation, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-location %s and capacity greater than %s, found",
					storageLocation,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityLessThan(
			String storageLocation, BigDecimal capacity) {
		validateString(storageLocation);
		validateBigDecimalNonNegative(capacity);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageLocationContainingIgnoreCaseAndCapacityLessThan(storageLocation, capacity);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-location %s and capacity less than %s ,found", 
					storageLocation,capacity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityBetween(
			String storageLocation, BigDecimal min, BigDecimal max) {
		validateString(storageLocation);
		validateMinAndMax(min, max);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_StorageLocationContainingIgnoreCaseAndCapacityBetween(storageLocation, min, max);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's storage-location %s and capacity between %s and %s, found",
					storageLocation, min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public QualityStandardResponse findByProduct_StorageHasShelvesForIsNull() {
		QualityStandard qs = qualityStandardRepository.findByProduct_StorageHasShelvesForIsNull().orElseThrow(() -> new ValidationException("Storage without shelves, not found"));
		return new QualityStandardResponse(qs);
	}

	@Override
	public List<QualityStandardResponse> findByProduct_SupplyId(Long supplyId) {
		validateSupplyId(supplyId);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_SupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's supply-id %d, found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_ShelfId(Long shelfId) {
		validateShelfId(shelfId);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_ShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's shelf-id %d, found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_ShelfRowCount(Integer rowCount) {
		validateInteger(rowCount);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_ShelfRowCount(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's shelf-row %d, found", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_ShelfCols(Integer cols) {
		validateInteger(cols);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_ShelfCols(cols);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's shelf-cols %d, found", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_ShelfRowAndColNullable(Integer row, Integer col) {
		validateInteger(row);
		validateInteger(col);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_ShelfRowAndColNullable(row, col);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's shelf row %d and col %s, found", row,col);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> findByProduct_ShelfRowAndColBetweenNullable(Integer rowMin, Integer rowMax,
			Integer colMin, Integer colMax) {
		validateMinAndMaxInteger(rowMin, rowMax);
		validateMinAndMaxInteger(colMin, colMax);
		List<QualityStandard> items = qualityStandardRepository.findByProduct_ShelfRowAndColBetweenNullable(rowMin, rowMax, colMin, colMax);
		if(items.isEmpty()) {
			String msg = String.format("No QualityStandard for product's shelf row between %d and %d, and shelf cols between %d and %d, found", 
					rowMin,rowMin, colMin,colMax);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QualityStandardResponse> searchQualityStandards(Long supplyId, BigDecimal productStorageMinCapacity,
			BigDecimal productStorageMaxCapacity, BigDecimal supplyMinQuantity, BigDecimal supplyMaxQuantity,
			Integer shelfRow, Integer shelfCol) {
		Specification<QualityStandard> spec = Specification.where(null);
        spec = spec.and(QualityStandardSpec.hasSupplyId(supplyId));
        spec = spec.and(QualityStandardSpec.hasProductStorageCapacityBetween(
                productStorageMinCapacity, productStorageMaxCapacity));
        spec = spec.and(QualityStandardSpec.hasSupplyCapacityBetween(
                supplyMinQuantity, supplyMaxQuantity));
        spec = spec.and(QualityStandardSpec.hasShelfRowAndCol(shelfRow, shelfCol));

        List<QualityStandard> items = qualityStandardRepository.findAll(spec);
        return items.stream().map(qualityStandardMapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateUnit(Unit unit) {
		Optional.ofNullable(unit)
			.orElseThrow(() -> new ValidationException("Unit unit must not be null"));
	}
	
	private void validateUnitMeasure(UnitMeasure unitMeasure) {
		Optional.ofNullable(unitMeasure)
			.orElseThrow(() -> new ValidationException("UnitMeasure unitMeasure must not be null"));
	}
	
	private void validateSupplierType(SupplierType supplierType) {
		Optional.ofNullable(supplierType)
			.orElseThrow(() -> new ValidationException("SupplierType supplierType must not be null"));
	}
	
	private void validateStorageType(StorageType storageType) {
		Optional.ofNullable(storageType)
			.orElseThrow(() -> new ValidationException("StorageType storageType"));
	}
	
	private void validateGoodsType(GoodsType goodsType) {
		Optional.ofNullable(goodsType)
			.orElseThrow(() -> new ValidationException("GoodsType goodsType must not be null"));
	}
	
	private void validateStorageStatus(StorageStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
	}
	
	private Storage validateStorageId(Long storageId) {
		if(storageId == null) {
			throw new ValidationException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
	}
	
	private Product validateProductId(Long productId) {
		if(productId == null) {
			throw new ValidationException("Product ID must not be null");
		}
		return productRepository.findById(productId).orElseThrow(() -> new ValidationException("Product not found with id "+productId));
	}
	
	private Shelf validateShelfId(Long shelfId) {
		if(shelfId == null) {
			throw new ValidationException("Shelf ID must not be null");
		}
		return shelfRepository.findById(shelfId).orElseThrow(() -> new ValidationException("Shelf not found with id "+shelfId));
	}
	
	private Supply validateSupplyId(Long supplyId) {
		if(supplyId == null) {
			throw new ValidationException("Supply ID must not be null");
		}
		return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("Given characters must not be null nor empty");
		}
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
	
	private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }
	
	private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Min i Max ne smeju biti null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Min mora biti >= 0, a Max mora biti > 0");
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Min ne može biti veći od Max");
        }
    }
	
	private void validateMinAndMaxInteger(Integer min, Integer max) {
		if(min == null || max == null) {
			throw new ValidationException("Both min and max must not be null.");
		}
		if(min < 0) {
			throw new ValidationException("Min must not be negative");
		}
		if(max <= 0) {
			throw new ValidationException("Max  must be greater than zero");
		}
		if(min > max) {
			throw new ValidationException("Min must not be greater than Max");
		}
	}
	
	private void validateInteger(Integer num) {
		if(num == null || num <= 0) {
			throw new ValidationException("Number must be positive");
		}
	}

	
}
