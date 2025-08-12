package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.request.MaterialRequestDTO;
import com.jovan.erp_v1.response.MaterialRequestResponse;

public interface IMaterialRequestService {

    MaterialRequestResponse create(MaterialRequestDTO dto);
    MaterialRequestResponse update(Long id, MaterialRequestDTO dto);
    void delete(Long id);
    MaterialRequestResponse findOne(Long id);
    List<MaterialRequestResponse> findAll();
    List<MaterialRequestResponse> findByRequestingWorkCenter_NameContainingIgnoreCase(String workCenterName);
    List<MaterialRequestResponse> findByRequestingWorkCenter_LocationContainingIgnoreCase(String workCenterLocation);
    List<MaterialRequestResponse> findByRequestingWorkCenter_Capacity(BigDecimal workCenterCapacity);
    List<MaterialRequestResponse> findByRequestingWorkCenter_CapacityGreaterThan(BigDecimal workCenterCapacity);
    List<MaterialRequestResponse> findByRequestingWorkCenter_CapacityLessThan(BigDecimal workCenterCapacity);
    List<MaterialRequestResponse> findByQuantity(BigDecimal quantity);
    List<MaterialRequestResponse> findByQuantityGreaterThan(BigDecimal quantity);
    List<MaterialRequestResponse> findByQuantityLessThan(BigDecimal quantity);
    List<MaterialRequestResponse> findByMaterial_Id(Long materialId);
    List<MaterialRequestResponse> findByMaterial_CodeContainingIgnoreCase(String code);
    List<MaterialRequestResponse> findByMaterial_NameContainingIgnoreCase(String name);
    List<MaterialRequestResponse> findByMaterial_Unit(UnitOfMeasure unit);
    List<MaterialRequestResponse> findByMaterial_CurrentStock(BigDecimal currentStock);
    List<MaterialRequestResponse> findByMaterial_CurrentStockLessThan(BigDecimal currentStock);
    List<MaterialRequestResponse> findByMaterial_CurrentStockGreaterThan(BigDecimal currentStock);
    List<MaterialRequestResponse> findByMaterial_ReorderLevel(BigDecimal reorderLevel);
    List<MaterialRequestResponse> findByMaterial_ReorderLevelGreaterThan(BigDecimal reorderLevel);
    List<MaterialRequestResponse> findByMaterial_ReorderLevelLessThan(BigDecimal reorderLevel);
    List<MaterialRequestResponse> findByMaterial_Storage_Id(Long storageId);
    List<MaterialRequestResponse> findByRequestDate(LocalDate requestDate);
    List<MaterialRequestResponse> findByRequestDateBefore(LocalDate requestDate);
    List<MaterialRequestResponse> findByRequestDateAfter(LocalDate requestDate);
    List<MaterialRequestResponse> findByRequestDateBetween(LocalDate startDate, LocalDate endDate);
    List<MaterialRequestResponse> findByNeededBy(LocalDate neededBy);
    List<MaterialRequestResponse> findByNeededByBefore(LocalDate neededBy);

    List<MaterialRequestResponse> findByNeededByAfter(LocalDate neededBy);

    List<MaterialRequestResponse> findByNeededByBetween(LocalDate startDate, LocalDate endDate);
}
