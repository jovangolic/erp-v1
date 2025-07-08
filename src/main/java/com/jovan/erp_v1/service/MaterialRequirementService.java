package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.exception.MaterialRequirementErrorException;
import com.jovan.erp_v1.mapper.MaterialRequirementMapper;
import com.jovan.erp_v1.model.MaterialRequirement;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.MaterialRequirementRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ProductionOrderRepository;
import com.jovan.erp_v1.request.MaterialRequirementRequest;
import com.jovan.erp_v1.response.MaterialRequirementResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialRequirementService implements IMaterialRequirementService {

    private final MaterialRequirementRepository materialRequirementRepository;
    private final MaterialRepository materialRepository;
    private final ProductRepository productRepository;
    private final ProductionOrderRepository productionOrderRepository;
    private final MaterialRequirementMapper materialRequirementMapper;

    @Transactional
    @Override
    public MaterialRequirementResponse create(MaterialRequirementRequest request) {
        validateMaterialId(request.materialId());
        validateProductionOrderId(request.productionOrderId());
        validateBigDecimal(request.requiredQuantity());
        validateBigDecimal(request.availableQuantity());
        DateValidator.validateNotNull(request.requirementDate(), "Datum ne sme biti null");
        validateMaterialRequestStatus(request.status());
        // Izračunaj manjak (shortage = required - available)
        BigDecimal shortage = request.requiredQuantity().subtract(request.availableQuantity());
        // Mapiraj request u entitet
        MaterialRequirement requirement = materialRequirementMapper.toEntity(request);
        // (opciono) Setuj manjak ako postoji odgovarajuće polje u entitetu
        validateShortage(shortage);
        requirement.setShortage(shortage);
        MaterialRequirement saved = materialRequirementRepository.save(requirement);
        return materialRequirementMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public MaterialRequirementResponse update(Long id, MaterialRequirementRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        MaterialRequirement mr = materialRequirementRepository.findById(id)
                .orElseThrow(() -> new MaterialRequirementErrorException("MaterialRequirement not found " + id));
        validateMaterialId(request.materialId());
        validateProductionOrderId(request.productionOrderId());
        validateBigDecimal(request.requiredQuantity());
        validateBigDecimal(request.availableQuantity());
        DateValidator.validateNotNull(request.requirementDate(), "Datum ne sme biti null");
        validateMaterialRequestStatus(request.status());
        // Izračunaj manjak (shortage = required - available)
        BigDecimal shortage = request.requiredQuantity().subtract(request.availableQuantity());
        MaterialRequirement updated = materialRequirementMapper.toUpdateEntity(mr, request);
        validateShortage(shortage);
        updated.setShortage(shortage);
        return materialRequirementMapper.toResponse(materialRequirementRepository.save(updated));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!materialRequirementRepository.existsById(id)) {
            throw new MaterialRequirementErrorException("MaterialRequirement not found " + id);
        }
        materialRequirementRepository.deleteById(id);
    }

    @Override
    public MaterialRequirementResponse findOne(Long id) {
        MaterialRequirement mr = materialRequirementRepository.findById(id)
                .orElseThrow(() -> new MaterialRequirementErrorException("MaterialRequirement not found " + id));
        return new MaterialRequirementResponse(mr);
    }

    @Override
    public List<MaterialRequirementResponse> findAll() {
        return materialRequirementRepository.findAll().stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_Id(Long productionOrderId) {
        validateProductionOrderId(productionOrderId);
        return materialRequirementRepository.findByProductionOrder_Id(productionOrderId).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_OrderNumberContainingIgnoreCase(String orderNumber) {
        validateString(orderNumber);
        return materialRequirementRepository.findByProductionOrder_OrderNumberContainingIgnoreCase(orderNumber).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_Product_Id(Long productId) {
        validateProductId(productId);
        return materialRequirementRepository.findByProductionOrder_Id(productId).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_QuantityPlanned(Integer quantityPlanned) {
        validateInteger(quantityPlanned);
        return materialRequirementRepository.findByProductionOrder_QuantityPlanned(quantityPlanned).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_QuantityPlannedLessThan(Integer quantityPlanned) {
        validateInteger(quantityPlanned);
        return materialRequirementRepository.findByProductionOrder_QuantityPlannedLessThan(quantityPlanned).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_QuantityPlannedGreaterThan(Integer quantityPlanned) {
        validateInteger(quantityPlanned);
        return materialRequirementRepository.findByProductionOrder_QuantityPlannedGreaterThan(quantityPlanned).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_QuantityProduced(Integer quantityProduced) {
        validateInteger(quantityProduced);
        return materialRequirementRepository.findByProductionOrder_QuantityProduced(quantityProduced).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_StartDate(LocalDate startDate) {
        DateValidator.validateNotNull(startDate, "Datum ne sme biti null");
        return materialRequirementRepository.findByProductionOrder_StartDate(startDate).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_EndDate(LocalDate endDate) {
        DateValidator.validateNotNull(endDate, "Datum ne sme biti null");
        return materialRequirementRepository.findByProductionOrder_EndDate(endDate).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_Status(ProductionOrderStatus status) {
        validateProductionOrderStatus(status);
        return materialRequirementRepository.findByProductionOrder_Status(status).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_WorkCenter_Id(Long workCenterId) {
        validateWorkCenter(workCenterId);
        return materialRequirementRepository.findByProductionOrder_WorkCenter_Id(workCenterId).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_Id(Long materialId) {
        validateMaterialId(materialId);
        return materialRequirementRepository.findByMaterial_Id(materialId).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_CodeContainingIgnoreCase(String materialCode) {
        validateString(materialCode);
        return materialRequirementRepository.findByMaterial_CodeContainingIgnoreCase(materialCode).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_NameContainingIgnoreCase(String materialName) {
        validateString(materialName);
        return materialRequirementRepository.findByMaterial_NameContainingIgnoreCase(materialName).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_Unit(UnitOfMeasure unit) {
        validateUnitOfMeasure(unit);
        return materialRequirementRepository.findByMaterial_Unit(unit).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_CurrentStock(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        return materialRequirementRepository.findByMaterial_CurrentStock(currentStock).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        return materialRequirementRepository.findByMaterial_CurrentStockLessThan(currentStock).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        return materialRequirementRepository.findByMaterial_CurrentStockGreaterThan(currentStock).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_Storage_Id(Long storageId) {
        validateStorageId(storageId);
        return materialRequirementRepository.findByMaterial_Storage_Id(storageId).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel) {
        validateBigDecimal(reorderLevel);
        return materialRequirementRepository.findByMaterial_ReorderLevel(reorderLevel).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByStatus(MaterialRequestStatus status) {
        validateMaterialRequestStatus(status);
        return materialRequirementRepository.findByStatus(status).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequiredQuantity(BigDecimal requiredQuantity) {
        validateBigDecimal(requiredQuantity);
        return materialRequirementRepository.findByRequiredQuantity(requiredQuantity).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequiredQuantityLessThan(BigDecimal requiredQuantity) {
        validateBigDecimal(requiredQuantity);
        return materialRequirementRepository.findByRequiredQuantityLessThan(requiredQuantity).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequiredQuantityGreaterThan(BigDecimal requiredQuantity) {
        validateBigDecimal(requiredQuantity);
        return materialRequirementRepository.findByRequiredQuantityGreaterThan(requiredQuantity).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByAvailableQuantity(BigDecimal availableQuantity) {
        validateBigDecimal(availableQuantity);
        return materialRequirementRepository.findByAvailableQuantity(availableQuantity).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByAvailableQuantityLessThan(BigDecimal availableQuantity) {
        validateBigDecimal(availableQuantity);
        return materialRequirementRepository.findByAvailableQuantityLessThan(availableQuantity).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByAvailableQuantityGreaterThan(BigDecimal availableQuantity) {
        validateBigDecimal(availableQuantity);
        return materialRequirementRepository.findByAvailableQuantityGreaterThan(availableQuantity).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequirementDate(LocalDate requirementDate) {
        DateValidator.validateNotNull(requirementDate, "Datum ne sme biti null");
        return materialRequirementRepository.findByRequirementDate(requirementDate).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequirementDateBetween(LocalDate start, LocalDate end) {
        DateValidator.validateRange(start, end);
        return materialRequirementRepository.findByRequirementDateBetween(start, end).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequirementDateGreaterThanEqual(LocalDate requirementDate) {
        DateValidator.validateNotNull(requirementDate, "Datum ne sme biti null");
        return materialRequirementRepository.findByRequirementDateGreaterThanEqual(requirementDate).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_OrderNumberContainingIgnoreCaseAndMaterial_CodeContainingIgnoreCase(
            String orderNumber, String materialCode) {
        validateDoubleString(orderNumber, materialCode);
        return materialRequirementRepository
                .findByProductionOrder_OrderNumberContainingIgnoreCaseAndMaterial_CodeContainingIgnoreCase(orderNumber,
                        materialCode)
                .stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findWhereShortageIsGreaterThan(BigDecimal minShortage) {
        validateBigDecimal(minShortage);
        return materialRequirementRepository.findWhereShortageIsGreaterThan(minShortage).stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    private void validateDoubleString(String s1, String s2) {
        if (s1 == null || s1.trim().isEmpty() || s2 == null || s2.trim().isEmpty()) {
            throw new IllegalArgumentException("Oba stringa moraju biti ne-null i ne-prazna");
        }
    }

    private void validateMaterialId(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new IllegalArgumentException("Materijal sa ID " + materialId + " ne postoji.");
        }
    }

    private void validateProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Proizvod za ID " + productId + " ne postoji.");
        }
    }

    private void validateMaterialRequestStatus(MaterialRequestStatus status) {
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

    private void validateProductionOrderId(Long productionOrderId) {
        if (!productionOrderRepository.existsById(productionOrderId)) {
            throw new IllegalArgumentException("ProductionOrder ID " + productionOrderId + " ne postoji.");
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

    private void validateProductionOrderStatus(ProductionOrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("ProductionOrderStatus ne sme bit null");
        }
    }

    private void validateInteger(Integer num) {
        if (num == null || num <= 0) {
            throw new IllegalArgumentException("Vrednost mora biti pozitivan ceo broj");
        }
    }

    private void validateShortage(BigDecimal shortage) {
        if (shortage == null) {
            throw new IllegalArgumentException("Shortage ne sme biti null");
        }
        if (shortage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Dostupna količina ne sme biti veća od potrebne.");
        }
    }

}
