package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.dto.InspectionQuantityAcceptedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityAcceptedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedSummaryDTO;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionStatus;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.InspectionMapper;
import com.jovan.erp_v1.model.Batch;
import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.QualityCheck;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.BatchRepository;
import com.jovan.erp_v1.repository.InspectionRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.QualityCheckRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.specification.InspectionSpecifications;
import com.jovan.erp_v1.request.InspectionRequest;
import com.jovan.erp_v1.response.InspectionResponse;
import com.jovan.erp_v1.save_as.InspectionSaveAsRequest;
import com.jovan.erp_v1.search_request.InspectionSearchRequest;
import com.jovan.erp_v1.statistics.inspection.InspectionResultStatDTO;
import com.jovan.erp_v1.statistics.inspection.InspectionTypeStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByQualityCheckStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByQualityCheckStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByQualityCheckStatDTO;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectionService implements InfInspectionService {

	private final InspectionRepository inspectionRepository;
	private final InspectionMapper inspectionMapper;
	private final QualityCheckRepository qualityCheckRepository;
	private final ProductRepository productRepository;
	private final ShelfRepository shelfRepository;
	private final SupplyRepository supplyRepository;
	private final BatchRepository batchRepository;
	private final UserRepository userRepository;
	private final StorageRepository storageRepository;

	@Transactional
	@Override
	public InspectionResponse create(InspectionRequest request) {
		Batch batch = validateBatchId(request.batchId());
		Product product = validateProductId(request.productId());
		User inspector = validateUserId(request.inspectorId());
		QualityCheck qc = validateQualityCheckId(request.qualityCheckId());
		validateInspectionRequest(request);
		Inspection inspection = inspectionMapper.toEntity(request, batch, product, inspector, qc);
		Inspection saved = inspectionRepository.save(inspection);
		return new InspectionResponse(saved);
	}

	@Transactional
	@Override
	public InspectionResponse update(Long id, InspectionRequest request) {
		if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		Inspection inspection = inspectionRepository.findById(id).orElseThrow(() -> new ValidationException("Inspection not found with id "+id));
		Batch batch = inspection.getBatch();
		if(request.batchId() != null && (batch.getId() == null || !request.batchId().equals(batch.getId()))){
			batch = validateBatchId(request.batchId());
		}
		Product product = inspection.getProduct();
		if(request.productId() != null && (product.getId() == null || !request.productId().equals(product.getId()))){
			product = validateProductId(request.productId());
		}
		User inspector = inspection.getInspector();
		if(request.inspectorId() != null && (inspector.getId() == null || !request.inspectorId().equals(inspector.getId()))){
			inspector = validateUserId(request.inspectorId());
		}
		QualityCheck qc = inspection.getQualityCheck();
		if(request.qualityCheckId() != null && (qc.getId() == null || !request.qualityCheckId().equals(qc.getId()))) {
			qc = validateQualityCheckId(request.qualityCheckId());
		}
		validateInspectionRequest(request);
		inspectionMapper.toEntityUpdate(inspection, request, batch, product, inspector, qc);
		Inspection saved = inspectionRepository.save(inspection);
		return new InspectionResponse(saved);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!inspectionRepository.existsById(id)) {
			throw new ValidationException("Inspection ID must not be null");
		}
		inspectionRepository.deleteById(id);;
	}

	@Override
	public InspectionResponse findOne(Long id) {
		
		return null;
	}

	@Override
	public List<InspectionResponse> findAll() {
		List<Inspection> items = inspectionRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Inspection list is empty");
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> searchInspections(String storageName, String storageLocation,
			BigDecimal minCapacity, BigDecimal maxCapacity) {
		Specification<Inspection> spec = Specification.where(null);

	    spec = spec.and(InspectionSpecifications.hasStorageNameLike(storageName));
	    spec = spec.and(InspectionSpecifications.hasStorageLocationLike(storageLocation));
	    spec = spec.and(InspectionSpecifications.hasStorageCapacityBetween(minCapacity, maxCapacity));
	    List<Inspection> items = inspectionRepository.findAll(spec);
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public InspectionQuantityInspectedDTO getQuantityInspected(Long inspectionId) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new ValidationException("Inspection not found"));
        return new InspectionQuantityInspectedDTO(
                1L, // jedan inspection zapis
                inspection.calculateQuantityInspected()
        );
    }

	@Override
    public InspectionQuantityAcceptedDTO getQuantityAccepted(Long inspectionId) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new ValidationException("Inspection not found"));
        return new InspectionQuantityAcceptedDTO(
                1L,
                inspection.calculateQuantityAccepted()
        );
    }

	@Override
    public InspectionQuantityRejectedDTO getQuantityRejected(Long inspectionId) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new ValidationException("Inspection not found"));
        return new InspectionQuantityRejectedDTO(
                1L,
                inspection.calculateQuantityRejected()
        );
    }

	@Override
	public InspectionQuantityInspectedSummaryDTO getQuantityInspectedSummary() {
		return inspectionRepository.getQuantityInspectedSummary();
	}

	@Override
	public InspectionQuantityAcceptedSummaryDTO getQuantityAcceptedSummary() {
		return inspectionRepository.getQuantityAcceptedSummary();
	}

	@Override
	public InspectionQuantityRejectedSummaryDTO getQuantityRejectedSummary() {
		return inspectionRepository.getQuantityRejectedSummary();
	}

	@Override
	public boolean existsByCode(String code) {
		validateString(code);
		return inspectionRepository.existsByCode(code);	
	}

	@Override
	public List<InspectionResponse> findByCode(String code) {
		validateString(code);
		List<Inspection> items = inspectionRepository.findByCode(code);
		if(items.isEmpty()) {
			String msg = String.format("No code %s for inspection, found", code);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByType(InspectionType type) {
		validateInspectionType(type);
		List<Inspection> items = inspectionRepository.findByType(type);
		if(items.isEmpty()) {
			String msg = String.format("No inspection-type %s for inspection, found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByResult(InspectionResult result) {
		validateInspectionResult(result);
		List<Inspection> items = inspectionRepository.findByResult(result);
		if(items.isEmpty()) {
			String msg = String.format("No inspection-result %s for inspection, found", result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByNotes(String notes) {
		validateString(notes);
		List<Inspection> items = inspectionRepository.findByNotes(notes);
		if(items.isEmpty()) {
			String msg = String.format("No notes %s for inspection, found", notes);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByTypeAndResult(InspectionType type, InspectionResult result) {
		validateInspectionType(type);
		validateInspectionResult(result);
		List<Inspection> items = inspectionRepository.findByTypeAndResult(type, result);
		if(items.isEmpty()) {
			String msg = String.format("No inspection-type %s and inspection-result %s, found", type,result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByNotesAndType(String notes, InspectionType type) {
		validateString(notes);
		validateInspectionType(type);
		List<Inspection> items = inspectionRepository.findByNotesAndType(notes, type);
		if(items.isEmpty()) {
			String msg = String.format("No notes %s and inspection-type %s, found",
					notes,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByNotesAndResult(String notes, InspectionResult result) {
		validateString(notes);
		validateInspectionResult(result);
		List<Inspection> items = inspectionRepository.findByNotesAndResult(notes, result);
		if(items.isEmpty()) {
			String msg = String.format("No notes %s and inspection result %s, found", notes,result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectionDate(LocalDateTime inspectionDate) {
		DateValidator.validateNotNull(inspectionDate, "Inspection date");
		List<Inspection> items = inspectionRepository.findByInspectionDate(inspectionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection date %s, found", inspectionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectionDateBefore(LocalDateTime inspectionDate) {
		DateValidator.validateNotInFuture(inspectionDate, "Inspection date before");
		List<Inspection> items = inspectionRepository.findByInspectionDateBefore(inspectionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection for date before %s, found", inspectionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectionDateAfter(LocalDateTime inspectionDate) {
		DateValidator.validateNotInPast(inspectionDate, "Inspection date after");
		List<Inspection> items = inspectionRepository.findByInspectionDateAfter(inspectionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection date after %s, found", inspectionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectionDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<Inspection> items = inspectionRepository.findByInspectionDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No inspection date between %s and %s, found",
					start.format(formatter), end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectionDateAndResult(LocalDateTime inspectionDate,
			InspectionResult result) {
		DateValidator.validateNotNull(inspectionDate, "Inspection date");
		validateInspectionResult(result);
		List<Inspection> items = inspectionRepository.findByInspectionDateAndResult(inspectionDate, result);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No inspection for date %s and result %s, found",
					inspectionDate.format(formatter),result);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatchId(Long batchId) {
		validateBatchId(batchId);
		List<Inspection> items = inspectionRepository.findByBatchId(batchId);
		if(items.isEmpty()) {
			String msg = String.format("No batch id %d, found", batchId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatchCode(String batchCode) {
		validateString(batchCode);
		List<Inspection> items = inspectionRepository.findByBatchCode(batchCode);
		if(items.isEmpty()) {
			String msg = String.format("No batch for code %s, found", batchCode);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public boolean existsByBatchCode(String batchCode) {
		validateString(batchCode);
		return inspectionRepository.existsByCode(batchCode);	
	}

	@Override
	public List<InspectionResponse> findByBatch_ExpiryDate(LocalDate expiryDate) {
		DateValidator.validateNotNull(expiryDate, "Expiry date");
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDate(expiryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for expiry date %s, found", expiryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ExpiryDateAfter(LocalDate expiryDate) {
		DateValidator.validateNotInPast(expiryDate, "Expiry date after");
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDateAfter(expiryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for expiry date after %s, found", expiryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ExpiryDateBefore(LocalDate expiryDate) {
		DateValidator.validateNotInFuture(expiryDate, "Expiry date before");
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDateBefore(expiryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for expiry date before %s, found", expiryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ExpiryDateBetween(LocalDate start, LocalDate end) {
		DateValidator.validateRange(start, end);
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for expiry date between %s and %s, found",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDate(LocalDate productionDate) {
		DateValidator.validateNotNull(productionDate, "Production date");
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDate(productionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for production date %s, found", productionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDateAfter(LocalDate productionDate) {
		DateValidator.validateNotInPast(productionDate, "Production date after");
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDateAfter(productionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for production date after %s, found", productionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDateBefore(LocalDate productionDate) {
		DateValidator.validateNotInFuture(productionDate, "Production date before");
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDateBefore(productionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for production date before %s, found", productionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDateBetween(LocalDate productionDateStart,
			LocalDate productionDateEnd) {
		DateValidator.validateRange(productionDateStart, productionDateEnd);
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDateBetween(productionDateStart, productionDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No Batch for production date between %s and %s, found",
					productionDateStart.format(formatter), productionDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDateEqualsDateNow() {
		LocalDate today = LocalDate.now();
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDateEquals(today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for production date equal to %s, found", today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ExpiryDateLessThanEqualDateNow() {
		LocalDate today = LocalDate.now();
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDateLessThanEqual(today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for expiry date less than equal %s, found", today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDateGreaterThanEqualDateNow() {
		LocalDate today = LocalDate.now();
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDateGreaterThanEqual(today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for production date greater than equal %s, found", today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ExpiryDateGreaterThanEqual(LocalDate expiryDate) {
		DateValidator.validateNotInPast(expiryDate, "Expiry-date");
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDateGreaterThanEqual(expiryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for expiry date greater than equal %s, found", expiryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDateLessThanEqual(LocalDate productionDate) {
		DateValidator.validateNotInFuture(productionDate, "Production-date");
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDateLessThanEqual(productionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for production-date less than or equal %s, found", productionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatchExpiryDateAfterToday()  {
		LocalDate today = LocalDate.now();
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDateGreaterThan(today);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String msg = String.format("No batch for expiry date greater than %s, found", today.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ExpiryDateIsNotNull() {
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDateIsNotNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No batch for expiry date equal not-null, found");
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDateIsNull() {
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDateIsNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No batch for production date equal null, found");
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductionDateIsNotNull() {
		List<Inspection> items = inspectionRepository.findByBatch_ProductionDateIsNotNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No batch for production date equal  not-null, found");
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ExpiryDateIsNull() {
		List<Inspection> items = inspectionRepository.findByBatch_ExpiryDateIsNull();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No batch for expiry date equal to null, found");
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_QuantityProduced(Integer quantityProduced) {
		validateInteger(quantityProduced);
		List<Inspection> items = inspectionRepository.findByBatch_QuantityProduced(quantityProduced);
		if(items.isEmpty()) {
			String msg = String.format("No batch for quantity-produced %s, found", quantityProduced);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_QuantityProducedGreaterThan(Integer quantityProduced) {
		validateInteger(quantityProduced);
		List<Inspection> items = inspectionRepository.findByBatch_QuantityProducedGreaterThan(quantityProduced);
		if(items.isEmpty()) {
			String msg = String.format("No batch for quantity-produced greater than %s, found", quantityProduced);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_QuantityProducedLessThan(Integer quantityProduced) {
		validateMinInteger(quantityProduced);
		List<Inspection> items = inspectionRepository.findByBatch_QuantityProducedLessThan(quantityProduced);
		if(items.isEmpty()) {
			String msg = String.format("No batch for quantity-produced less than %s, found", quantityProduced);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_QuantityProducedBetween(Integer min, Integer max) {
		validateMinAndMaxInteger(min, max);
		List<Inspection> items = inspectionRepository.findByBatch_QuantityProducedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No batch for quantity-produced between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductId(Long productId) {
		validateProductId(productId);
		List<Inspection> items = inspectionRepository.findByBatch_ProductId(productId);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product'sid %d, found", productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductCurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<Inspection> items = inspectionRepository.findByBatch_ProductCurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's current-quantity %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductCurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<Inspection> items = inspectionRepository.findByBatch_ProductCurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's current-quantity greater than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductCurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<Inspection> items = inspectionRepository.findByBatch_ProductCurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's current-quantity less than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductCurrentQuantityBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<Inspection> items = inspectionRepository.findByBatch_ProductCurrentQuantityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's current-quantity between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductNameContainingIgnoreCase(String productName) {
		validateString(productName);
		List<Inspection> items = inspectionRepository.findByBatch_ProductNameContainingIgnoreCase(productName);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's name %s, found", productName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductUnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<Inspection> items = inspectionRepository.findByBatch_ProductUnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's unit-measure %s, found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductSupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<Inspection> items = inspectionRepository.findByBatch_ProductSupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's supplier-type %s, found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductStorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<Inspection> items = inspectionRepository.findByBatch_ProductStorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's storage-type %s, found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_ProductGoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<Inspection> items = inspectionRepository.findByBatch_ProductGoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No batch for product's goods-type %s, found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_Product_StorageId(Long storageId) {
		validateStorageId(storageId);
		List<Inspection> items = inspectionRepository.findByBatch_Product_StorageId(storageId);
		if(items.isEmpty()) {
			String msg = String.format("No batch for storage-id %d, found", storageId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_Product_ShelfId(Long shelfId) {
		validateShelfId(shelfId);
		List<Inspection> items = inspectionRepository.findByBatch_Product_ShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No batch for shelf-id %d, found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByBatch_Product_SupplyId(Long supplyId) {
		validateSupplyId(supplyId);
		List<Inspection> items = inspectionRepository.findByBatch_Product_SupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No batch for supply-id %d, found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectorId(Long inspectorId) {
		validateUserId(inspectorId);
		List<Inspection> items = inspectionRepository.findByInspectorId(inspectorId);
		if(items.isEmpty()) {
			String msg = String.format("No inspector's id %d, found", inspectorId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectorFirstNameContainingIgnoreCaseAndInspectorLastNameContainingIgnoreCase(
			String firstName, String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<Inspection> items = inspectionRepository.findByInspectorFirstNameContainingIgnoreCaseAndInspectorLastNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No inspector's for first-name %s and last-name %s, found",
					firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectorEmailLikeIgnoreCase(String inspectorEmail) {
		validateString(inspectorEmail);
		List<Inspection> items = inspectionRepository.findByInspectorEmailLikeIgnoreCase(inspectorEmail);
		if(items.isEmpty()) {
			String msg = String.format("No inspector's email %s, found", inspectorEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByInspectorPhoneNumberLikeIgnoreCase(String inspectorPhoneNumber) {
		validateString(inspectorPhoneNumber);
		List<Inspection> items = inspectionRepository.findByInspectorPhoneNumberLikeIgnoreCase(inspectorPhoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No inspector's phone-number %s, found", inspectorPhoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductId(Long productId) {
		validateProductId(productId);
		List<Inspection> items = inspectionRepository.findByProductId(productId);
		if(items.isEmpty()) {
			String msg = String.format("No product's id %d, found", productId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductCurrentQuantity(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<Inspection> items = inspectionRepository.findByProductCurrentQuantity(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No product's for current-quantity %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductCurrentQuantityGreaterThan(BigDecimal currentQuantity) {
		validateBigDecimal(currentQuantity);
		List<Inspection> items = inspectionRepository.findByProductCurrentQuantityGreaterThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No product's for current-quantity greater than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductCurrentQuantityLessThan(BigDecimal currentQuantity) {
		validateBigDecimalNonNegative(currentQuantity);
		List<Inspection> items = inspectionRepository.findByProductCurrentQuantityLessThan(currentQuantity);
		if(items.isEmpty()) {
			String msg = String.format("No product's for current-quantity less than %s, found", currentQuantity);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductCurrentQuantityBetween(BigDecimal min, BigDecimal max) {
		validateMinAndMax(min, max);
		List<Inspection> items = inspectionRepository.findByProductCurrentQuantityBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No product's for current-quantity between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductNameContainingIgnoreCase(String productName) {
		validateString(productName);
		List<Inspection> items = inspectionRepository.findByProductNameContainingIgnoreCase(productName);
		if(items.isEmpty()) {
			String msg = String.format("No product's name %s, found", productName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductUnitMeasure(UnitMeasure unitMeasure) {
		validateUnitMeasure(unitMeasure);
		List<Inspection> items = inspectionRepository.findByProductUnitMeasure(unitMeasure);
		if(items.isEmpty()) {
			String msg = String.format("No product's unit-measure %s, found", unitMeasure);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductSupplierType(SupplierType supplierType) {
		validateSupplierType(supplierType);
		List<Inspection> items = inspectionRepository.findByProductSupplierType(supplierType);
		if(items.isEmpty()) {
			String msg = String.format("No product's suppier-type %s, found", supplierType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductStorageType(StorageType storageType) {
		validateStorageType(storageType);
		List<Inspection> items = inspectionRepository.findByProductStorageType(storageType);
		if(items.isEmpty()) {
			String msg = String.format("No product's for storage-type %s, found", storageType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProductGoodsType(GoodsType goodsType) {
		validateGoodsType(goodsType);
		List<Inspection> items = inspectionRepository.findByProductGoodsType(goodsType);
		if(items.isEmpty()) {
			String msg = String.format("No product's for goods-type %s, found", goodsType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProduct_SupplyId(Long supplyId) {
		validateSupplyId(supplyId);
		List<Inspection> items = inspectionRepository.findByProduct_SupplyId(supplyId);
		if(items.isEmpty()) {
			String msg = String.format("No product's for supply-id %d, found", supplyId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProduct_ShelfId(Long shelfId) {
		validateShelfId(shelfId);
		List<Inspection> items = inspectionRepository.findByProduct_ShelfId(shelfId);
		if(items.isEmpty()) {
			String msg = String.format("No product's for shelf-id %d, found", shelfId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public InspectionResponse findByProduct_StorageHasShelvesForIsNull() {
		Inspection items = inspectionRepository.findByProduct_StorageHasShelvesForIsNull().orElseThrow(() -> new ValidationException("Storage without shelves, not found"));
		return new InspectionResponse(items);
	}

	@Override
	public List<InspectionResponse> findByProduct_ShelfRowCount(Integer rowCount) {
		validateInteger(rowCount);
		List<Inspection> items = inspectionRepository.findByProduct_ShelfRowCount(rowCount);
		if(items.isEmpty()) {
			String msg = String.format("No product's for shelf row-count %d, found", rowCount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProduct_ShelfCols(Integer cols) {
		validateInteger(cols);
		List<Inspection> items = inspectionRepository.findByProduct_ShelfCols(cols);
		if(items.isEmpty()) {
			String msg = String.format("No product's for shelf cols %d, found", cols);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProduct_ShelfRowAndColNullable(Integer row, Integer col) {
		validateInteger(row);
		validateInteger(col);
		List<Inspection> items = inspectionRepository.findByProduct_ShelfRowAndColNullable(row, col);
		if(items.isEmpty()) {
			String msg = String.format("No product's for shelf row %d, and col %d, found", row,col);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByProduct_ShelfRowAndColBetweenNullable(Integer rowMin, Integer rowMax,
			Integer colMin, Integer colMax) {
		validateMinAndMaxInteger(rowMin, rowMax);
		validateMinAndMaxInteger(colMin, colMax);
		List<Inspection> items = inspectionRepository.findByProduct_ShelfRowAndColBetweenNullable(rowMin, rowMax, colMin, colMax);
		if(items.isEmpty()) {
			String msg = String.format("No product's shelf for row between %s and %s, and column between %s and %s, found", 
					rowMin,rowMax, colMin,colMax);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityInspected(Integer quantityInspected) {
		validateInteger(quantityInspected);
		List<Inspection> items = inspectionRepository.findByQuantityInspected(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-inspected  %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityInspectedGreaterThan(Integer quantityInspected) {
		validateInteger(quantityInspected);
		List<Inspection> items = inspectionRepository.findByQuantityInspectedGreaterThan(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-inspected greater than %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityInspectedLessThan(Integer quantityInspected) {
		validateMinInteger(quantityInspected);
		List<Inspection> items = inspectionRepository.findByQuantityInspectedLessThan(quantityInspected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-inspected less than %s, found", quantityInspected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityInspectedBetween(Integer min, Integer max) {
		validateMinAndMaxInteger(min, max);
		List<Inspection> items = inspectionRepository.findByQuantityInspectedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-inspected between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityAccepted(Integer quantityAccepted) {
		validateInteger(quantityAccepted);
		List<Inspection> items = inspectionRepository.findByQuantityAccepted(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-accepted  %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityAcceptedGreaterThan(Integer quantityAccepted) {
		validateInteger(quantityAccepted);
		List<Inspection> items = inspectionRepository.findByQuantityAcceptedGreaterThan(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-accepted greater than %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityAcceptedLessThan(Integer quantityAccepted) {
		validateMinInteger(quantityAccepted);
		List<Inspection> items = inspectionRepository.findByQuantityAcceptedLessThan(quantityAccepted);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-accepted less than %s, found", quantityAccepted);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityAcceptedBetween(Integer min, Integer max) {
		validateMinAndMaxInteger(min, max);
		List<Inspection> items = inspectionRepository.findByQuantityAcceptedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-accepted between %s and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityRejected(Integer quantityRejected) {
		validateInteger(quantityRejected);
		List<Inspection> items = inspectionRepository.findByQuantityRejected(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-rejected %d, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityRejectedGreaterThan(Integer quantityRejected) {
		validateInteger(quantityRejected);
		List<Inspection> items = inspectionRepository.findByQuantityRejectedGreaterThan(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-rejected greater than %d, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityRejectedLessThan(Integer quantityRejected) {
		validateMinInteger(quantityRejected);
		List<Inspection> items = inspectionRepository.findByQuantityRejectedLessThan(quantityRejected);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-rejected less than %d, found", quantityRejected);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQuantityRejectedBetween(Integer min, Integer max) {
		validateMinAndMaxInteger(min, max);
		List<Inspection> items = inspectionRepository.findByQuantityRejectedBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No quantity-rejected between %d and %s, found", min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckId(Long qualityCheckId) {
		validateQualityCheckId(qualityCheckId);
		List<Inspection> items = inspectionRepository.findByQualityCheckId(qualityCheckId);
		if(items.isEmpty()) {
			String msg = String.format("No quality-check-id %d, found", qualityCheckId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckLocDate(LocalDateTime locDate) {
		DateValidator.validateNotNull(locDate, "Date-time");
		List<Inspection> items = inspectionRepository.findByQualityCheckLocDate(locDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No date %s for quality-check, found", locDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckLocDateAfter(LocalDateTime locDate) {
		DateValidator.validateNotInPast(locDate, "Date-after");
		List<Inspection> items = inspectionRepository.findByQualityCheckLocDateAfter(locDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No date-after %s for quality-check, found", locDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckLocDateBefore(LocalDateTime locDate) {
		DateValidator.validateNotInFuture(locDate, "Date-before");
		List<Inspection> items = inspectionRepository.findByQualityCheckLocDateBefore(locDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No date before %s for quality-check, found", locDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckLocDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<Inspection> items = inspectionRepository.findByQualityCheckLocDateBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No date between %s and %s for quality-check, found", 
					start.format(formatter), end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckNotes(String notes) {
		validateString(notes);
		List<Inspection> items = inspectionRepository.findByQualityCheckNotes(notes);
		if(items.isEmpty()) {
			String msg = String.format("No notes %s for quality-check, found", notes);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckReferenceId(Long referenceId) {
		validateReferenceId(referenceId);
		List<Inspection> items = inspectionRepository.findByQualityCheckReferenceId(referenceId);
		if(items.isEmpty()) {
			String msg = String.format("No reference-id %d, for quality-check, found", referenceId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckReferenceType(ReferenceType referenceType) {
		validateReferenceType(referenceType);
		List<Inspection> items = inspectionRepository.findByQualityCheckReferenceType(referenceType);
		if(items.isEmpty()) {
			String msg = String.format("No reference-type %s for quality-check, found", referenceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheck_CheckType(QualityCheckType checkType) {
		validateQualityCheckType(checkType);
		List<Inspection> items = inspectionRepository.findByQualityCheck_CheckType(checkType);
		if(items.isEmpty()) {
			String msg = String.format("No check-type %s for quality-check, found", checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheck_Status(QualityCheckStatus status) {
		validateQualityCheckStatus(status);
		List<Inspection> items = inspectionRepository.findByQualityCheck_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No status %s for quality-check, found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheck_ReferenceTypeAndQualityCheck_CheckType(ReferenceType referenceType,
			QualityCheckType checkType) {
		validateReferenceType(referenceType);
		validateQualityCheckType(checkType);
		List<Inspection> items = inspectionRepository.findByQualityCheck_ReferenceTypeAndQualityCheck_CheckType(referenceType, checkType);
		if(items.isEmpty()) {
			String msg = String.format("No reference-type %s, check-type %s for quality-check, found", referenceType,checkType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheck_ReferenceTypeAndQualityCheck_Status(ReferenceType referenceType,
			QualityCheckStatus status) {
		validateReferenceType(referenceType);
		validateQualityCheckStatus(status);
		List<Inspection> items = inspectionRepository.findByQualityCheck_ReferenceTypeAndQualityCheck_Status(referenceType, status);
		if(items.isEmpty()) {
			String msg = String.format("No reference-type %s , status %s for quality-check, found", 
					referenceType,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheck_CheckTypeAndQualityCheck_Status(QualityCheckType checkType,
			QualityCheckStatus status) {
		validateQualityCheckType(checkType);
		validateQualityCheckStatus(status);
		List<Inspection> items = inspectionRepository.findByQualityCheck_CheckTypeAndQualityCheck_Status(checkType, status);
		if(items.isEmpty()) {
			String msg = String.format("No check-type %s, status %s for quality-check, found",
					checkType,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckInspectorId(Long inspectorId) {
		validateUserId(inspectorId);
		List<Inspection> items = inspectionRepository.findByQualityCheckInspectorId(inspectorId);
		if(items.isEmpty()) {
			String msg = String.format("No inspector-id %d for quality-check, found", inspectorId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckInspectorEmailLikeIgnoreCase(String inspectorEmail) {
		validateString(inspectorEmail);
		List<Inspection> items = inspectionRepository.findByQualityCheckInspectorEmailLikeIgnoreCase(inspectorEmail);
		if(items.isEmpty()) {
			String msg = String.format("No email %s for inspector quality-check, found", inspectorEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckInspectorPhoneNumberLikeIgnoreCase(String inspectorPhoneNumber) {
		validateString(inspectorPhoneNumber);
		List<Inspection> items = inspectionRepository.findByQualityCheckInspectorPhoneNumberLikeIgnoreCase(inspectorPhoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No phone-number %s for inspector quality-checj, found", inspectorPhoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<InspectionResponse> findByQualityCheckInspectorFirstNameContainingIgnoreCaseAndQualityCheckInspectorLastNameContainingIgnoreCase(
			String firstName, String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<Inspection> items = inspectionRepository.findByQualityCheckInspectorFirstNameContainingIgnoreCaseAndQualityCheckInspectorLastNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No first-name %s, last-name %s for inspector quality-check, found", firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public InspectionResponse trackInspectionByInspectionDefect(Long id) {
		List<Inspection> items = inspectionRepository.trackInspectionByInspectionDefect(id);
		if(items.isEmpty()) {
			throw new NoDataFoundException("Inspection with id "+id+" for inspection-defect, not found");
		}
		Inspection ins = items.get(0);
		return new InspectionResponse(ins);
	}

	@Transactional(readOnly = true)
	@Override
	public InspectionResponse trackInspectionByTestMeasurement(Long id) {
		List<Inspection> items = inspectionRepository.trackInspectionByTestMeasurement(id);
		if(items.isEmpty()) {
			throw new NoDataFoundException("Inspection with id "+id+" for test-measurement, not found");
		}
		Inspection ins = items.get(0);
		return new InspectionResponse(ins);
	}

	@Override
	public List<InspectionResponse> findByReports(Long id, String notes) {
		if(id != null) validateInspectionId(id);
		if(notes != null && !notes.trim().isEmpty()) validateString(notes);
		List<Inspection> items = inspectionRepository.findByReports(id, notes);
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional
	@Override
	public InspectionResponse confirmInspection(Long id) {
		Inspection ins = inspectionRepository.findById(id).orElseThrow(() -> new ValidationException("Inspection not found with id "+id));
		ins.setConfirmed(true);
		ins.setStatus(InspectionStatus.CONFIRMED);
		ins.getDefects().stream()
			.filter(i -> i.getQuantityAffected() > 0)
			.forEach(i -> i.setConfirmed(true));
		ins.getMeasurements().stream()
			
			.forEach(t -> t.setConfirmed(true));
		return new InspectionResponse(inspectionRepository.save(ins));
	}

	@Transactional
	@Override
	public InspectionResponse cancelInspection(Long id) {
		Inspection ins = inspectionRepository.findById(id).orElseThrow(() -> new ValidationException("Inspection not found with id "+id));
		if(ins.getStatus() != InspectionStatus.NEW && ins.getStatus() != InspectionStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED inspections can be cancelled");
		}
		ins.setStatus(InspectionStatus.CANCELLED);
		return new InspectionResponse(inspectionRepository.save(ins));
	}

	@Transactional
	@Override
	public InspectionResponse closeInspection(Long id) {
		Inspection ins = inspectionRepository.findById(id).orElseThrow(() -> new ValidationException("Inspection not found with id "+id));
		if(ins.getStatus() != InspectionStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED inspections can be closed");
		}
		ins.setStatus(InspectionStatus.CLOSED);
		return new InspectionResponse(inspectionRepository.save(ins));
	}

	@Transactional
	@Override
	public InspectionResponse changeStatus(Long id, InspectionStatus status) {
		Inspection ins = inspectionRepository.findById(id).orElseThrow(() -> new ValidationException("Inspection not found with id "+id));
		validateInspectionStatus(status);
		if(ins.getStatus() == InspectionStatus.CLOSED) {
			throw new ValidationException("Closed inspections cannot change status");
		}
		if(status == InspectionStatus.CONFIRMED) {
			if(ins.getStatus() != InspectionStatus.NEW) {
				throw new ValidationException("Only NEW inspections can be confirmed");
			}
			ins.setConfirmed(true);
			ins.getDefects().stream().forEach(f -> f.setConfirmed(true));
			ins.getMeasurements().stream().forEach(t -> t.setConfirmed(true));
		}
		ins.setStatus(status);
		return new InspectionResponse(inspectionRepository.save(ins));
	}

	@Transactional
	@Override
	public InspectionResponse saveInspection(InspectionRequest request) {
		Inspection ins = Inspection.builder()
				.code(request.code())
				.type(request.type())
				.batch(validateBatchId(request.batchId()))
				.product(validateProductId(request.productId()))
				.inspector(validateUserId(request.inspectorId()))
				.quantityInspected(request.quantityInspected())
				.quantityAccepted(request.quantityAccepted())
				.quantityRejected(request.quantityRejected())
				.notes(request.notes())
				.result(request.result())
				.qualityCheck(validateQualityCheckId(request.qualityCheckId()))
				.status(request.satus())
				.confirmed(request.confirmed())
				.build();
		Inspection saved = inspectionRepository.save(ins);
		return new InspectionResponse(saved);
	}

	@Transactional
	@Override
	public InspectionResponse saveAs(InspectionSaveAsRequest request) {
		
		return null;
	}

	@Transactional
	@Override
	public List<InspectionResponse> saveAll(List<InspectionRequest> requests) {
		
		return null;
	}

	@Override
	public List<InspectionResponse> generalSearch(InspectionSearchRequest request) {
		Specification<Inspection> spec = InspectionSpecifications.fromRequest(request);
		List<Inspection> items = inspectionRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspections for given criteria, found");
		}
		return items.stream().map(inspectionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<QuantityInspectedByBatchStatDTO> countQuantityInspectedByBatch() {
		List<QuantityInspectedByBatchStatDTO> items = inspectionRepository.countQuantityInspectedByBatch();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quantity-inspected by batch, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityInspected = item.getQuantityInspected();
					Long batchId = item.getBatchId();
					String batchCode = item.getBatchCode();
					return new QuantityInspectedByBatchStatDTO(count, quantityInspected, batchId, batchCode);
				})
				.toList();
		}

	@Override
	public List<QuantityRejectedByBatchStatDTO> countQuantityRejectedByBatch() {
		List<QuantityRejectedByBatchStatDTO> items = inspectionRepository.countQuantityRejectedByBatch();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quantity-rejected by batch, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityRejected = item.getQuantityRejected();
					Long batchId = item.getBatchId();
					String batchCode = item.getBatchCode();
					return new QuantityRejectedByBatchStatDTO(count, quantityRejected, batchId, batchCode);
				})
				.toList();
	}

	@Override
	public List<QuantityAcceptedByBatchStatDTO> countQuantityAcceptedByBatch() {
		List<QuantityAcceptedByBatchStatDTO> items = inspectionRepository.countQuantityAcceptedByBatch();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quantity-accepted by batch, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityAccepted = item.getQuantityAccepted();
					Long batchId = item.getBatchId();
					String batchCode = item.getBatchCode();
					return new QuantityAcceptedByBatchStatDTO(count, quantityAccepted, batchId, batchCode);
				})
				.toList();
	}

	@Override
	public List<QuantityInspectedByProductStatDTO> countQuantityInspectedByProduct() {
		List<QuantityInspectedByProductStatDTO> items = inspectionRepository.countQuantityInspectedByProduct();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quantity-inspected by product, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityInspected = item.getQuantityInspected();
					Long productId = item.getProductId();
					String productName = item.getProductName();
					return new QuantityInspectedByProductStatDTO(count, quantityInspected, productId, productName);
				})
				.toList();
	}

	@Override
	public List<QuantityAcceptedByProductStatDTO> countQuantityAcceptedByProduct() {
		List<QuantityAcceptedByProductStatDTO> items = inspectionRepository.countQuantityAcceptedByProduct();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection fro count quantity-accepted by product, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityAccepted = item.getQuantityAccepted();
					Long productId = item.getProductId();
					String productName = item.getProductName();
					return new QuantityAcceptedByProductStatDTO(count, quantityAccepted, productId, productName);
				})
				.toList();
		}

	@Override
	public List<QuantityRejectedByProductStatDTO> countQuantityRejectedByProduct() {
		List<QuantityRejectedByProductStatDTO> items = inspectionRepository.countQuantityRejectedByProduct();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quantity-rejected by product, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityRejected = item.getQuantityRejected();
					Long productId = item.getProductId();
					String productName = item.getProductName();
					return new QuantityRejectedByProductStatDTO(count, quantityRejected, productId, productName);
				})
				.toList();
		}

	@Override
	public List<QuantityInspectedByInspectorStatDTO> countQuantityInspectedByInspector() {
		List<QuantityInspectedByInspectorStatDTO> items = inspectionRepository.countQuantityInspectedByInspector();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Insepction fro count quantity-inspected by given inspector, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityInspected = item.getQuantityInspected();
					Long inspectorId = item.getInspectorId();
					String firstName = item.getFirstName();
					String lastName = item.getLastName();
					return new QuantityInspectedByInspectorStatDTO(count, quantityInspected, inspectorId, firstName, lastName);
				})
				.toList();
		}

	@Override
	public List<QuantityAcceptedByInspectorStatDTO> countQuantityAcceptedByInspector() {
		List<QuantityAcceptedByInspectorStatDTO> items = inspectionRepository.countQuantityAcceptedByInspector();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quantity-accepted by given inspector, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityAccepted = item.getQuantityAccepted();
					Long inspectorId = item.getInspectorId();
					String firstName = item.getFirstName();
					String lastName = item.getLastName();
					return new QuantityAcceptedByInspectorStatDTO(count, quantityAccepted, inspectorId, firstName, lastName);
				})
				.toList();
		}

	@Override
	public List<QuantityRejectedByInspectorStatDTO> countQuantityRejectedByInspector() {
		List<QuantityRejectedByInspectorStatDTO> items = inspectionRepository.countQuantityRejectedByInspector();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quality-rejected by particular inspector, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityRejected = item.getQuantityRejected();
					Long inspectorId = item.getInspectorId();
					String firstName = item.getFirstName();
					String lastName = item.getLastName();
					return new QuantityRejectedByInspectorStatDTO(count, quantityRejected, inspectorId, firstName, lastName);
				})
				.toList();	
		}

	@Override
	public List<QuantityInspectedByQualityCheckStatDTO> countQuantityInspectedByQualityCheck() {
		List<QuantityInspectedByQualityCheckStatDTO> items = inspectionRepository.countQuantityInspectedByQualityCheck();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quantity-inspected by quality-check, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityInspected = item.getQuantityInspected();
					Long qualityCheckId = item.getQualityCheckId();
					return new QuantityInspectedByQualityCheckStatDTO(count, quantityInspected, qualityCheckId);
				})
				.toList();
		}

	@Override
	public List<QuantityAcceptedByQualityCheckStatDTO> countQuantityAcceptedByQualityCheck() {
		List<QuantityAcceptedByQualityCheckStatDTO> items = inspectionRepository.countQuantityAcceptedByQualityCheck();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quality-accepted by quality-check, found");
		}
		return items.stream()
				.map(item  -> {
					Long count = item.getCount();
					Integer quantityAccepted = item.getQuantityAccepted();
					Long qualityCheckId = item.getQualityCheckId();
					return new QuantityAcceptedByQualityCheckStatDTO(count, quantityAccepted, qualityCheckId);
				})
				.toList();
		}

	@Override
	public List<QuantityRejectedByQualityCheckStatDTO> countQuantityRejectedByQualityCheck() {
		List<QuantityRejectedByQualityCheckStatDTO> items = inspectionRepository.countQuantityRejectedByQualityCheck();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count quality-rejected by quality check, found");
		}
		return items.stream()
				.map(item -> {
					Long count = item.getCount();
					Integer quantityRejected = item.getQuantityRejected();
					Long qualityCheckId = item.getQualityCheckId();
					return new QuantityRejectedByQualityCheckStatDTO(count, quantityRejected, qualityCheckId);
				})
				.toList();
		}

	@Override
	public List<InspectionTypeStatDTO> countInspectionByType() {
		List<InspectionTypeStatDTO> items = inspectionRepository.countInspectionByType();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count insepction-type, is found");
		}
		return items.stream()
				.map(item -> {
					InspectionType type = item.type();
					Long count = item.count();
					return new InspectionTypeStatDTO(type, count);
				})
				.toList();
		}

	@Override
	public List<InspectionResultStatDTO> countInspectionByResult() {
		List<InspectionResultStatDTO> items = inspectionRepository.countInspectionByResult();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Inspection for count inspection-result, is found");
		}
		return items.stream()
				.map(item -> {
					InspectionResult res = item.result();
					Long count = item.count();
					return new InspectionResultStatDTO(res, count);
				})
				.toList();
		}
	
	private QualityCheck validateQualityCheckId(Long qualityCheckId) {
		if(qualityCheckId == null) {
			throw new ValidationException("QualityCheck ID must not be null");
		}
		return qualityCheckRepository.findById(qualityCheckId).orElseThrow(() -> new ValidationException("QualityCheck not found with id "+qualityCheckId));
	}
	
	private Batch validateBatchId(Long batchId) {
		if(batchId == null) {
			throw new ValidationException("Batch ID must not be null");
		}
		return batchRepository.findById(batchId).orElseThrow(() -> new ValidationException("Batch not found with id "+batchId));
	}
	
	private Supply validateSupplyId(Long supplyId) {
		if(supplyId == null) {
			throw new ValidationException("Supply ID must not be null");
		}
		return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
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
	
	private User validateUserId(Long userId) {
		if(userId == null) {
			throw new ValidationException("Inspector ID must not be null");
		}
		return userRepository.findById(userId).orElseThrow(() -> new ValidationException("Inspector not found with id "+userId));
	}
	
	private void validateInspectionType(InspectionType type) {
		Optional.ofNullable(type)
			.orElseThrow(() -> new ValidationException("InspectionType type must not be null"));
	}
	
	private void validateInspectionResult(InspectionResult result) {
		Optional.ofNullable(result)
			.orElseThrow(() -> new ValidationException("InspectionResult result must not be null"));
	}
	
	private void validateReferenceType(ReferenceType referenceType) {
		Optional.ofNullable(referenceType)
			.orElseThrow(() -> new ValidationException("ReferenceType referenceType must not be null"));
	}
	
	private void validateQualityCheckType(QualityCheckType checkType) {
		Optional.ofNullable(checkType)
			.orElseThrow(() -> new ValidationException("QualityCheckType checkType must not be null"));
	}
	
	private void validateQualityCheckStatus(QualityCheckStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("QualityCheckStatus status must not be null"));
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
	
	private void validateReferenceId(Long referenceId) {
		if(referenceId == null) {
			throw new ValidationException("Reference-id, must not be null");
		}
	}
	
	private void validateMinInteger(Integer num) {
		if(num == null || num < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
	}

	private Storage validateStorageId(Long storageId) {
		if(storageId == null) {
			throw new ValidationException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
	}
	
	private void validateInspectionRequest(InspectionRequest request) {
		if(request == null) {
			throw new ValidationException("InspectionRequest request must not be null");
		}
		validateString(request.code());
		validateInspectionType(request.type());
		validateInteger(request.quantityAccepted());
		validateInteger(request.quantityInspected());
		validateInteger(request.quantityRejected());
		validateString(request.notes());
		validateInspectionResult(request.result());
	}
	
	private void validateInspectionStatus(InspectionStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("InspectionStatus status must not be null"));
	}
	
	private Inspection validateInspectionId(Long id) {
		if(id == null) {
			throw new ValidationException("Inspection id must not be null");
		}
		return inspectionRepository.findById(id).orElseThrow(() -> new ValidationException("Inspection not found with id "+id));
	}
}
