package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.dto.StorageGoodsCountDTO;
import com.jovan.erp_v1.dto.StorageIncomingMovementCountDTO;
import com.jovan.erp_v1.dto.StorageIncomingTransferCountDTO;
import com.jovan.erp_v1.dto.StorageMaterialCountDTO;
import com.jovan.erp_v1.dto.StorageOutgoingMovementCountDTO;
import com.jovan.erp_v1.dto.StorageOutgoingTransferCountDTO;
import com.jovan.erp_v1.dto.StorageShelfCountDTO;
import com.jovan.erp_v1.dto.StorageShipmentCountDTO;
import com.jovan.erp_v1.dto.StorageWorkCenterCountDTO;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    List<Supply> findByStorage_Id(Long storageId);
    List<Supply> findByUpdates(LocalDateTime updates);
    List<Supply> findByGoodsContaining(Goods good);
    List<Supply> findByGoods_Name(String name);
    @Query("SELECT s FROM Supply s JOIN s.goods g WHERE g.name = :name")
    List<Supply> findSuppliesByGoodsName(@Param("name") String name);
    @Query("SELECT s FROM Supply s WHERE s.quantity >= :minQuantity")
    List<Supply> findSuppliesWithMinQuantity(@Param("minQuantity") BigDecimal minQuantity);
    @Query("SELECT s FROM Supply s JOIN s.goods g WHERE TYPE(g) = :clazz")
    List<Supply> findSuppliesByGoodsClass(@Param("clazz") Class<? extends Goods> clazz);
    @Query("SELECT s FROM Supply s WHERE s.storage.id = :storageId")
    List<Supply> findSuppliesByStorageId(@Param("storageId") Long storageId);

    //nove metode
    List<Supply> findByUpdatesBetween(LocalDateTime start, LocalDateTime end);
    List<Supply> findByStorage_NameContainingIgnoreCase(String name);
    List<Supply> findByStorage_LocationContainingIgnoreCase(String locatin);
    List<Supply> findByStorage_Capacity(BigDecimal capacity);
    List<Supply> findByStorage_CapacityGreaterThan(BigDecimal capacity);
    List<Supply> findByStorage_CapacityLessThan(BigDecimal capacity);
    List<Supply> findByStorage_Type(StorageType type);
    List<Supply> findByStorage_Status(StorageStatus status);
    @Query("SELECT s FROM Supply s WHERE s.storage.type = :type AND s.storage.capacity = :capacity")
    List<Supply> findByStorageTypeAndCapacity(@Param("type") StorageType type, @Param("capacity") BigDecimal capacity);
    @Query("SELECT s FROM Supply s WHERE s.storage.type = :type AND s.storage.capacity > :capacity")
    List<Supply> findByStorageTypeAndCapacityGreaterThan(@Param("type") StorageType type, @Param("capacity") BigDecimal capacity);
    @Query("SELECT s FROM Supply s WHERE s.storage.type = :type AND s.storage.capacity < :capacity")
    List<Supply> findByStorageTypeAndCapacityLessThan(@Param("type") StorageType type, @Param("capacity") BigDecimal capacity);
    @Query("SELECT s FROM Supply s WHERE s.storage.type = :type AND s.storage.status = :status")
    List<Supply> findByStorageTypeAndStatus(@Param("type") StorageType type, @Param("status") StorageStatus status);
    @Query("SELECT s FROM Supply s WHERE s.storage.type = :type AND LOWER(s.storage.location) = LOWER(:location)")
    List<Supply> findByStorageTypeAndLocation(@Param("type") StorageType type, @Param("location") String location);
    @Query("SELECT s FROM Supply s WHERE LOWER(s.storage.location) = LOWER(:location) AND s.storage.capacity = :capacity")
    List<Supply> findByStorageLocationAndCapacity(@Param("location") String location, @Param("capacity") BigDecimal capacity);
    @Query("SELECT s FROM Supply s WHERE LOWER(s.storage.location) = LOWER(:location) AND s.storage.capacity > :capacity")
    List<Supply> findByStorageLocationAndCapacityGreaterThan(@Param("location") String location, @Param("capacity") BigDecimal capacity);
    @Query("SELECT s FROM Supply s WHERE LOWER(s.storage.location) = LOWER(:location) AND s.storage.capacity < :capacity")
    List<Supply> findByStorageLocationAndCapacityLessThan(@Param("location") String location, @Param("capacity") BigDecimal capacity);
    List<Supply> findByStorage_CapacityBetween(BigDecimal min, BigDecimal max);
    @Query("SELECT new com.jovan.erp_v1.dto.StorageGoodsCountDTO(s.storage.name, COUNT(g)) FROM Supply s JOIN s.goods g GROUP BY s.storage.name")
    List<StorageGoodsCountDTO> countGoodsPerStorage();
    @Query("SELECT new com.jovan.erp_v1.dto.StorageShelfCountDTO(s.name, COUNT(sh))  FROM Storage s JOIN s.shelves sh GROUP BY s.name")
    List<StorageShelfCountDTO> countShelvesPerStorage();
    @Query("SELECT new com.jovan.erp_v1.dto.StorageShipmentCountDTO(s.name, COUNT(sh)) FROM Storage s JOIN s.outgoingShipments sh GROUP BY s.name")
    List<StorageShipmentCountDTO> countOutgoingShipmentsPerStorage();
    @Query("SELECT new com.jovan.erp_v1.dto.StorageOutgoingTransferCountDTO(s.name, COUNT(t)) FROM Storage s JOIN s.outgoingTransfers t GROUP BY s.name")
    List<StorageOutgoingTransferCountDTO> countOutgoingTransfersPerStorage();
    @Query("SELECT new com.jovan.erp_v1.dto.StorageIncomingTransferCountDTO(s.name, COUNT(t)) FROM Storage s JOIN s.incomingTransfers t GROUP BY s.name")
    List<StorageIncomingTransferCountDTO> countIncomingTransfersPerStorage();
    @Query("SELECT new com.jovan.erp_v1.dto.StorageMaterialCountDTO(s.name, COUNT(m))  FROM Storage s JOIN s.materials m GROUP BY s.name")
    List<StorageMaterialCountDTO> countMaterialsPerStorage();
    @Query("SELECT new com.jovan.erp_v1.dto.StorageOutgoingMovementCountDTO(s.name, COUNT(m)) FROM Storage s JOIN s.outgoingMaterialMovements m GROUP BY s.name")
    List<StorageOutgoingMovementCountDTO> countOutgoingMaterialMovementsPerStorage();
    @Query("SELECT new com.jovan.erp_v1.dto.StorageIncomingMovementCountDTO(s.name, COUNT(m)) FROM Storage s JOIN s.incomingMaterialMovements m GROUP BY s.name")
    List<StorageIncomingMovementCountDTO> countIncomingMaterialMovementsPerStorage();
    @Query("SELECT new com.jovan.erp_v1.dto.StorageWorkCenterCountDTO(s.name, COUNT(w)) FROM Storage s JOIN s.workCenters w GROUP BY s.name")
    List<StorageWorkCenterCountDTO> countWorkCentersPerStorage();
    @Query("SELECT s FROM Supply s WHERE SIZE(s.storage.goods) > :minGoodsCount")
    List<Supply> findByStorageWithMinGoodsCount(@Param("minGoodsCount") Integer minGoodsCount);
    @Query("SELECT s FROM Supply s WHERE SIZE(s.storage.goods) < :maxGoodsCount")
    List<Supply> findByStorageWithMaxGoodsCount(@Param("minGoodsCount") Integer maxGoodsCount);
    @Query("SELECT s FROM Supply s JOIN s.storage.materials m WHERE m.name = :name")
    List<Supply> findByStorageContainingMaterial(@Param("name") String name);
    @Query("SELECT s FROM Supply s JOIN s.storage.shelves sh WHERE sh.storage.capacity >= :minShelfCapacity")
    List<Supply> findByShelfCapacityInStorage(@Param("minShelfCapacity") BigDecimal minShelfCapacity);
    @Query("SELECT s FROM Supply s WHERE s.storage IN (SELECT t.fromStorage FROM StockTransfer t)")
    List<Supply> findByStorageUsedAsTransferOrigin();
    @Query("SELECT s FROM Supply s WHERE SIZE(s.storage.workCenters) > 0")
    List<Supply> findSuppliesWithWorkCenters();
    List<Supply> findByStorage_UsedCapacityGreaterThan(BigDecimal usedCapacity);
    List<Supply> findByStorage_UsedCapacityLessThan(BigDecimal usedCapacity);
    @Query("SELECT s FROM Supply s JOIN s.storage.shelves sh WHERE sh.storage IS NULL")
    List<Supply> findByStorageWithEmptyShelves();
    @Query("SELECT s FROM Supply s WHERE s.storage IN (SELECT t.toStorage FROM StockTransfer t)")
    List<Supply> findByStorageUsedAsTransferDestination();
    @Query("SELECT s FROM Supply s WHERE s.storage.type = 'PRODUCTION'")
    List<Supply> findProductionStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.type = 'DISTRIBUTION'")
    List<Supply> findDistributionStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.type = 'OPEN'")
    List<Supply> findOpenStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.type = 'CLOSED'")
    List<Supply> findClosedStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.type = 'INTERIM'")
    List<Supply> findInterimStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.type = 'AVAILABLE'")
    List<Supply> findAvailableStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.status =  'ACTIVE'")
    List<Supply> findActiveStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.status =  'UNDER_MAINTENANCE'")
    List<Supply> findUnderMaintenanceStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.status =  'DECOMMISSIONED'")
    List<Supply> findDecommissionedStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.status =  'RESERVED'")
    List<Supply> findReservedStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.status =  'TEMPORARY'")
    List<Supply> findTemporaryStorage();
    @Query("SELECT s FROM Supply s WHERE s.storage.status =  'FULL'")
    List<Supply> findFullStorage();
    
    //boolean
    boolean existsByStorage_Id(Long storageId);
    boolean existsByUpdatesBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByStorage_Capacity(BigDecimal capacity);
    boolean existsByStorage_CapacityGreaterThan(BigDecimal capacity);
    boolean existsByStorage_CapacityLessThan(BigDecimal capacity);
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = :type")
    boolean existsByStorageType(@Param("type") StorageType type);
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.status = :status")
    boolean existsByStorageStatus(@Param("status") StorageStatus status);
    boolean existsByStorage_CapacityBetween(BigDecimal min, BigDecimal max);
    boolean existsByStorage_IdAndGoodsIsNotEmpty(Long storageId);
}
