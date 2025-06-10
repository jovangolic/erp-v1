package com.jovan.erp_v1.repository;

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

}
