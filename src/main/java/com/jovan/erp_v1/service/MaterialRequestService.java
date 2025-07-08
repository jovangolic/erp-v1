package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.enumeration.MaterialRequestStatus;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.exception.MaterialRequestObjectErrorException;
import com.jovan.erp_v1.mapper.MaterialRequestMapper;
import com.jovan.erp_v1.model.MaterialRequest;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.MaterialRequestRepository;
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

    @Transactional
    @Override
    public MaterialRequestResponse create(MaterialRequestDTO dto) {
        validateMaterialId(dto.materialId());
        validateWorkCenter(dto.requestingWorkCenterId());
        validateBigDecimal(dto.quantity());
        DateValidator.validateRange(dto.requestDate(), dto.neededBy());
        validateMovementType(dto.status());
        MaterialRequest req = materialRequestMapper.toEntity(dto);
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
        validateMaterialId(dto.materialId());
        validateWorkCenter(dto.requestingWorkCenterId());
        validateBigDecimal(dto.quantity());
        DateValidator.validateRange(dto.requestDate(), dto.neededBy());
        validateMovementType(dto.status());
        materialRequestMapper.toUpdateEntity(req, dto);
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
        return materialRequestRepository.findAll().stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_NameContainingIgnoreCase(String workCenterName) {
        validateString(workCenterName);
        return materialRequestRepository.findByRequestingWorkCenter_NameContainingIgnoreCase(workCenterName).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_LocationContainingIgnoreCase(
            String workCenterLocation) {
        validateString(workCenterLocation);
        return materialRequestRepository.findByRequestingWorkCenter_LocationContainingIgnoreCase(workCenterLocation)
                .stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_Capacity(BigDecimal workCenterCapacity) {
        validateBigDecimal(workCenterCapacity);
        return materialRequestRepository.findByRequestingWorkCenter_Capacity(workCenterCapacity).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_CapacityGreaterThan(BigDecimal workCenterCapacity) {
        validateBigDecimal(workCenterCapacity);
        return materialRequestRepository.findByRequestingWorkCenter_CapacityGreaterThan(workCenterCapacity).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestingWorkCenter_CapacityLessThan(BigDecimal workCenterCapacity) {
        validateBigDecimal(workCenterCapacity);
        return materialRequestRepository.findByRequestingWorkCenter_CapacityLessThan(workCenterCapacity).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByQuantity(BigDecimal quantity) {
        validateBigDecimal(quantity);
        return materialRequestRepository.findByQuantity(quantity).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByQuantityGreaterThan(BigDecimal quantity) {
        validateBigDecimal(quantity);
        return materialRequestRepository.findByQuantityGreaterThan(quantity).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByQuantityLessThan(BigDecimal quantity) {
        validateBigDecimal(quantity);
        return materialRequestRepository.findByQuantityLessThan(quantity).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_Id(Long materialId) {
        validateMaterialId(materialId);
        return materialRequestRepository.findByMaterial_Id(materialId).stream()
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
        return materialRequestRepository.findByMaterial_NameContainingIgnoreCase(name).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_Unit(UnitOfMeasure unit) {
        validateUnitOfMeasure(unit);
        return materialRequestRepository.findByMaterial_Unit(unit).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_CurrentStock(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        return materialRequestRepository.findByMaterial_CurrentStock(currentStock).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        return materialRequestRepository.findByMaterial_CurrentStockLessThan(currentStock).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        return materialRequestRepository.findByMaterial_CurrentStockGreaterThan(currentStock).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel) {
        validateBigDecimal(reorderLevel);
        return materialRequestRepository.findByMaterial_ReorderLevel(reorderLevel).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByMaterial_Storage_Id(Long storageId) {
        validateStorageId(storageId);
        return materialRequestRepository.findByMaterial_Storage_Id(storageId).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestDate(LocalDate requestDate) {
        DateValidator.validateNotNull(requestDate, "Datum ne sme biti null");
        return materialRequestRepository.findByRequestDate(requestDate).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestDateBefore(LocalDate requestDate) {
        DateValidator.validateNotNull(requestDate, "Datum ne sme biti null");
        return materialRequestRepository.findByRequestDateBefore(requestDate).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestDateAfter(LocalDate requestDate) {
        DateValidator.validateNotNull(requestDate, "Datum ne sme biti null");
        return materialRequestRepository.findByRequestDateAfter(requestDate).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByRequestDateBetween(LocalDate startDate, LocalDate endDate) {
        DateValidator.validateRange(startDate, endDate);
        return materialRequestRepository.findByRequestDateBetween(startDate, endDate).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByNeededBy(LocalDate neededBy) {
        DateValidator.validateNotNull(neededBy, "Datum ne sme biti null");
        return materialRequestRepository.findByNeededBy(neededBy).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByNeededByBefore(LocalDate neededBy) {
        DateValidator.validateNotNull(neededBy, "Datum ne sme biti null");
        return materialRequestRepository.findByNeededByBefore(neededBy).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByNeededByAfter(LocalDate neededBy) {
        DateValidator.validateNotNull(neededBy, "Datum ne sme biti null");
        return materialRequestRepository.findByNeededByAfter(neededBy).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequestResponse> findByNeededByBetween(LocalDate startDate, LocalDate endDate) {
        DateValidator.validateNotNull(startDate, "Start datum ne sme biti null");
        DateValidator.validateNotNull(endDate, "End datum ne sme biti null");
        DateValidator.validateRange(startDate, endDate);
        return materialRequestRepository.findByNeededByBetween(startDate, endDate).stream()
                .map(MaterialRequestResponse::new)
                .collect(Collectors.toList());
    }

    private void validateMaterialId(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new IllegalArgumentException("Materijal sa ID " + materialId + " ne postoji.");
        }
    }

    private void validateMovementType(MaterialRequestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status za  MaterialRequestStatus ne sme biti null.");
        }
    }

    private void validateWorkCenter(Long workCenterId) {
        if (workCenterId == null) {
            throw new IllegalArgumentException("ID za workCenter ne sme biti null");
        }
    }

    private void validateStorageId(Long storageId) {
        if (storageId == null) {
            throw new IllegalArgumentException("ID za skladiste ne sme biti null");
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

}
