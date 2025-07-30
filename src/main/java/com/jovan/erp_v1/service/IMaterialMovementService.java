package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.MovementType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
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

    //nove metode
    List<MaterialMovementResponse> findByFromStorage_CapacityGreaterThan(BigDecimal capacity);
    List<MaterialMovementResponse> findByToStorage_CapacityGreaterThan(BigDecimal capacity);
    List<MaterialMovementResponse> findByFromStorage_CapacityLessThan(BigDecimal capacity);
    List<MaterialMovementResponse> findByToStorage_CapacityLessThan(BigDecimal capacity);
    List<MaterialMovementResponse> findByFromStorage_Type(StorageType type);
    List<MaterialMovementResponse> findByToStorage_Type(StorageType type);
    List<MaterialMovementResponse> findByFromStorage_Status(StorageStatus status);
    List<MaterialMovementResponse> findByToStorage_Status(StorageStatus status);
    BigDecimal countAvailableCapacityFromStorage(Long fromStorageId);
    BigDecimal countAvailableCapacityToStorage(Long toStorageId);
    boolean hasCapacityForFromStorage(Long fromStorageId, BigDecimal amount);
    boolean hasCapacityForToStoage(Long toStorageId, BigDecimal amount);
    void allocateCapacityFromStorage(Long fromStorageId, BigDecimal amount);
    void allocateCapacityToStorage(Long toStorageId, BigDecimal amount);
    void releaseCapacityFromStorage(Long fromStorageId, BigDecimal amount);
    void releaseCapacityToStorage(Long toStorageId, BigDecimal amount);
}
