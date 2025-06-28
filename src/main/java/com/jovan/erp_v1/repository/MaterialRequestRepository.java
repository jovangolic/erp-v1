package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.model.MaterialRequest;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Long> {

    List<MaterialRequest> findByRequestingWorkCenter_NameContainingIgnoreCase(String workCenterName);

    List<MaterialRequest> findByRequestingWorkCenter_LocationContainingIgnoreCase(String workCenterLocation);

    List<MaterialRequest> findByRequestingWorkCenter_Capacity(BigDecimal workCenterCapacity);

    List<MaterialRequest> findByRequestingWorkCenter_CapacityGreaterThan(BigDecimal workCenterCapacity);

    List<MaterialRequest> findByRequestingWorkCenter_CapacityLessThan(BigDecimal workCenterCapacity);

    List<MaterialRequest> findByQuantity(BigDecimal quantity);

    List<MaterialRequest> findByQuantityGreaterThan(BigDecimal quantity);

    List<MaterialRequest> findByQuantityLessThan(BigDecimal quantity);

    List<MaterialRequest> findByMaterial_Id(Long materialId);

    List<MaterialRequest> findByMaterial_CodeContainingIgnoreCase(String code);

    List<MaterialRequest> findByMaterial_NameContainingIgnoreCase(String name);

    List<MaterialRequest> findByMaterial_Unit(UnitOfMeasure unit);

    List<MaterialRequest> findByMaterial_CurrentStock(BigDecimal currentStock);

    List<MaterialRequest> findByMaterial_CurrentStockLessThan(BigDecimal currentStock);

    List<MaterialRequest> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock);

    List<MaterialRequest> findByMaterial_ReorderLevel(BigDecimal reorderLevel);

    @Query("SELECT mr FROM MaterialRequest mr WHERE mr.material.storage.id = :storageId")
    List<MaterialRequest> findByMaterial_Storage_Id(@Param("storageId") Long storageId);

    List<MaterialRequest> findByRequestDate(LocalDate requestDate);

    List<MaterialRequest> findByRequestDateBefore(LocalDate requestDate);

    List<MaterialRequest> findByRequestDateAfter(LocalDate requestDate);

    List<MaterialRequest> findByRequestDateBetween(LocalDate startDate, LocalDate endDate);

    List<MaterialRequest> findByNeededBy(LocalDate neededBy);

    List<MaterialRequest> findByNeededByBefore(LocalDate neededBy);

    List<MaterialRequest> findByNeededByAfter(LocalDate neededBy);

    List<MaterialRequest> findByNeededByBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByMaterial_CodeIgnoreCase(String code);
}
