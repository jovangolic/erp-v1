package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.MovementType;
import com.jovan.erp_v1.request.MaterialMovementRequest;
import com.jovan.erp_v1.response.MaterialMovementResponse;

public interface IMaterialMovementService {

    MaterialMovementResponse create(MaterialMovementRequest request);

    MaterialMovementResponse update(Long id, MaterialMovementRequest request);

    void delete(Long id);

    MaterialMovementResponse findOne(Long id);

    List<MaterialMovementResponse> findAll();

    List<MaterialMovementResponse> findByType(MovementType type);

    List<MaterialMovementResponse> findByQuantity(BigDecimal quantity);

    List<MaterialMovementResponse> findByQuantityGreaterThan(BigDecimal quantity);

    List<MaterialMovementResponse> findByQuantityLessThan(BigDecimal quantity);

    List<MaterialMovementResponse> findByFromStorage_Id(Long fromStorageId);

    List<MaterialMovementResponse> findByToStorage_Id(Long toStorageId);

    List<MaterialMovementResponse> findByFromStorage_NameContainingIgnoreCase(String fromStorageName);

    List<MaterialMovementResponse> findByToStorage_NameContainingIgnoreCase(String toStorageName);

    List<MaterialMovementResponse> findByFromStorage_LocationContainingIgnoreCase(String fromStorageLocation);

    List<MaterialMovementResponse> findByToStorage_LocationContainingIgnoreCase(String toStorageLocation);

    List<MaterialMovementResponse> findByFromStorage_Capacity(BigDecimal capacity);

    List<MaterialMovementResponse> findByToStorage_Capacity(BigDecimal capacity);

    List<MaterialMovementResponse> findByMovementDate(LocalDate movementDate);

    List<MaterialMovementResponse> findByMovementDateBetween(LocalDate start, LocalDate end);

    List<MaterialMovementResponse> findByMovementDateGreaterThanEqual(LocalDate date);

    List<MaterialMovementResponse> findByMovementDateAfterOrEqual(LocalDate movementDate);

}
