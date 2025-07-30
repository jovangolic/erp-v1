package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.enumeration.MaterialRequestStatus;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.exception.MaterialRequestObjectErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.MaterialRequestMapper;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialRequest;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.MaterialRequestRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.MaterialRequestDTO;
import com.jovan.erp_v1.response.MaterialRequestResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialRequestService implements IMaterialRequestService {

    private final MaterialRequestRepository materialRequestRepository;
    private final MaterialRequestMapper materialRequestMapper;
    private final MaterialRepository materialRepository;
    private final WorkCenterRepository workCenterRepository;

    @Transactional
    @Override
    public MaterialRequestResponse create(MaterialRequestDTO dto) {
        Material material = validateMaterialId(dto.materialId());
        WorkCenter wc = validateWorkCenter(dto.requestingWorkCenterId());
        validateBigDecimal(dto.quantity());
        DateValidator.validateRange(dto.requestDate(), dto.neededBy());
        validateMovementType(dto.status());
        MaterialRequest req = materialRequestMapper.toEntity(dto,wc, material);
        MaterialRequest saved = materialRequestRepository.save(req);
        return materialRequestMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public MaterialRequestResponse update(Long id, MaterialRequestDTO dto) {
    	if (!dto.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        MaterialRequest req = materialRequestRepository.findById(id).orElseThrow(
                () -> new MaterialRequestObjectErrorException("MaterilaRequestObject not found with id :" + id));
        WorkCenter wc = req.getRequestingWorkCenter();
        if(dto.requestingWorkCenterId() != null && (wc.getId() == null || !dto.requestingWorkCenterId().equals(wc.getId()))) {
        	wc = validateWorkCenter(dto.requestingWorkCenterId());
        }
        Material material = req.getMaterial();
        if(dto.materialId() != null && (material.getId() == null || !dto.materialId().equals(material.getId()))) {
        	material = validateMaterialId(dto.materialId());
        }
        validateBigDecimal(dto.quantity());
        DateValidator.validateRange(dto.requestDate(), dto.neededBy());
        validateMovementType(dto.status());
        materialRequestMapper.toUpdateEntity(req, dto, wc, material);
        return materialRequestMapper.toResponse(materialRequestRepository.save(req));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!materialRequestRepository.existsById(id)) {
            throw new MaterialRequestObjectErrorException("MaterilaRequestObject not found with id :" + id);
        }
        materialRequestRepository.deleteById(id);
    }

    @Override
    public MaterialRequestResponse findOne(Long id) {
        MaterialRequest req = materialRequestRepository.findById(id).orElseThrow(
                () -> new MaterialRequestObjectErrorException("MaterilaRequestObject not found with id :" + id));
        return new MaterialRequestResponse(req);
    }

    @Override
    public List<MaterialRequestResponse> findAll() {
    	List<MaterialRequest> items = materialRequestRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("MaterialRequest list is empty");
    	}
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_NameContainingIgnoreCase(String workCenterName) {
        validateString(workCenterName);
        List<MaterialRequest> items = materialRequestRepository.findByRequestingWorkCenter_NameContainingIgnoreCase(workCenterName);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for work-center name %s is found", workCenterName);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_LocationContainingIgnoreCase(
            String workCenterLocation) {
        validateString(workCenterLocation);
        List<MaterialRequest> items = materialRequestRepository.findByRequestingWorkCenter_LocationContainingIgnoreCase(workCenterLocation);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for work-center location %s is found", workCenterLocation);
        	throw new NoDataFoundException(msg);
        }
        return items
                .stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_Capacity(BigDecimal workCenterCapacity) {
        validateBigDecimal(workCenterCapacity);
        List<MaterialRequest> items = materialRequestRepository.findByRequestingWorkCenter_Capacity(workCenterCapacity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for work-center capacity %s is found", workCenterCapacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_CapacityGreaterThan(BigDecimal workCenterCapacity) {
        validateBigDecimal(workCenterCapacity);
        List<MaterialRequest> items = materialRequestRepository.findByRequestingWorkCenter_CapacityGreaterThan(workCenterCapacity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for work-center capacity greater than %s is found", workCenterCapacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_CapacityLessThan(BigDecimal workCenterCapacity) {
        validateBigDecimalNonNegative(workCenterCapacity);
        List<MaterialRequest> items = materialRequestRepository.findByRequestingWorkCenter_CapacityLessThan(workCenterCapacity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for work-center capacity less than %s is found", workCenterCapacity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByQuantity(BigDecimal quantity) {
        validateBigDecimal(quantity);
        List<MaterialRequest> items = materialRequestRepository.findByQuantity(quantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for quantity %s is found", quantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByQuantityGreaterThan(BigDecimal quantity) {
        validateBigDecimal(quantity);
        List<MaterialRequest> items = materialRequestRepository.findByQuantityGreaterThan(quantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for quantity greater than %s is found", quantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByQuantityLessThan(BigDecimal quantity) {
        validateBigDecimalNonNegative(quantity);
        List<MaterialRequest> items = materialRequestRepository.findByQuantityLessThan(quantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for quantity less than %s is found", quantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_Id(Long materialId) {
        validateMaterialId(materialId);
        List<MaterialRequest> items = materialRequestRepository.findByMaterial_Id(materialId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for material-id %d is found", materialId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_CodeContainingIgnoreCase(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Kod ne sme biti prazan ili null");
        }
        boolean exists = materialRequestRepository.existsByMaterial_CodeIgnoreCase(code);
        if (!exists) {
            return Collections.emptyList();
        }
        return materialRequestRepository.findByMaterial_CodeContainingIgnoreCase(code).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_NameContainingIgnoreCase(String name) {
        validateString(name);
        List<MaterialRequest> items = materialRequestRepository.findByMaterial_NameContainingIgnoreCase(name);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for material name %s is found", name);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_Unit(UnitOfMeasure unit) {
        validateUnitOfMeasure(unit);
        List<MaterialRequest> items = materialRequestRepository.findByMaterial_Unit(unit);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for material unit %s is found", unit);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_CurrentStock(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        List<MaterialRequest> items = materialRequestRepository.findByMaterial_CurrentStock(currentStock);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for material current-stock %s is found", currentStock);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock) {
        validateBigDecimalNonNegative(currentStock);
        List<MaterialRequest> items = materialRequestRepository.findByMaterial_CurrentStockLessThan(currentStock);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for material current-stock less than %s is found", currentStock);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        List<MaterialRequest> items = materialRequestRepository.findByMaterial_CurrentStockGreaterThan(currentStock);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for material current-stock greater than %s is found", currentStock);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel) {
        validateBigDecimal(reorderLevel);
        List<MaterialRequest> items = materialRequestRepository.findByMaterial_ReorderLevel(reorderLevel);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for material reorder-level %s is found", reorderLevel);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_Storage_Id(Long storageId) {
        validateStorageId(storageId);
        List<MaterialRequest> items = materialRequestRepository.findByMaterial_Storage_Id(storageId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequest for material storage-id %d is found", storageId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestDate(LocalDate requestDate) {
        DateValidator.validateNotNull(requestDate, "Datum ne sme biti null");
        List<MaterialRequest> items = materialRequestRepository.findByRequestDate(requestDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequest for request-date %s is found",
        			requestDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestDateBefore(LocalDate requestDate) {
        DateValidator.validateNotNull(requestDate, "Datum ne sme biti null");
        List<MaterialRequest> items = materialRequestRepository.findByRequestDateBefore(requestDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequest for request-date before %s is found",
        			requestDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestDateAfter(LocalDate requestDate) {
        DateValidator.validateNotNull(requestDate, "Datum ne sme biti null");
        List<MaterialRequest> items = materialRequestRepository.findByRequestDateAfter(requestDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequest for request-date after %s is found",
        			requestDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestDateBetween(LocalDate startDate, LocalDate endDate) {
        DateValidator.validateRange(startDate, endDate);
        List<MaterialRequest> items = materialRequestRepository.findByRequestDateBetween(startDate, endDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequest for request-date between %s and %s is found",
        			startDate.format(formatter), endDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByNeededBy(LocalDate neededBy) {
        DateValidator.validateNotNull(neededBy, "Datum ne sme biti null");
        List<MaterialRequest> items = materialRequestRepository.findByNeededBy(neededBy);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequest for neededBy date %s is found",
        			neededBy.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByNeededByBefore(LocalDate neededBy) {
        DateValidator.validateNotNull(neededBy, "Datum ne sme biti null");
        List<MaterialRequest> items = materialRequestRepository.findByNeededByBefore(neededBy);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequest for neededBy date before %s is found",
        			neededBy.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByNeededByAfter(LocalDate neededBy) {
        DateValidator.validateNotNull(neededBy, "Datum ne sme biti null");
        List<MaterialRequest> items = materialRequestRepository.findByNeededByAfter(neededBy);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequest for neededBy date after %s is found",
        			neededBy.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByNeededByBetween(LocalDate startDate, LocalDate endDate) {
        DateValidator.validateNotNull(startDate, "Start datum ne sme biti null");
        DateValidator.validateNotNull(endDate, "End datum ne sme biti null");
        DateValidator.validateRange(startDate, endDate);
        List<MaterialRequest> items = materialRequestRepository.findByNeededByBetween(startDate, endDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequest for neededBy date between %s and %s is found",
        			startDate.format(formatter), endDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    private Material validateMaterialId(Long materialId) {
        if(materialId == null) {
        	throw new ValidationException("Material ID must not be null");
        }
        return materialRepository.findById(materialId).orElseThrow(() -> new ValidationException("Material not found with id "+materialId));
    }

    private void validateMovementType(MaterialRequestStatus status) {
        if (status == null) {
            throw new ValidationException("Status za  MaterialRequestStatus ne sme biti null.");
        }
    }

    private WorkCenter validateWorkCenter(Long workCenterId) {
        if (workCenterId == null) {
            throw new ValidationException("ID za workCenter ne sme biti null");
        }
        return workCenterRepository.findById(workCenterId).orElseThrow(() -> new ValidationException("WorkCenter not found with id "+workCenterId));
    }

    private void validateStorageId(Long storageId) {
        if (storageId == null) {
            throw new ValidationException("ID za skladiste ne sme biti null");
        }
    }

    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

    private void validateUnitOfMeasure(UnitOfMeasure unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit za UnitOfMeasure ne sme biti null");
        }
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
}
