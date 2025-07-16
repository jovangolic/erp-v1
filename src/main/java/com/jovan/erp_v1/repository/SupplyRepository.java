package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    List<Supply> findByStorage_Type_AndCapacity(StorageType type, BigDecimal capacity);
    List<Supply> findByStorage_Type_AndCapacityGreaterThan(StorageType type, BigDecimal capacity);
    List<Supply> findByStorage_Type_AndCapacityLessThan(StorageType type, BigDecimal capacity);
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.type = 'PRODUCTION'")
    List<Supply> findProductionStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'DISTRIBUTION'")
    List<Supply> findDistributionStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'OPEN'")
    List<Supply> findOpenStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'CLOSED'")
    List<Supply> findClosedStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'INTERIM'")
    List<Supply> findInterimStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'AVAILABLE'")
    List<Supply> findAvailableStorage();
    
    //boolean
    boolean existsByStorage_Id(Long storageId);
    boolean existsByStorage_NameContainingIgnoreCase(String name);
    boolean existsByStorage_LocationContainingIgnoreCase(String locatin);
    boolean existsByUpdatesBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByStorage_Capacity(BigDecimal capacity);
    boolean existsByStorage_CapacityGreaterThan(BigDecimal capacity);
    boolean existsByStorage_CapacityLessThan(BigDecimal capacity);
    boolean existsByStorage_Type(StorageType type);
    boolean existsByStorage_Status(StorageStatus status);
    boolean existsByStorage_Type_AndCapacity(StorageType type, BigDecimal capacity);
    boolean existsByStorage_Type_AndCapacityGreaterThan(StorageType type, BigDecimal capacity);
    boolean existsByStorage_Type_AndCapacityLessThan(StorageType type, BigDecimal capacity);
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'PRODUCTION'")
    boolean existsProductionStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'DISTRIBUTION'")
    boolean existsDistributionStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'OPEN'")
    boolean existsOpenStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'CLOSED'")
    boolean existsClosedStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'INTERIM'")
    boolean existsInterimStorage();
    @Query("SELECT COUNT(s) > 0 FROM Supply s WHERE s.storage.type = 'AVAILABLE'")
    boolean existsAvailableStorages();
}
