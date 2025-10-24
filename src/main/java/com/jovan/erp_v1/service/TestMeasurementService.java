package com.jovan.erp_v1.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TestMeasurementMapper;
import com.jovan.erp_v1.model.Batch;
import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.QualityCheck;
import com.jovan.erp_v1.model.QualityStandard;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.model.TestMeasurement;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BatchRepository;
import com.jovan.erp_v1.repository.InspectionRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.QualityCheckRepository;
import com.jovan.erp_v1.repository.QualityStandardRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.TestMeasurementRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.specification.TestMeasurementSpecification;
import com.jovan.erp_v1.request.TestMeasurementRequest;
import com.jovan.erp_v1.response.TestMeasurementResponse;
import com.jovan.erp_v1.util.DateValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestMeasurementService implements ITestMeasurementService {

	private final TestMeasurementRepository testMeasurementRepository;
	private final StorageRepository storageRepository;
	private final ShelfRepository shelfRepository;
	private final SupplyRepository supplyRepository;
	private final ProductRepository productRepository;
	private final TestMeasurementMapper testMeasurementMapper;
	private final QualityStandardRepository qualityStandardRepository;
	private final BatchRepository batchRepository;
	private final InspectionRepository inspectionRepository;
	private final QualityCheckRepository qualityCheckRepository;
	private final UserRepository userRepository;
	
	@Transactional
	@Override
	public TestMeasurementResponse create(TestMeasurementRequest request) {
		Inspection inspection = validateInspectionId(request.inspectionId());
		QualityStandard qs = validateQualityStandardId(request.qualityStandardId());
		validateBigDecimal(request.measuredValue());
		TestMeasurement tm = testMeasurementMapper.toEntity(request, inspection, qs);
		TestMeasurement saved = testMeasurementRepository.save(tm);
		return new TestMeasurementResponse(saved);
	}

	@Transactional
	@Override
	public TestMeasurementResponse update(Long id, TestMeasurementRequest request) {
		if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		TestMeasurement tm = testMeasurementRepository.findById(id).orElseThrow(() -> new ValidationException("TestMeasurement not found with id "+id));
		Inspection inspection = tm.getInspection();
		if(request.inspectionId() != null && (inspection.getId() == null || !request.inspectionId().equals(inspection.getId()))) {
			inspection = validateInspectionId(request.inspectionId());
		}
		QualityStandard qs = tm.getStandard();
		if(request.qualityStandardId() != null && (qs.getId() == null || !request.qualityStandardId().equals(qs.getId()))) {
			qs = validateQualityStandardId(request.qualityStandardId());
		}
		validateBigDecimal(request.measuredValue());
		testMeasurementMapper.toEntityUpdate(tm, request, inspection, qs);
		TestMeasurement saved = testMeasurementRepository.save(tm);
		return new TestMeasurementResponse(saved);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!testMeasurementRepository.existsById(id)) {
			throw new ValidationException("TestMeasurement ID must not be null");
		}
		testMeasurementRepository.deleteById(id);
	}

	@Override
	public TestMeasurementResponse findOne(Long id) {
		TestMeasurement tm = testMeasurementRepository.findById(id).orElseThrow(() -> new ValidationException("TestMeasurement not found with id "+id));
		return new TestMeasurementResponse(tm);
	}

	@Override
	public List<TestMeasurementResponse> findAll() {
		List<TestMeasurement> items = testMeasurementRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TestMeasurement list is empty");
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<TestMeasurementResponse> search(
            Long inspectionId,
            String productName,
            QualityCheckStatus status,
            BigDecimal minMeasuredValue,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        Specification<TestMeasurement> spec = Specification
                .where(TestMeasurementSpecification.hasInspectionId(inspectionId))
                .and(TestMeasurementSpecification.productNameContains(productName))
                .and(TestMeasurementSpecification.hasQualityCheckStatus(status))
                .and(TestMeasurementSpecification.measuredValueGreaterThan(minMeasuredValue))
                .and(TestMeasurementSpecification.dateBetween(startDate, endDate));
        List<TestMeasurement> items = testMeasurementRepository.findAll(spec);
        return items.stream().map(TestMeasurementResponse::new).collect(Collectors.toList());

    }

	@Override
	public List<TestMeasurementResponse> deepSearch(String productName, LocalDateTime supplyUpdatedAfter,
			InspectionResult result) {
		Specification<TestMeasurement> spec = Specification
                .where(TestMeasurementSpecification.productNameContains(productName))
                .and(TestMeasurementSpecification.supplyUpdatedAfter(supplyUpdatedAfter))
                .and(TestMeasurementSpecification.inspectionResult(result));
		List<TestMeasurement> items = testMeasurementRepository.findAll(spec);
        return items.stream().map(TestMeasurementResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> searchMeasurements(String storageName, String storageLocation,
			BigDecimal minCapacity, BigDecimal maxCapacity, StorageStatus status, StorageType type, String firstName,
			String lastName, String email, String phone) {
		Specification<TestMeasurement> spec = Specification.where(null);

	    if (storageName != null) {
	        spec = spec.and(TestMeasurementSpecification.storageNameContains(storageName));
	    }
	    if (storageLocation != null) {
	        spec = spec.and(TestMeasurementSpecification.storageLocationContains(storageLocation));
	    }
	    if (minCapacity != null) {
	        spec = spec.and(TestMeasurementSpecification.storageCapacityGreaterThan(minCapacity));
	    }
	    if (maxCapacity != null) {
	        spec = spec.and(TestMeasurementSpecification.storageCapacityLessThan(maxCapacity));
	    }
	    if (status != null) {
	        spec = spec.and(TestMeasurementSpecification.storageStatus(status));
	    }
	    if (type != null) {
	        spec = spec.and(TestMeasurementSpecification.storageType(type));
	    }
	    if (firstName != null) {
	        spec = spec.and(TestMeasurementSpecification.inspectorFirstNameContains(firstName));
	    }
	    if (lastName != null) {
	        spec = spec.and(TestMeasurementSpecification.inspectorLastNameContains(lastName));
	    }
	    if (email != null) {
	        spec = spec.and(TestMeasurementSpecification.inspectorEmailContains(email));
	    }
	    if (phone != null) {
	        spec = spec.and(TestMeasurementSpecification.inspectorPhoneContains(phone));
	    }
	    List<TestMeasurement> items = testMeasurementRepository.findAll(spec);
        return items.stream().map(TestMeasurementResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> searchTestMeasurements(String storageName, String storageLocation,
			BigDecimal storageCapacityMin, BigDecimal storageCapacityMax, StorageType storageType,
			StorageStatus storageStatus, BigDecimal supplyQuantityMin, BigDecimal supplyQuantityMax,
			LocalDateTime supplyUpdatedAfter, LocalDateTime supplyUpdatedBefore) {
		Specification<TestMeasurement> spec = Specification.where(null);
        if (storageName != null && !storageName.isEmpty()) {
            spec = spec.and(TestMeasurementSpecification.hasStorageNameLike(storageName));
        }
        if (storageLocation != null && !storageLocation.isEmpty()) {
            spec = spec.and(TestMeasurementSpecification.hasStorageLocationLike(storageLocation));
        }
        if (storageCapacityMin != null) {
            spec = spec.and(TestMeasurementSpecification.hasStorageCapacityGreaterThan(storageCapacityMin));
        }
        if (storageCapacityMax != null) {
            spec = spec.and(TestMeasurementSpecification.hasStorageCapacityLessThan(storageCapacityMax));
        }
        if (storageType != null) {
            spec = spec.and(TestMeasurementSpecification.hasStorageType(storageType));
        }
        if (storageStatus != null) {
            spec = spec.and(TestMeasurementSpecification.hasStorageStatus(storageStatus));
        }
        if (supplyQuantityMin != null) {
            spec = spec.and(TestMeasurementSpecification.standardSupplyQuantityGreaterThan(supplyQuantityMin));
        }
        if (supplyQuantityMax != null) {
            spec = spec.and(TestMeasurementSpecification.standardSupplyQuantityLessThan(supplyQuantityMax));
        }
        if (supplyUpdatedAfter != null) {
            spec = spec.and(TestMeasurementSpecification.standardSupplyUpdatesAfter(supplyUpdatedAfter));
        }
        if (supplyUpdatedBefore != null) {
            spec = spec.and(TestMeasurementSpecification.standardSupplyUpdatesBefore(supplyUpdatedBefore));
        }
        List<TestMeasurement> items = testMeasurementRepository.findAll(spec);
        return items.stream().map(TestMeasurementResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByMeasuredValue(BigDecimal measuredValue) {
		validateBigDecimal(measuredValue);
		List<TestMeasurement> items = testMeasurementRepository.findByMeasuredValue(measuredValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for measured-value %s, found", measuredValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByMeasuredValueGreaterThan(BigDecimal measuredValue) {
		validateBigDecimal(measuredValue);
		List<TestMeasurement> items = testMeasurementRepository.findByMeasuredValueGreaterThan(measuredValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for measured-value greater than %s, found", measuredValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByMeasuredValueLessThan(BigDecimal measuredValue) {
		validateBigDecimalNonNegative(measuredValue);
		List<TestMeasurement> items = testMeasurementRepository.findByMeasuredValueLessThan(measuredValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for measured-value less than %s, found", measuredValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public boolean existsByWithinSpec(Boolean withinSpec) {
	    if (withinSpec == null) {
	        throw new ValidationException("withinSpec cannot be null");
	    }
	    return testMeasurementRepository.existsByWithinSpec(withinSpec);
	}

	@Override
	public List<TestMeasurementResponse> findByInspectionId(Long inspectionId) {
		validateInspectionId(inspectionId);
		List<TestMeasurement> items = testMeasurementRepository.findByInspectionId(inspectionId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection-id %d, found", inspectionId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_CodeContainingIgnoreCase(String code) {
		validateCodeExists(code);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_CodeContainingIgnoreCase(code);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection code %s, found", code);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_Type(InspectionType type) {
		validateInspectionType(type);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_Type(type);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection-type %s, found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_InspectionDate(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date");
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_InspectionDate(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No TestMeasurement for inspection date %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_InspectionDateAfter(LocalDateTime date) {
		DateValidator.validateNotInPast(date, "Date-after");
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_InspectionDateAfter(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No TestMeasurement for inspection date-after %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_InspectionDateBefore(LocalDateTime date) {
		DateValidator.validateNotInFuture(date, "Date-before");
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_InspectionDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No TestMeasurement for inspection date before %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_InspectionDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_InspectionDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No TestMeasurement for inspection date between %s and %s, found",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_BatchId(Long batchId) {
		validateBatchId(batchId);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_BatchId(batchId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for batch-id %d, found", batchId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductId(Long productId) {
		validateProductId(productId);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductId(productId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product-id %d, found", productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductCurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductCurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product current-quantity %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductCurrentQuantityGreaterThan(
			BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductCurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product current-quantity greater than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductCurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductCurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product current-quantity less than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductNameContainingIgnoreCase(String productName) {
		validateString(productName);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductNameContainingIgnoreCase(productName);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product name %s, found", productName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductUnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductUnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product unit-measure %s, found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductSupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductSupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product supplier-type %s, found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductStorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductStorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product storage-type %s, found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ProductGoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ProductGoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product goods-type %s, found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_InspectorId(Long inspectorId) {
		validateUserId(inspectorId);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_InspectorId(inspectorId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspector-id %d, found", inspectorId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_Product_StorageId(Long storageId) {
		validateStorageId(storageId);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_Product_StorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's storage-id %d, found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_Product_SupplyId(Long supplyId) {
		validateSupplyId(supplyId);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_Product_SupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's supply-id %d, found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_Product_ShelfId(Long shelfId) {
		validateShelfId(shelfId);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_Product_ShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's shelf-id %d, found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityInspected(BigDecimal quantityInspected) {
		validateBigDecimal(quantityInspected);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityInspected(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-inspected %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityInspectedGreaterThan(BigDecimal quantityInspected) {
		validateBigDecimal(quantityInspected);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityInspectedGreaterThan(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-inspected greater than %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityInspectedLessThan(BigDecimal quantityInspected) {
		validateBigDecimalNonNegative(quantityInspected);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityInspectedLessThan(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-inspected less than %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityAccepted(BigDecimal quantityAccepted) {
		validateBigDecimal(quantityAccepted);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityAccepted(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-accepted %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityAcceptedGreaterThan(BigDecimal quantityAccepted) {
		validateBigDecimal(quantityAccepted);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityAcceptedGreaterThan(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-accepted greater than %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityAcceptedLessThan(BigDecimal quantityAccepted) {
		validateBigDecimalNonNegative(quantityAccepted);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityAcceptedLessThan(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-accepted less than %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityRejected(BigDecimal quantityRejected) {
		validateBigDecimal(quantityRejected);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityRejected(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-rejected %s, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityRejectedGreaterThan(BigDecimal quantityRejected) {
		validateBigDecimal(quantityRejected);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityRejectedGreaterThan(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-rejected greater than %s, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityRejectedLessThan(BigDecimal quantityRejected) {
		validateBigDecimalNonNegative(quantityRejected);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityRejectedLessThan(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quantity-rejected less than %s, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_Notes(String notes) {
		validateString(notes);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_Notes(notes);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's notes %s, found", notes);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_Result(InspectionResult result) {
		validateInspectionResult(result);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_Result(result);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's result %s, found", result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_ResultAndType(InspectionResult result, InspectionType type) {
		validateInspectionResult(result);
		validateInspectionType(type);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_ResultAndType(result, type);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's result %s and type %s, found", result,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheckId(Long qualityCheckId) {
		validateQualityCheckId(qualityCheckId);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheckId(qualityCheckId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quality-check-id %d, found", qualityCheckId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_LocDate(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date-time");
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_LocDate(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No TestMeasurement for inspection quality-check date %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_LocDateAfter(LocalDateTime date) {
		DateValidator.validateNotInPast(date, "Date after");
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_LocDateAfter(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No TestMeasurement for inspection's quality-check date after %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_LocDateBefore(LocalDateTime date) {
		DateValidator.validateNotInFuture(date, "Date before");
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_LocDateBefore(date);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No TestMeasurement for inspection's quality-check date before %s, found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_LocDateBetween(LocalDateTime start,
			LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_LocDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No TestMeasurement for inspection's quality-check date between %s and %s, found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_Notes(String notes) {
		validateString(notes);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_Notes(notes);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's notes %s, found", notes);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_ReferenceType(ReferenceType referenceType) {
		validateReferenceType(referenceType);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_ReferenceType(referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's reference-type %s, found", referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_CheckType(QualityCheckType checkType) {
		validateQualityCheckType(checkType);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_CheckType(checkType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quality-check-type %s, found", checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_Status(QualityCheckStatus status) {
		validateQualityCheckStatus(status);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for inspection's quality-check-status %s, found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_ReferenceId(Long referenceId) {
		if(referenceId == null) {
			throw new NoDataFoundException("Reference-id must not be null");
		}
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_ReferenceId(referenceId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurment for inpsection's reference-id %d, found", referenceId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_StatusAndCheckType(
			QualityCheckStatus status, QualityCheckType checkType) {
		validateQualityCheckStatus(status);
		validateQualityCheckType(checkType);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_StatusAndCheckType(status, checkType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for quality-chect status %s and quality-chech-type %s, found",
					status,checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QualityCheck_ReferenceType_Notes(ReferenceType referenceType,
			String notes) {
		validateReferenceType(referenceType);
		validateString(notes);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QualityCheck_ReferenceType_Notes(referenceType, notes);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for quality-check reference-type %s and notes %, found",
					referenceType,notes);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Id(Long qualityStandardId) {
		validateQualityStandardId(qualityStandardId);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Id(qualityStandardId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standardId %d ,found", qualityStandardId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Description(String description) {
	validateString(description);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Description(description);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's description %s, found", description);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_MinValue(BigDecimal minValue) {
		validateBigDecimal(minValue);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_MinValue(minValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's min-value %s, found", minValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_MinValueGreaterThan(BigDecimal minValue) {
		validateBigDecimal(minValue);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_MinValueGreaterThan(minValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's min-value greater than %s, found", minValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_MinValueLessThan(BigDecimal minValue) {
		validateBigDecimalNonNegative(minValue);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_MaxValueLessThan(minValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's min-value less than %s, found", minValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_MaxValue(BigDecimal maxValue) {
		validateBigDecimal(maxValue);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_MaxValue(maxValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's max-value %s, found", maxValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_MaxValueGreaterThan(BigDecimal maxValue) {
		validateBigDecimal(maxValue);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_MaxValueGreaterThan(maxValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's max-value greater than %s, found", maxValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_MaxValueLessThan(BigDecimal maxValue) {
		validateBigDecimalNonNegative(maxValue);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_MaxValueLessThan(maxValue);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's max-value less than %s, found", maxValue);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Unit(Unit unit) {
		validateUnit(unit);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Unit(unit);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's unit %s, found", unit);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_Id(Long productId) {
		validateProductId(productId);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_Id(productId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard' product-id %d ,found", productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_CurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_CurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product current quantity %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_CurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_CurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product current quantity greater than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_CurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_CurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product current quantity less than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_NameContainingIgnoreCase(String productName) {
		validateString(productName);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_NameContainingIgnoreCase(productName);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product name %s is found", productName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_UnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_UnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product unit-measure %s, is found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_SupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_SupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product supply-type %s, is found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_StorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_StorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product storage-type %s is found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_GoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_GoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product goods-type %s is found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_StorageId(Long storageId) {
		validateStorageId(storageId);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_StorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product, storage-id %s is found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public TestMeasurementResponse findByStandard_Product_StorageHasShelvesForIsNull() {
		TestMeasurement item = testMeasurementRepository.findByStandard_Product_StorageHasShelvesForIsNull().orElseThrow(() -> new ValidationException("Storage without shelves, is not found"));
		return new TestMeasurementResponse(item);
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_SupplyId(Long supplyId) {
		validateSupplyId(supplyId);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_SupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for standard's product supply-id %d is found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByStandard_Product_ShelfId(Long shelfId) {
		validateShelfId(shelfId);
		List<TestMeasurement> items = testMeasurementRepository.findByStandard_Product_ShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No TestMeasurement for product's shelf-id %d, is found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<TestMeasurementResponse> findByMeasuredValueBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<TestMeasurement> items = testMeasurementRepository.findByMeasuredValueBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No measured-value between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityInspectedBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityInspectedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-rejected between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityAcceptedBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityAcceptedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-accepted between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TestMeasurementResponse> findByInspection_QuantityRejectedBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<TestMeasurement> items = testMeasurementRepository.findByInspection_QuantityRejectedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No inspection for quantity-rejected between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(testMeasurementMapper::toResponse).collect(Collectors.toList());
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
	
	private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

    private void validateStorageType(StorageType type) {
        if (type == null) {
            throw new IllegalArgumentException("Unit za StorageType ne sme biti null");
        }
    }
    
    private void validateStorageStatus(StorageStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("StorageStatus status must not be null"));
    }

    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
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
    
    private void validateGoodsType(GoodsType goodsType) {
    	Optional.ofNullable(goodsType)
    		.orElseThrow(() -> new ValidationException("GoodsType goodsType must not be null"));
    }
    
    private void validateSupplierType(SupplierType supplierType) {
    	Optional.ofNullable(supplierType)
    		.orElseThrow(()-> new ValidationException("SupplierType supplierType must not be null"));
    }
    
    private void validateUnitMeasure(UnitMeasure unitMeasure) {
    	Optional.ofNullable(unitMeasure)
    		.orElseThrow(() -> new ValidationException("UnitMeasure unitMeasure must not be null"));
    }
    
    private void validateUnit(Unit unit) {
    	Optional.ofNullable(unit)
    		.orElseThrow(() -> new ValidationException("Unit unit must not be null"));
    }
    
    private void validateReferenceType(ReferenceType referenceType) {
    	Optional.ofNullable(referenceType)
    		.orElseThrow(() -> new ValidationException("ReferenceType referenceType must not be null"));
    }
    
    private void validateQualityCheckStatus(QualityCheckStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("QualityCheckStatus status must not be null"));
    }
    
    private void validateQualityCheckType(QualityCheckType checkType) {
    	Optional.ofNullable(checkType)
    		.orElseThrow(() -> new ValidationException("QualityCheckType checkType must not be null"));
    }
    
    private void validateInspectionResult(InspectionResult result) {
    	Optional.ofNullable(result)
    		.orElseThrow(() -> new ValidationException("InspectionResult result must not be null"));
    }
    
    private void validateInspectionType(InspectionType type) {
    	Optional.ofNullable(type)
    		.orElseThrow(() -> new ValidationException("InspectionType type must not be null"));
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
    
    private Supply validateSupplyId(Long supplyId) {
    	if(supplyId == null) {
    		throw new ValidationException("Supply ID must not be null");
    	}
    	return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
    }
    
    private Product validateProductId(Long productId) {
    	if(productId == null) {
    		throw new ValidationException("Product ID must not be null");
    	}
    	return productRepository.findById(productId).orElseThrow(() -> new ValidationException("Product not found with id "+productId));
    }
    
    private QualityStandard validateQualityStandardId(Long standardId) {
    	if(standardId == null) {
    		throw new ValidationException("QualityStandard ID must not be null");
    	}
    	return qualityStandardRepository.findById(standardId).orElseThrow(() -> new ValidationException("QualityStandard not found with id "+standardId));
    }

	private Batch validateBatchId(Long batchId) {
		if(batchId == null) {
			throw new ValidationException("Batch ID must not be null");
		}
		return batchRepository.findById(batchId).orElseThrow(() -> new ValidationException("Batch not found with id "+batchId));
	}
	
	private Inspection validateInspectionId(Long inspId) {
		if(inspId == null) {
			throw new ValidationException("Inspection ID must not be null");
		}
		return inspectionRepository.findById(inspId).orElseThrow(() -> new ValidationException("Inspection not found with id "+inspId));
	}
	
	private QualityCheck validateQualityCheckId(Long qualityId) {
		if(qualityId == null) {
			throw new ValidationException("QualityCheck ID must not be null");
		}
		return qualityCheckRepository.findById(qualityId).orElseThrow(() -> new ValidationException("QualityCheck not found with id "+qualityId));
	}
	
	private User validateUserId(Long userId) {
		if(userId == null) {
			throw new ValidationException("Inspector ID must not be null");
		}
		return userRepository.findById(userId).orElseThrow(() -> new ValidationException("Inspector not found with id "+userId));
	}
	
	private void validateCodeExists(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new ValidationException("Code must not be null or empty");
        }
        if (!inspectionRepository.existsByCode(code)) {
            throw new ValidationException("TestMeasurement with code '" + code + "' not found.");
        }
    }

}
