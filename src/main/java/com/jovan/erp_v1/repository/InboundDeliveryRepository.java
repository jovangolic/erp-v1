package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.InboundDelivery;

@Repository
public interface InboundDeliveryRepository extends JpaRepository<InboundDelivery, Long> {

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    Optional<InboundDelivery> findById(Long id);

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<InboundDelivery> findAll();

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<InboundDelivery> findByStatus(DeliveryStatus status);

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<InboundDelivery> findBySupplyId(Long supplyId);

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    //nove metode
    List<InboundDelivery> findByDeliveryDateBetween(LocalDate from, LocalDate to);
    @Query("SELECT d FROM InboundDelivery d WHERE d.supply.storage.id = :storageId")
    List<InboundDelivery> findBySupply_Storage_Id(@Param("storageId") Long storageId);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%'))")
    List<InboundDelivery> findBySupply_Storage_NameContainingIgnoreCase(@Param("storageName") String storageName);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%'))")
    List<InboundDelivery> findBySupply_Storage_LocationContainingIgnoreCase(@Param("storageLocation") String storageLocation);
    @Query("SELECT d FROM InboundDelivery d WHERE d.supply.storage.capacity = :storageCapacity")
    List<InboundDelivery> findBySupply_StorageCapacity(@Param("storageCapacity") BigDecimal storageCapacity);
    @Query("SELECT d FROM InboundDelivery d WHERE d.supply.storage.capacity >= :storageCapacity")
    List<InboundDelivery> findBySupply_StorageCapacityGreaterThan(@Param("storageCapacity") BigDecimal storageCapacity);
    @Query("SELECT d FROM InboundDelivery d WHERE d.supply.storage.capacity <= :storageCapacity")
    List<InboundDelivery> findBySupply_StorageCapacityLessThan(@Param("storageCapacity") BigDecimal storageCapacity);
    @Query("SELECT d FROM InboundDelivery d WHERE d.supply.storage.type = :type")
    List<InboundDelivery> findBySupply_StorageType(@Param("type") StorageType type);
    @Query("SELECT d FROM InboundDelivery d WHERE d.supply.storage.status = :status")
    List<InboundDelivery> findBySupply_StorageStatus(@Param("status") StorageStatus status);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%')) AND d.supply.storage.type = :type")
    List<InboundDelivery> findBySupply_StorageNameContainingIgnoreCaseAndType(@Param("storageName") String storageName,@Param("type") StorageType type);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%')) AND d.supply.storage.status = :status")
    List<InboundDelivery> findBySupply_StorageNameContainingIgnoreCaseAndStatus(@Param("storageName") String storageName,@Param("status") StorageStatus status);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND d.supply.storage.type = :type")
    List<InboundDelivery> findBySupply_StorageLocationContainingIgnoreCaseAndType(@Param("storageLocation") String storageLocation,@Param("type") StorageType type);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND d.supply.storage.status = :status")
    List<InboundDelivery> findBySupply_StorageLocationContainingIgnoreCaseAndStatus(@Param("storageLocation") String storageLocation,@Param("status") StorageStatus status);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.name) LIKE LOWER(CONCAT('%', :storageName,'%')) AND d.supply.storage.capacity = :capacity")
    List<InboundDelivery> findBySupply_StorageNameContainingIgnoreCaseAndCapacity(@Param("storageName") String storageName,@Param("capacity") BigDecimal capacity);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.name) LIKE LOWER(CONCAT('%', :storageName,'%')) AND d.supply.storage.capacity >= :capacity")
    List<InboundDelivery> findBySupply_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(@Param("storageName") String storageName,@Param("capacity") BigDecimal capacity);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%')) AND d.supply.storage.capacity <= :capacity")
    List<InboundDelivery> findBySupply_StorageNameContainingIgnoreCaseAndCapacityLessThan(@Param("storageName") String storageName,@Param("capacity") BigDecimal capacity);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%')) AND LOWER(d.supply.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%'))")
    List<InboundDelivery> findBySupply_StorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(@Param("storageName") String storageName,@Param("storageLocation") String storageLocation);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND d.supply.storage.capacity = :capacity")
    List<InboundDelivery> findBySupply_StorageLocationContainingIgnoreCaseAndCapacity(@Param("storageLocAtion") String storageLocation, @Param("capacity") BigDecimal capacity);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND d.supply.storage.capacity >= :capacity")
    List<InboundDelivery> findBySupply_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(@Param("storageLocAtion") String storageLocation, @Param("capacity") BigDecimal capacity);
    @Query("SELECT d FROM InboundDelivery d WHERE LOWER(d.supply.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND d.supply.storage.capacity <= :capacity")
    List<InboundDelivery> findBySupply_StorageLocationContainingIgnoreCaseAndCapacityLessThan(@Param("storageLocAtion") String storageLocation, @Param("capacity") BigDecimal capacity);
    @Query("""
    	       SELECT d
    	       FROM InboundDelivery d
    	       WHERE d.supply.storage IS NOT NULL
    	         AND (d.supply.storage.hasShelvesFor = false OR d.supply.storage.hasShelvesFor IS NULL)
    	       """)
    List<InboundDelivery> findByStorageWithoutShelvesOrUnknown();
    List<InboundDelivery> findBySupply_Quantity(BigDecimal quantity);
    List<InboundDelivery> findBySupply_QuantityGreaterThan(BigDecimal quantity);
    List<InboundDelivery> findBySupply_QuantityLessThan(BigDecimal quantity);
    List<InboundDelivery> findBySupply_QuantityBetween(BigDecimal min, BigDecimal max);
    
    List<InboundDelivery> findBySupply_Updates(LocalDateTime updates);
    List<InboundDelivery> findBySupply_UpdatesAfter(LocalDateTime updates);
    List<InboundDelivery> findBySupply_UpdatesBefore(LocalDateTime updates);
    List<InboundDelivery> findBySupply_UpdatesBetween(LocalDateTime updatesFrom, LocalDateTime updatesTo);
}
