package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.model.MaterialRequirement;
import com.jovan.erp_v1.enumeration.MaterialRequestStatus;

@Repository
public interface MaterialRequirementRepository extends JpaRepository<MaterialRequirement, Long> {
    // === Production Order related ===
    List<MaterialRequirement> findByProductionOrder_Id(Long productionOrderId);
    List<MaterialRequirement> findByProductionOrder_OrderNumberContainingIgnoreCase(String orderNumber);
    @Query("SELECT mr FROM MaterialRequirement mr WHERE mr.productionOrder.product.id = :productId")
    List<MaterialRequirement> findByProductionOrder_Product_Id(@Param("productId") Long productId);
    List<MaterialRequirement> findByProductionOrder_QuantityPlanned(Integer quantityPlanned);
    List<MaterialRequirement> findByProductionOrder_QuantityPlannedLessThan(Integer quantityPlanned);
    List<MaterialRequirement> findByProductionOrder_QuantityPlannedGreaterThan(Integer quantityPlanned);
    List<MaterialRequirement> findByProductionOrder_QuantityProduced(Integer quantityProduced);
    List<MaterialRequirement> findByProductionOrder_QuantityProducedGreaterThan(Integer quantityProduced);
    List<MaterialRequirement> findByProductionOrder_QuantityProducedLessThan(Integer quantityProduced);
    List<MaterialRequirement> findByProductionOrder_StartDate(LocalDate startDate);
    List<MaterialRequirement> findByProductionOrder_EndDate(LocalDate endDate);
    List<MaterialRequirement> findByProductionOrder_Status(ProductionOrderStatus status);
    @Query("SELECT mr FROM MaterialRequirement mr WHERE mr.productionOrder.workCenter.id = :workCenterId")
    List<MaterialRequirement> findByProductionOrder_WorkCenter_Id(@Param("workCenterId") Long workCenterId);
    // === Material related ===
    List<MaterialRequirement> findByMaterial_Id(Long materialId);
    List<MaterialRequirement> findByMaterial_CodeContainingIgnoreCase(String materialCode);
    List<MaterialRequirement> findByMaterial_NameContainingIgnoreCase(String materialName);
    List<MaterialRequirement> findByMaterial_Unit(UnitOfMeasure unit);
    List<MaterialRequirement> findByMaterial_CurrentStock(BigDecimal currentStock);
    List<MaterialRequirement> findByMaterial_CurrentStockLessThan(BigDecimal currentStock);
    List<MaterialRequirement> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock);
    @Query("SELECT mr FROM MaterialRequirement mr WHERE mr.material.storage.id = :storageId")
    List<MaterialRequirement> findByMaterial_Storage_Id(@Param("storageId") Long storageId);
    List<MaterialRequirement> findByMaterial_ReorderLevel(BigDecimal reorderLevel);
    List<MaterialRequirement> findByMaterial_ReorderLevelGreaterThan(BigDecimal reorderLevel);
    List<MaterialRequirement> findByMaterial_ReorderLevelLessThan(BigDecimal reorderLevel);
    // === Material Requirement fields ===
    List<MaterialRequirement> findByStatus(MaterialRequestStatus status);
    List<MaterialRequirement> findByRequiredQuantity(BigDecimal requiredQuantity);
    List<MaterialRequirement> findByRequiredQuantityLessThan(BigDecimal requiredQuantity);
    List<MaterialRequirement> findByRequiredQuantityGreaterThan(BigDecimal requiredQuantity);
    List<MaterialRequirement> findByAvailableQuantity(BigDecimal availableQuantity);
    List<MaterialRequirement> findByAvailableQuantityLessThan(BigDecimal availableQuantity);
    List<MaterialRequirement> findByAvailableQuantityGreaterThan(BigDecimal availableQuantity);
    // === Date filters ===
    List<MaterialRequirement> findByRequirementDate(LocalDate requirementDate);
    List<MaterialRequirement> findByRequirementDateBetween(LocalDate start, LocalDate end);
    List<MaterialRequirement> findByRequirementDateGreaterThanEqual(LocalDate requirementDate);
    // === Dodatna kombinacija: filtriranje po više kriterijuma (opciono) ===
    List<MaterialRequirement> findByProductionOrder_OrderNumberContainingIgnoreCaseAndMaterial_CodeContainingIgnoreCase(
            String orderNumber, String materialCode);
    // === Napredni upit: manjak materijala veći od neke vrednosti ===
    @Query("SELECT mr FROM MaterialRequirement mr WHERE mr.requiredQuantity - mr.availableQuantity > :minShortage")
    List<MaterialRequirement> findWhereShortageIsGreaterThan(@Param("minShortage") BigDecimal minShortage);
}
