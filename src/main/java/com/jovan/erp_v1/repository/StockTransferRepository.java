package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.model.StockTransfer;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

    List<StockTransfer> findByStatus(TransferStatus status);
    List<StockTransfer> findByTransferDate(LocalDate transferDate);
    List<StockTransfer> findByTransferDateBetween(LocalDate from, LocalDate to);
    List<StockTransfer> findByFromStorageId(Long fromStorageId);
    List<StockTransfer> findByToStorageId(Long toStorageId);
    List<StockTransfer> findByFromStorage_Name(String fromStorageName);
    List<StockTransfer> findByFromStorage_Location(String fromLocation);
    List<StockTransfer> findByToStorage_Name(String toStorageName);
    List<StockTransfer> findByToStorage_Location(String toLocation);
    List<StockTransfer> findByFromStorage_Type(StorageType fromStorageType);
    List<StockTransfer> findByToStorage_Type(StorageType toStorageType);
    // napredne metode pretrage
    // Pretraga po statusu i opsegu datuma
    @Query("SELECT st FROM StockTransfer st WHERE st.status = :status AND st.transferDate BETWEEN :startDate AND :endDate")
    List<StockTransfer> findByStatusAndDateRange(
            @Param("status") TransferStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    // Pretraga po tipu oba magacina
    @Query("SELECT st FROM StockTransfer st WHERE st.fromStorage.type = :fromType AND st.toStorage.type = :toType")
    List<StockTransfer> findByFromAndToStorageType(
            @Param("fromType") StorageType fromType,
            @Param("toType") StorageType toType);
    // Pretraga po imenu i lokaciji FROM magacina
    @Query("SELECT st FROM StockTransfer st WHERE st.fromStorage.name LIKE %:name% AND st.fromStorage.location LIKE %:location%")
    List<StockTransfer> searchFromStorageByNameAndLocation(
            @Param("name") String name,
            @Param("location") String location);
    //nove metode
    List<StockTransfer> findByFromStorage_Capacity(BigDecimal capacity);
    List<StockTransfer> findByToStorage_Capacity(BigDecimal capacity);
    List<StockTransfer> findByFromStorage_CapacityGreaterThan(BigDecimal capacity);
    List<StockTransfer> findByToStorage_CapacityGreaterThan(BigDecimal capacity);
    List<StockTransfer> findByFromStorage_CapacityLessThan(BigDecimal capacity);
    List<StockTransfer> findByToStorage_CapacityLessThan(BigDecimal capacity);
    List<StockTransfer> findByFromStorage_CapacityAndType(BigDecimal capacity, StorageType type);
    List<StockTransfer> findByToStorage_CapacityAndType(BigDecimal capacity, StorageType type);
    @Query("SELECT st FROM StockTransfer st WHERE st.fromStorage.capacity >= :capacity AND st.fromStorage.type = :type")
    List<StockTransfer> findByFromStorage_CapacityGreaterThanAndType(@Param("capacity") BigDecimal capacity,@Param("type") StorageType type);
    @Query("SELECT st FROM StockTransfer st WHERE st.toStorage.capacity >= :capacity AND st.toStorage.type = :type")
    List<StockTransfer> findByToStorage_CapacityGreaterThanAndType(@Param("capacity") BigDecimal capacity,@Param("type") StorageType type);
    @Query("SELECT st FROM StockTransfer st WHERE st.fromStorage.capacity <= :capacity AND st.fromStorage.type = :type")
    List<StockTransfer> findByFromStorage_CapacityLessThanAndType(@Param("capacity") BigDecimal capacity,@Param("type") StorageType type);
    @Query("SELECT st FROM StockTransfer st WHERE st.toStorage.capacity <= :capacity AND st.toStorage.type = :type")
    List<StockTransfer> findByToStorage_CapacityLessThanAndType(@Param("capacity") BigDecimal capacity,@Param("type") StorageType type);
    
    //boolean
    boolean existsByFromStorage_Id(Long fromStorageId);
    boolean existsByToStorage_Id(Long toStorageId);
    boolean existsByStatus(TransferStatus status);
    boolean existsByFromStorage_Capacity(BigDecimal capacity);
    boolean existsByToStorage_Capacity(BigDecimal capacity);
    boolean existsByTransferDate(LocalDate date);
    boolean existsByStatusAndTransferDateBetween(TransferStatus status, LocalDate from, LocalDate to);
    boolean existsByFromStorage_CapacityGreaterThan(BigDecimal capacity);
    boolean existsByFromStorage_CapacityLessThan(BigDecimal capacity);
    boolean existsByToStorage_Type(StorageType type);
    boolean existsByToStorage_CapacityGreaterThan(BigDecimal capacity);
    boolean existsByToStorage_CapacityLessThan(BigDecimal capacity);
    boolean existsByFromStorage_Type(StorageType type);
    @Query("SELECT st FROM StockTransfer st WHERE st.fromStorage.type = :fromType AND st.toStorage.type = :toType")
    boolean existsByFromAndToStorageType(@Param("fromType") StorageType fromType,@Param("toType") StorageType toType);
    @Query("SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END FROM StockTransfer st WHERE st.fromStorage.capacity >= :capacity AND st.fromStorage.type = :fromType")
    boolean existsFromStorageByCapacityGreaterThanAndType(@Param("capacity") BigDecimal capacity, @Param("fromType") StorageType type);
    @Query("SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END FROM StockTransfer st WHERE st.toStorage.capacity >= :capacity AND st.toStorage.type = :toType")
    boolean existsToStorageByCapacityGreaterThanAndType(@Param("capacity") BigDecimal capacity, @Param("toType") StorageType type);
    @Query("SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END FROM StockTransfer st WHERE st.fromStorage.capacity < :capacity AND st.fromStorage.type = :fromType")
    boolean existsFromStorageByCapacityLessThanAndType(@Param("capacity") BigDecimal capacity, @Param("fromType") StorageType type);
    @Query("SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END FROM StockTransfer st WHERE st.toStorage.capacity <= :capacity AND st.toStorage.type = :toType")
    boolean existsToStorageByCapacityLessThanAndType(@Param("capacity") BigDecimal capacity, @Param("toType") StorageType type);
}
