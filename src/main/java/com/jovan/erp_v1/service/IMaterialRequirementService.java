package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.request.MaterialRequirementRequest;
import com.jovan.erp_v1.response.MaterialRequirementResponse;

public interface IMaterialRequirementService {

    MaterialRequirementResponse create(MaterialRequirementRequest request);
    MaterialRequirementResponse update(Long id, MaterialRequirementRequest request);
    void delete(Long id);
    MaterialRequirementResponse findOne(Long id);
    List<MaterialRequirementResponse> findAll();
    List<MaterialRequirementResponse> findByProductionOrder_Id(Long productionOrderId);
    List<MaterialRequirementResponse> findByProductionOrder_OrderNumberContainingIgnoreCase(String orderNumber);
    List<MaterialRequirementResponse> findByProductionOrder_Product_Id(Long productId);
    List<MaterialRequirementResponse> findByProductionOrder_QuantityPlanned(Integer quantityPlanned);
    List<MaterialRequirementResponse> findByProductionOrder_QuantityPlannedLessThan(Integer quantityPlanned);
    List<MaterialRequirementResponse> findByProductionOrder_QuantityPlannedGreaterThan(Integer quantityPlanned);
    List<MaterialRequirementResponse> findByProductionOrder_QuantityProduced(Integer quantityProduced);
    List<MaterialRequirementResponse> findByProductionOrder_StartDate(LocalDate startDate);
    List<MaterialRequirementResponse> findByProductionOrder_EndDate(LocalDate endDate);
    List<MaterialRequirementResponse> findByProductionOrder_Status(ProductionOrderStatus status);
    List<MaterialRequirementResponse> findByProductionOrder_WorkCenter_Id(Long workCenterId);
    List<MaterialRequirementResponse> findByMaterial_Id(Long materialId);
    List<MaterialRequirementResponse> findByMaterial_CodeContainingIgnoreCase(String materialCode);
    List<MaterialRequirementResponse> findByMaterial_NameContainingIgnoreCase(String materialName);
    List<MaterialRequirementResponse> findByMaterial_Unit(UnitOfMeasure unit);
    List<MaterialRequirementResponse> findByMaterial_CurrentStock(BigDecimal currentStock);
    List<MaterialRequirementResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock);
    List<MaterialRequirementResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock);
    List<MaterialRequirementResponse> findByMaterial_Storage_Id(Long storageId);
    List<MaterialRequirementResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel);
    List<MaterialRequirementResponse> findByStatus(MaterialRequestStatus status);
    List<MaterialRequirementResponse> findByRequiredQuantity(BigDecimal requiredQuantity);
    List<MaterialRequirementResponse> findByRequiredQuantityLessThan(BigDecimal requiredQuantity);
    List<MaterialRequirementResponse> findByRequiredQuantityGreaterThan(BigDecimal requiredQuantity);
    List<MaterialRequirementResponse> findByAvailableQuantity(BigDecimal availableQuantity);
    List<MaterialRequirementResponse> findByAvailableQuantityLessThan(BigDecimal availableQuantity);
    List<MaterialRequirementResponse> findByAvailableQuantityGreaterThan(BigDecimal availableQuantity);
    List<MaterialRequirementResponse> findByRequirementDate(LocalDate requirementDate);
    List<MaterialRequirementResponse> findByRequirementDateBetween(LocalDate start, LocalDate end);
    List<MaterialRequirementResponse> findByRequirementDateGreaterThanEqual(LocalDate requirementDate);
    List<MaterialRequirementResponse> findByProductionOrder_OrderNumberContainingIgnoreCaseAndMaterial_CodeContainingIgnoreCase(
            String orderNumber, String materialCode);
    List<MaterialRequirementResponse> findWhereShortageIsGreaterThan(BigDecimal minShortage);
}
