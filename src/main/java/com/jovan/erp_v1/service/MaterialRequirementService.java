package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.exception.MaterialRequirementErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.MaterialRequirementMapper;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialRequirement;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.MaterialRequirementRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.ProductionOrderRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
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
    private final StorageRepository storageRepository;
    private final WorkCenterRepository workCenterRepository;	

    @Transactional
    @Override
    public MaterialRequirementResponse create(MaterialRequirementRequest request) {
        ProductionOrder po = validateProductionOrderId(request.productionOrderId());
        Material material = validateMaterialId(request.materialId());
        validateBigDecimal(request.requiredQuantity());
        validateBigDecimal(request.availableQuantity());
        DateValidator.validateNotNull(request.requirementDate(), "Datum ne sme biti null");
        validateMaterialRequestStatus(request.status());
        // Racunaje manjak (shortage = required - available)
        BigDecimal shortage = request.requiredQuantity().subtract(request.availableQuantity());
        MaterialRequirement requirement = materialRequirementMapper.toEntity(request,po,material);
        // (opciono) Setovanje manjaka ako postoji odgovarajuce polje u entitetu
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
        ProductionOrder po = mr.getProductionOrder();
        if(request.productionOrderId() != null && (po.getId() == null || !request.productionOrderId().equals(po.getId()))) {
        	po = validateProductionOrderId(request.productionOrderId());
        }
        Material material = mr.getMaterial();
        if(request.materialId() != null && (material.getId() == null || !request.materialId().equals(material.getId()))) {
        	material = validateMaterialId(request.materialId());
        }
        validateBigDecimal(request.requiredQuantity());
        validateBigDecimal(request.availableQuantity());
        DateValidator.validateNotNull(request.requirementDate(), "Datum ne sme biti null");
        validateMaterialRequestStatus(request.status());
        BigDecimal shortage = request.requiredQuantity().subtract(request.availableQuantity());
        MaterialRequirement updated = materialRequirementMapper.toUpdateEntity(mr, request,po, material);
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
    	List<MaterialRequirement> items = materialRequirementRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("MaterialRequirement list is empty");
    	}
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_Id(Long productionOrderId) {
        validateProductionOrderId(productionOrderId);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_Id(productionOrderId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for productionOrderId %d is found", productionOrderId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_OrderNumberContainingIgnoreCase(String orderNumber) {
        validateString(orderNumber);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_OrderNumberContainingIgnoreCase(orderNumber);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order, with order-number %s is found", orderNumber);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_Product_Id(Long productId) {
        validateProductId(productId);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_Product_Id(productId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order with productId %d is found", productId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_QuantityPlanned(Integer quantityPlanned) {
        validateInteger(quantityPlanned);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_QuantityPlanned(quantityPlanned);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order with quantity-planned %d is found", quantityPlanned);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_QuantityPlannedLessThan(Integer quantityPlanned) {
        validateInteger(quantityPlanned);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_QuantityPlannedLessThan(quantityPlanned);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order where quantity-planned less than %d is found", quantityPlanned);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_QuantityPlannedGreaterThan(Integer quantityPlanned) {
        validateInteger(quantityPlanned);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_QuantityPlannedGreaterThan(quantityPlanned);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order where quantity-planned greater than %d is found", quantityPlanned);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_QuantityProduced(Integer quantityProduced) {
        validateInteger(quantityProduced);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_QuantityProduced(quantityProduced);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order quantity produced %d is found", quantityProduced);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<MaterialRequirementResponse> findByProductionOrder_QuantityProducedGreaterThan(
			Integer quantityProduced) {
		validateInteger(quantityProduced);
		List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_QuantityProducedGreaterThan(quantityProduced);
    	if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order quantity produced greater than %d is found", quantityProduced);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
	}

	@Override
	public List<MaterialRequirementResponse> findByProductionOrder_QuantityProducedLessThan(Integer quantityProduced) {
		validateInteger(quantityProduced);
		List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_QuantityProducedGreaterThan(quantityProduced);
		if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order quantity produced less than %d is found", quantityProduced);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
	}

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_StartDate(LocalDate startDate) {
        DateValidator.validateNotNull(startDate, "Datum ne sme biti null");
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_StartDate(startDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequirement for production-order start-date %s is found", startDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_EndDate(LocalDate endDate) {
        DateValidator.validateNotNull(endDate, "Datum ne sme biti null");
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_EndDate(endDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequirement for production order end-date %s is found", endDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_Status(ProductionOrderStatus status) {
        validateProductionOrderStatus(status);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_Status(status);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order status %s is found", status);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_WorkCenter_Id(Long workCenterId) {
        validateWorkCenter(workCenterId);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_WorkCenter_Id(workCenterId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order bound to workCenterId %d is found", workCenterId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_Id(Long materialId) {
        validateMaterialId(materialId);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_Id(materialId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for material-id %d is found", materialId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_CodeContainingIgnoreCase(String materialCode) {
        validateString(materialCode);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_CodeContainingIgnoreCase(materialCode);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for material-code %s is found", materialCode);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_NameContainingIgnoreCase(String materialName) {
        validateString(materialName);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_NameContainingIgnoreCase(materialName);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for material-name %s is found", materialName);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_Unit(UnitOfMeasure unit) {
        validateUnitOfMeasure(unit);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_Unit(unit);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for material unit %s is found", unit);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_CurrentStock(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_CurrentStock(currentStock);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for material current-stock %s is found", currentStock);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock) {
        validateBigDecimalNonNegative(currentStock);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_CurrentStockLessThan(currentStock);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for material current-stock less than %s is found", currentStock);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock) {
        validateBigDecimal(currentStock);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_CurrentStockGreaterThan(currentStock);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for material current-stock greater than %s is found", currentStock);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_Storage_Id(Long storageId) {
        validateStorageId(storageId);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_Storage_Id(storageId);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for material storage-id %d is found", storageId);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel) {
        validateBigDecimal(reorderLevel);
        List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_ReorderLevel(reorderLevel);
        if(items.isEmpty()) {
        	if(items.isEmpty()) {
            	String msg = String.format("No MaterialRequirement for material reorder-level %s is found", reorderLevel);
            	throw new NoDataFoundException(msg);
            }
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<MaterialRequirementResponse> findByMaterial_ReorderLevelGreaterThan(BigDecimal reorderLevel) {
		validateBigDecimal(reorderLevel);
		List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_ReorderLevelGreaterThan(reorderLevel);
    	if(items.isEmpty()) {
        	if(items.isEmpty()) {
            	String msg = String.format("No MaterialRequirement for material reorder-level greater than %s is found", reorderLevel);
            	throw new NoDataFoundException(msg);
            }
        }
		return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
	}

	@Override
	public List<MaterialRequirementResponse> findByMaterial_ReorderLevelLessThan(BigDecimal reorderLevel) {
		validateBigDecimalNonNegative(reorderLevel);
		List<MaterialRequirement> items = materialRequirementRepository.findByMaterial_ReorderLevelLessThan(reorderLevel);
		if(items.isEmpty()) {
        	if(items.isEmpty()) {
            	String msg = String.format("No MaterialRequirement for material reorder-level less than %s is found", reorderLevel);
            	throw new NoDataFoundException(msg);
            }
        }
		return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
	}

    @Override
    public List<MaterialRequirementResponse> findByStatus(MaterialRequestStatus status) {
        validateMaterialRequestStatus(status);
        List<MaterialRequirement> items = materialRequirementRepository.findByStatus(status);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement found for status %s", status);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequiredQuantity(BigDecimal requiredQuantity) {
        validateBigDecimal(requiredQuantity);
        List<MaterialRequirement> items = materialRequirementRepository.findByRequiredQuantity(requiredQuantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement found for requirement quantity %s", requiredQuantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequiredQuantityLessThan(BigDecimal requiredQuantity) {
        validateBigDecimalNonNegative(requiredQuantity);
        List<MaterialRequirement> items = materialRequirementRepository.findByRequiredQuantityLessThan(requiredQuantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement found for requirement quantity less than %s", requiredQuantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequiredQuantityGreaterThan(BigDecimal requiredQuantity) {
        validateBigDecimal(requiredQuantity);
        List<MaterialRequirement> items = materialRequirementRepository.findByRequiredQuantityGreaterThan(requiredQuantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement found for requirement quantity greater than %s", requiredQuantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByAvailableQuantity(BigDecimal availableQuantity) {
        validateBigDecimal(availableQuantity);
        List<MaterialRequirement> items = materialRequirementRepository.findByAvailableQuantity(availableQuantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement found for available quantity %s", availableQuantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByAvailableQuantityLessThan(BigDecimal availableQuantity) {
        validateBigDecimalNonNegative(availableQuantity);
        List<MaterialRequirement> items = materialRequirementRepository.findByAvailableQuantityLessThan(availableQuantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement found for available quantity less than %s", availableQuantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByAvailableQuantityGreaterThan(BigDecimal availableQuantity) {
        validateBigDecimal(availableQuantity);
        List<MaterialRequirement> items = materialRequirementRepository.findByAvailableQuantityGreaterThan(availableQuantity);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement found for available quantity greate than %s", availableQuantity);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequirementDate(LocalDate requirementDate) {
        DateValidator.validateNotNull(requirementDate, "Datum ne sme biti null");
        List<MaterialRequirement> items = materialRequirementRepository.findByRequirementDate(requirementDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequirement found for requirement-date %s", requirementDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequirementDateBetween(LocalDate start, LocalDate end) {
        DateValidator.validateRange(start, end);
        List<MaterialRequirement> items = materialRequirementRepository.findByRequirementDateBetween(start, end);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequirement found for date between %s and %s",
        			start.format(formatter),end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByRequirementDateGreaterThanEqual(LocalDate requirementDate) {
        DateValidator.validateNotNull(requirementDate, "Datum ne sme biti null");
        List<MaterialRequirement> items = materialRequirementRepository.findByRequirementDateGreaterThanEqual(requirementDate);
        if(items.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        	String msg = String.format("No MaterialRequirementfound for requirement-date %s", requirementDate.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findByProductionOrder_OrderNumberContainingIgnoreCaseAndMaterial_CodeContainingIgnoreCase(
            String orderNumber, String materialCode) {
        validateDoubleString(orderNumber, materialCode);
        List<MaterialRequirement> items = materialRequirementRepository.findByProductionOrder_OrderNumberContainingIgnoreCaseAndMaterial_CodeContainingIgnoreCase(orderNumber, materialCode);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement for production-order number %s and material-code %s is found",
        			orderNumber, materialCode);
        	throw new NoDataFoundException(msg);
        }
        return items
                .stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialRequirementResponse> findWhereShortageIsGreaterThan(BigDecimal minShortage) {
        validateBigDecimal(minShortage);
        List<MaterialRequirement> items = materialRequirementRepository.findWhereShortageIsGreaterThan(minShortage);
        if(items.isEmpty()) {
        	String msg = String.format("No MaterialRequirement where shortage is greater than %s is found", minShortage);
        	throw new NoDataFoundException(msg);
        }
        return items.stream()
                .map(MaterialRequirementResponse::new)
                .collect(Collectors.toList());
    }

    private void validateDoubleString(String s1, String s2) {
        if (s1 == null || s1.trim().isEmpty() || s2 == null || s2.trim().isEmpty()) {
            throw new IllegalArgumentException("Oba stringa moraju biti ne-null i ne-prazna");
        }
    }

    private Material validateMaterialId(Long materialId) {
        if (materialId == null) {
        	throw new ValidationException("Material ID must not be null");
        }
        return materialRepository.findById(materialId).orElseThrow(() -> new ValidationException("Material not found with id "+materialId));
    }

    private Product validateProductId(Long productId) {
        if(productId == null) {
        	throw new ValidationException("Product ID must not be null");
        }
        return productRepository.findById(productId).orElseThrow(()-> new ValidationException("Product not found with id "+productId));
    }

    private void validateMaterialRequestStatus(MaterialRequestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status za  MaterialRequestStatus ne sme biti null.");
        }
    }

    private WorkCenter validateWorkCenter(Long workCenterId) {
        if (workCenterId == null) {
            throw new ValidationException("ID za workCenter ne sme biti null");
        }
        return workCenterRepository.findById(workCenterId).orElseThrow(() -> new ValidationException("WorkCenter not found with id "+workCenterId));
    }

    private Storage validateStorageId(Long storageId) {
        if (storageId == null) {
            throw new ValidationException("ID za skladiste ne sme biti null");
        }
        return storageRepository.findById(storageId).orElseThrow(() -> new ValidationException("Storage not found with id "+storageId));
    }

    private ProductionOrder validateProductionOrderId(Long productionOrderId) {
        if(productionOrderId == null) {
        	throw new ValidationException("ProductionOrder ID must not be null");
        }
        return productionOrderRepository.findById(productionOrderId).orElseThrow(() -> new ValidationException("ProductionOrder not found with id "+productionOrderId));
    }

    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
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
