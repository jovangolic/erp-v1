package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

    List<Storage> findByType(StorageType type);
    List<Storage> findByName(String name);
    List<Storage> findByLocation(String location);
    List<Storage> findByCapacity(BigDecimal capacity);
    List<Storage> findByNameAndLocation(String name, String location);
    List<Storage> findByTypeAndCapacityGreaterThan(StorageType type, BigDecimal capacity);
    @Query("SELECT s FROM Storage s WHERE SIZE(s.goods) > :minCount")
    List<Storage> findStoragesWithMinGoods(@Param("minCount") Integer minCount);
    List<Storage> findByNameContainingIgnoreCase(String name);
    
    //nove metode
    List<Storage> findByTypeAndCapacityLessThan(StorageType type, BigDecimal capacity);
    List<Storage> findByCapacityGreaterThan(BigDecimal capacity);
    List<Storage> findByCapacityLessThan(BigDecimal capacity);
    List<Storage> findByNameAndLocationAndCapacity(String name, String location, BigDecimal capacity);
    List<Storage> findByTypeAndLocation(StorageType type, String location);
    List<Storage> findByTypeAndName(StorageType type, String name);
    List<Storage> findByLocationAndCapacity(StorageType type, BigDecimal capacity);
    List<Storage> findByTypeAndLocationAndCapacity(StorageType type, String location, BigDecimal capacity);
    List<Storage> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);
    List<Storage> findByCapacityBetween(BigDecimal min, BigDecimal max);
    List<Storage> findByTypeOrderByCapacityDesc(StorageType type);
    List<Storage> findByLocationOrderByNameAsc(String location);
    @Query("SELECT s FROM Storage s WHERE s.goods IS EMPTY")
    List<Storage> findStoragesWithoutGoods();
    @Query("SELECT s FROM Storage s WHERE SIZE(s.shelves) = :shelfCount")
    List<Storage> findByExactShelfCount(@Param("shelfCount") Integer shelfCount);
    List<Storage> findByLocationContainingIgnoreCaseAndType(String location, StorageType type);
    @Query("SELECT s FROM Storage s WHERE SIZE(s.materials) > 0")
    List<Storage> findStoragesWithMaterials();
    @Query("SELECT s FROM Storage s WHERE SIZE(s.workCenters) > 0")
    List<Storage> findStoragesWithWorkCenters();
    @Query("SELECT s FROM Storage s WHERE SIZE(s.shelves) = 0")
    List<Storage> findStoragesWithoutShelves();
    @Query("SELECT s FROM Storage s WHERE s.capacity > s.usedCapacity")
    List<Storage> findAvailableStorages();
    @Query("SELECT s FROM Storage s WHERE s.status = 'ACTIVE' AND s.capacity > :minCapacity")
    List<Storage> findSuitableStoragesForShipment(@Param("minCapacity") BigDecimal minCapacity);
    @Query("""
    	    SELECT s FROM Storage s
    	    WHERE s.goods IS EMPTY
    	      AND s.shelves IS EMPTY
    	      AND s.materials IS EMPTY
    	      AND s.outgoingTransfers IS EMPTY
    	      AND s.incomingTransfers IS EMPTY
    	      AND s.outgoingMaterialMovements IS EMPTY
    	      AND s.incomingMaterialMovements IS EMPTY
    	      AND s.outgoingShipments IS EMPTY
    	""")
    List<Storage> findEmptyStorages();
    @Query("""
    	    SELECT COUNT(s) > 0 FROM Storage s
    	    WHERE s.type = :type AND s.goods IS EMPTY AND s.materials IS EMPTY
    	""")
    List<Storage> findStorageWithoutGoodsAndMaterialsByType(@Param("type") StorageType type);
    @Query("""
    	    SELECT s FROM Storage s
    	    WHERE s.type = :type
    	      AND s.goods IS NOT EMPTY
    	      AND s.materials IS NOT EMPTY
    	""")
    List<Storage> findStorageWithGoodsAndMaterialsByType(@Param("type") StorageType type);
    @Query("""
    	    SELECT s FROM Storage s
    	    WHERE s.type = :type
    	      AND (s.goods IS NOT EMPTY OR s.materials IS NOT EMPTY)
    	""")
    List<Storage> findStorageWithGoodsOrMaterialsByType(@Param("type") StorageType type);
    List<Storage> findAllByType(StorageType type);
    @Query("SELECT s FROM Storage s WHERE s.type = :type AND s.goods IS EMPTY AND s.materials IS EMPTY")
    List<Storage> findEmptyStorageByType(@Param("type") StorageType type);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'PRODUCTION'")
    List<Storage> findProductionStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'DISTRIBUTION'")
    List<Storage> findDistributionStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'OPEN'")
    List<Storage> findOpenStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'CLOSED'")
    List<Storage> findClosedStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'INTERIM'")
    List<Storage> findInterimStorage();
    
    
    //boolean
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = :type")
    boolean existsByType(@Param("type") StorageType type);
    boolean existsByNameContainingIgnoreCase(String name);
    boolean existsByLocationContainingIgnoreCase(String location);
    boolean existsByLocationAndCapacity(StorageType type, BigDecimal capacity);
    boolean existsByNameAndLocationAndCapacity(String name, String location, BigDecimal capacity);
    boolean existsByCapacity(BigDecimal capacity);
    boolean existsByCapacityGreaterThan(BigDecimal capacity);
    boolean existsByCapacityLessThan(BigDecimal capacity);
    boolean existsByNameAndLocation(String name, String location);
    boolean existsByNameAndCapacity(String name, BigDecimal capacity);
    boolean existsByTypeAndLocation(StorageType type, String location);
    boolean existsByTypeAndLocationAndCapacity(StorageType type, String location, BigDecimal capacity);
    boolean existsByTypeAndCapacityGreaterThan(StorageType type, BigDecimal capacity);
    boolean existsByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE SIZE(s.goods) > :minCount")
    boolean existsByGoodsGreaterThan(@Param("minCount") Integer minCount);
    boolean existsByCapacityBetween(BigDecimal min, BigDecimal max);
    boolean existsByCapacityGreaterThanAndLocation(BigDecimal capacity, String location);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = :type AND SIZE(s.shelves) = 0")
    boolean existsByTypeAndNoShelves(@Param("type") StorageType type);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.workCenters IS NOT EMPTY")
    boolean existsStoragesWithWorkCenters();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.materials IS NOT EMPTY")
    boolean existsStoragesWithMaterials();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.goods IS EMPTY")
    boolean existsStoragesWithoutGoods();
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Storage s WHERE SIZE(s.shelves) = :shelfCount")
    boolean existsByExactShelfCount(@Param("shelfCount") Integer shelfCount);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.location = :location")
    boolean existsByLocation(@Param("location") String location);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE LOWER(s.location) LIKE LOWER(CONCAT('%', :location, '%')) AND s.type = :type")
    boolean existsByLocationContainingIgnoreCaseAndType(@Param("location") String location, @Param("type") StorageType type);
    @Query("""
    	    SELECT COUNT(s) > 0 FROM Storage s
    	    WHERE s.goods IS EMPTY AND s.materials IS EMPTY
    	""")
    boolean existsStorageWithoutGoodsAndMaterials();
    boolean existsByStatus(StorageStatus status);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.shelves IS EMPTY")
    boolean existsStorageWithoutShelves();
    @Query("""
    	    SELECT COUNT(s) > 0 FROM Storage s
    	    WHERE s.goods IS EMPTY
    	      AND s.shelves IS EMPTY
    	      AND s.materials IS EMPTY
    	      AND s.outgoingTransfers IS EMPTY
    	      AND s.incomingTransfers IS EMPTY
    	      AND s.outgoingMaterialMovements IS EMPTY
    	      AND s.incomingMaterialMovements IS EMPTY
    	      AND s.outgoingShipments IS EMPTY
    	""")
    boolean existsCompletelyEmptyStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = :type AND s.goods IS NOT EMPTY AND s.materials IS NOT EMPTY")
    boolean existsStorageWithGoodsAndMaterialsByType(@Param("type") StorageType type);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = :type AND (s.goods IS NOT EMPTY OR s.materials IS NOT EMPTY)")
    boolean existsStorageWithGoodsOrMaterialsByType(@Param("type") StorageType type);
    @Query("""
    	    SELECT COUNT(s) > 0 FROM Storage s
    	    WHERE s.type = :type AND s.goods IS EMPTY AND s.materials IS EMPTY
    	""")
    boolean existsStorageWithoutGoodsAndMaterialsByType(@Param("type") StorageType type);
    @Query("""
    	    SELECT COUNT(s) > 0 FROM Storage s
    	    WHERE s.type = :type AND s.goods IS EMPTY OR s.materials IS EMPTY
    	""")
    boolean existsStorageWithoutGoodsOrMaterialsByType(@Param("type") StorageType type);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = :type AND s.goods IS EMPTY AND s.materials IS EMPTY")
    boolean existsEmptyStorageByType(@Param("type") StorageType type);
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'PRODUCTION'")
    boolean existsProductionStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'DISTRIBUTION'")
    boolean existsDistributionStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'OPEN'")
    boolean existsOpenStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'CLOSED'")
    boolean existsClosedStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.type = 'INTERIM'")
    boolean existsInterimStorage();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.status = 'AVAILABLE'")
    boolean existsAvailableStorages();
    @Query("SELECT COUNT(s) > 0 FROM Storage s WHERE s.status = :status AND s.goods IS EMPTY AND s.materials IS EMPTY")
    boolean existsAvailableStorages(@Param("status") StorageStatus status);
}
