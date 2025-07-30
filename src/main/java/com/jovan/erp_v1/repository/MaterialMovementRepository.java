package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jovan.erp_v1.model.MaterialMovement;
import com.jovan.erp_v1.enumeration.MovementType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface MaterialMovementRepository extends JpaRepository<MaterialMovement, Long> {

    List<MaterialMovement> findByType(MovementType type);
    List<MaterialMovement> findByQuantity(BigDecimal quantity);
    List<MaterialMovement> findByQuantityGreaterThan(BigDecimal quantity);
    List<MaterialMovement> findByQuantityLessThan(BigDecimal quantity);
    List<MaterialMovement> findByFromStorage_Id(Long fromStorageId);
    List<MaterialMovement> findByToStorage_Id(Long toStorageId);
    List<MaterialMovement> findByFromStorage_NameContainingIgnoreCase(String fromStorageName);
    List<MaterialMovement> findByToStorage_NameContainingIgnoreCase(String toStorageName);
    List<MaterialMovement> findByFromStorage_LocationContainingIgnoreCase(String fromStorageLocation);
    List<MaterialMovement> findByToStorage_LocationContainingIgnoreCase(String toStorageLocation);
    List<MaterialMovement> findByFromStorage_Capacity(BigDecimal capacity);
    List<MaterialMovement> findByToStorage_Capacity(BigDecimal capacity);
    List<MaterialMovement> findByMovementDate(LocalDate movementDate);
    List<MaterialMovement> findByMovementDateBetween(LocalDate start, LocalDate end);
    List<MaterialMovement> findByMovementDateGreaterThanEqual(LocalDate date);
    @Query("SELECT m FROM MaterialMovement m WHERE m.movementDate >= :movementDate")
    List<MaterialMovement> findByMovementDateAfterOrEqual(@Param("movementDate") LocalDate movementDate);
    
    //nove metode
    List<MaterialMovement> findByFromStorage_CapacityGreaterThan(BigDecimal capacity);
    List<MaterialMovement> findByToStorage_CapacityGreaterThan(BigDecimal capacity);
    List<MaterialMovement> findByFromStorage_CapacityLessThan(BigDecimal capacity);
    List<MaterialMovement> findByToStorage_CapacityLessThan(BigDecimal capacity);
    List<MaterialMovement> findByFromStorage_Type(StorageType type);
    List<MaterialMovement> findByToStorage_Type(StorageType type);
    List<MaterialMovement> findByFromStorage_Status(StorageStatus status);
    List<MaterialMovement> findByToStorage_Status(StorageStatus status);
}
