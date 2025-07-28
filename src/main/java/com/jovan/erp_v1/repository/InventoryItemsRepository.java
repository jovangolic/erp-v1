package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.InventoryItems;
import com.jovan.erp_v1.request.InventoryItemCalculateRequest;
import com.jovan.erp_v1.response.InventoryItemStorageCapacityResponse;
import com.jovan.erp_v1.response.InventorySummaryResponse;
import com.jovan.erp_v1.response.StorageCapacityAndInventorySummaryResponse;
import com.jovan.erp_v1.response.StorageCapacityResponse;
import com.jovan.erp_v1.response.StorageItemSummaryResponse;

import jakarta.transaction.Transactional;

@Repository
public interface InventoryItemsRepository extends JpaRepository<InventoryItems, Long> {

	List<InventoryItems> findByInventoryId(Long inventoryId);
	List<InventoryItems> findByProductId(Long productId);
	@Query("SELECT it FROM InventoryItems it WHERE it.product.name = :productName")
	List<InventoryItems> findByProductName(@Param("productName") String productName);
	List<InventoryItems> findByDifferenceGreaterThan(BigDecimal threshold);
	List<InventoryItems> findByQuantity(BigDecimal quantity);
	List<InventoryItems> findByItemCondition(BigDecimal itemCondition);
	// void deleteAllByInventoryId(Long inventoryId);
	@Modifying
	@Query("DELETE FROM InventoryItems i WHERE i.inventory.id = :inventoryId")
	void deleteAllByInventoryId(@Param("inventoryId") Long inventoryId);
	@Transactional
	void deleteAllById(Long inventoryId);

	//nove metode
	List<InventoryItems> findByDifference(BigDecimal difference);
	List<InventoryItems> findByDifferenceLessThan(BigDecimal difference);
	List<InventoryItems> findByQuantityGreaterThan(BigDecimal quantity);
	List<InventoryItems> findByQuantityLessThan(BigDecimal quantity);
	List<InventoryItems> findByItemConditionGreaterThan(BigDecimal itemCondition);
	List<InventoryItems> findByItemConditionLessThan(BigDecimal itemCondition);
	List<InventoryItems> findByItemConditionAndQuantity(BigDecimal itemCondition, BigDecimal quantity);
	@Query("SELECT new com.jovan.erp_v1.request.InventoryItemCalculateRequest(i.id, i.quantity, i.itemCondition) " +
		       "FROM InventoryItems i WHERE i.inventory.id = :inventoryId")
	List<InventoryItemCalculateRequest> findInventoryItemsForCalculation(@Param("inventoryId") Long inventoryId);
	@Query("SELECT new com.jovan.erp_v1.request.InventoryItemCalculateRequest(i.id, i.quantity, i.itemCondition) " +
		       "FROM InventoryItems i WHERE i.inventory.id = :inventoryId")
	List<InventoryItemCalculateRequest> findItemsForShortageAllowed(@Param("inventoryId") Long inventoryId);
	@Query("SELECT i FROM InventoryItems i WHERE i.inventory.storageEmployee.id = :storageEmployeeId")
	List<InventoryItems> findByInventory_StorageEmployee_Id(@Param("storageEmployeeId") Long storageEmployeeId);
	@Query("SELECT i FROM InventoryItems i WHERE i.inventory.storageForeman.id = :storageForemanId")
	List<InventoryItems> findByInventory_StorageForeman_Id(@Param("storageEmployeeId") Long storageForemanId);
	List<InventoryItems> findByInventoryDate(LocalDate date);
	List<InventoryItems> findByInventoryDateBetween(LocalDate start, LocalDate end);
	List<InventoryItems> findByInventoryDateAfter(LocalDate date);
	List<InventoryItems> findByInventoryDateBefore(LocalDate date);
	List<InventoryItems> findByInventory_Status(InventoryStatus status);
	Boolean existsByInventory_Aligned(Boolean aligned);
	List<InventoryItems> findByInventoryAlignedFalse();
	@Query("SELECT i FROM InventoryItems i WHERE i.inventory.status = :status AND i.inventory.storageEmployee.id = :storageEmployeeId")
	List<InventoryItems> findByInventoryStatusAndInventoryStorageEmployeeId(@Param("status") InventoryStatus status,@Param("storageEmployeeId") Long storageEmployeeId);
	@Query("SELECT i FROM InventoryItems i WHERE i.inventory.status = :status AND i.inventory.storageForeman.id = :storageForemanId")
	List<InventoryItems> findByInventoryStatusAndInventoryStorageForemanId(@Param("status") InventoryStatus status,@Param("storageForemanId") Long storageForemanId);
	Boolean existsByInventoryAlignedFalseAndInventoryStorageEmployeeId(Long employeeId);
	@Query("SELECT i FROM InventoryItems i WHERE i.difference IS NOT NULL AND i.difference <> 0")
	List<InventoryItems> findItemsWithNonZeroDifference();
	List<InventoryItems> findByInventoryStatusAndInventoryAlignedFalse(InventoryStatus status);
	List<InventoryItems> findByInventoryDateAndInventoryStorageForemanId(LocalDate date, Long foremanId);
	@Query("SELECT new com.jovan.erp_v1.response.InventorySummaryResponse(i.inventory.id, COUNT(i), SUM(i.quantity)) " +
		       "FROM InventoryItems i GROUP BY i.inventory.id")
	List<InventorySummaryResponse> fetchInventorySummaries();
	List<InventoryItems> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
	List<InventoryItems> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<InventoryItems> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
	List<InventoryItems> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
	List<InventoryItems> findByProduct_SupplierType(SupplierType supplierType);
	List<InventoryItems> findByProduct_StorageType(StorageType storageType);
	List<InventoryItems> findByProduct_GoodsType(GoodsType type);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.storage.id = :storageId")
	List<InventoryItems> findByProduct_StorageId(@Param("storageId") Long storageId);
	@Query("SELECT i FROM InventoryItems i WHERE LOWER(i.product.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%'))")
	List<InventoryItems> findByProduct_StorageNameContainingIgnoreCase(@Param("storageName") String storageName);
	@Query("SELECT i FROM InventoryItems i WHERE LOWER(i.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%'))")
	List<InventoryItems> findByProduct_StorageLocationContainingIgnoreCase(@Param("storageLocation") String storageLocation);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.storage.capacity = :capacity")
	List<InventoryItems> findByProduct_StorageCapacity(@Param("capacity") BigDecimal capacity);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.storage.capacity >= :capacity")
	List<InventoryItems> findByProduct_StorageCapacityGreaterThan(@Param("capacity") BigDecimal capacity);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.storage.capacity <= :capacity")
	List<InventoryItems> findByProduct_StorageCapacityLessThan(@Param("capacity") BigDecimal capacity);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.storage.status = :status")
	List<InventoryItems> findByProduct_Storage_Status(@Param("status") StorageStatus status);
	@Query("SELECT new com.jovan.erp_v1.response.InventoryItemStorageCapacityResponse(i.id, p.id, s.id, s.usedCapacity) " +
		       "FROM InventoryItems i " +
		       "JOIN i.product p " +
		       "JOIN p.storage s")
	List<InventoryItemStorageCapacityResponse> fetchUsedCapacitiesForItems();
	@Query("SELECT new com.jovan.erp_v1.response.StorageCapacityResponse(s.id, s.usedCapacity) " +
		       "FROM Storage s")
	List<StorageCapacityResponse> fetchAllStorageCapacities();
	@Query("SELECT new com.jovan.erp_v1.response.StorageItemSummaryResponse(s.id, SUM(i.quantity)) " +
		       "FROM InventoryItems i " +
		       "JOIN i.product p " +
		       "JOIN p.storage s " +
		       "GROUP BY s.id")
	List<StorageItemSummaryResponse> fetchItemQuantitiesPerStorage();
	@Query("""
		    SELECT new com.jovan.erp_v1.response.StorageCapacityAndInventorySummaryResponse(
		        s.id,
		        s.capacity,
		        s.usedCapacity,
		        SUM(i.quantity)
		    )
		    FROM InventoryItems i
		    JOIN i.product p
		    JOIN p.storage s
		    GROUP BY s.id, s.capacity, s.usedCapacity
		""")
	List<StorageCapacityAndInventorySummaryResponse> fetchStorageCapacityAndInventorySummary();
	@Query("""
		    SELECT new com.jovan.erp_v1.response.StorageCapacityAndInventorySummaryResponse(
		        s.id,
		        s.capacity,
		        s.usedCapacity,
		        SUM(i.quantity)
		    )
		    FROM InventoryItems i
		    JOIN i.product p
		    JOIN p.storage s
		    GROUP BY s.id, s.capacity, s.usedCapacity
		""")
	List<StorageCapacityAndInventorySummaryResponse> fetchDetailedStorageStats();
	@Query("SELECT i FROM InventoryItems i WHERE i.product.supply.id = :supplyId")
	List<InventoryItems> findByProduct_SupplyId(@Param("supplyId") Long supplyId);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.supply.quantity = :quantity")
	List<InventoryItems> findByProduct_SupplyQuantity(@Param("quantity") BigDecimal quantity);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.supply.quantity >= :quantity")
	List<InventoryItems> findByProduct_SupplyQuantityGreaterThan(@Param("quantity") BigDecimal quantity);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.supply.quantity <= :quantity")
	List<InventoryItems> findByProduct_SupplyQuantityLessThan(@Param("quantity") BigDecimal quantity);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.supply.updates = :updates")
	List<InventoryItems> findByProduct_SupplyUpdates(@Param("updates") LocalDateTime updates);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.supply.updates BETWEEN :start AND :end")
	List<InventoryItems> findByProduct_SupplyUpdatesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.supply.storage.id = :storageId")
	List<InventoryItems> findByProduct_SupplyStorageId(@Param("storageId") Long storageId);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.shelf.id = :shelfId")
	List<InventoryItems> findByProduct_ShelfId(@Param("shelfId") Long shelfId);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.shelf.rowCount = :rowCount")
	List<InventoryItems> findByProduct_ShelfRowCount(@Param("rowCount") Integer rowCount);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.shelf.cols = :cols")
	List<InventoryItems> findByProduct_ShelfCols(@Param("cols") Integer cols);
	@Query("SELECT i FROM InventoryItems i WHERE i.product.shelf IS NULL")
	List<InventoryItems> findInventoryItemsWithoutShelf();
	List<InventoryItems> findByProduct_ShelfIsNotNull();
}

