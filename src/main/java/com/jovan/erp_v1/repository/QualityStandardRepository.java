package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.QualityStandard;

@Repository
public interface QualityStandardRepository extends JpaRepository<QualityStandard, Long>, JpaSpecificationExecutor<QualityStandard> {

	List<QualityStandard> findByUnit(Unit unit);
	List<QualityStandard> findByDescriptionContainingIgnoreCase(String description);
	List<QualityStandard> findByMinValue(BigDecimal minValue);
	List<QualityStandard> findByMinValueGreaterThan(BigDecimal minValue);
	List<QualityStandard> findByMinValueLessThan(BigDecimal minValue);
	List<QualityStandard> findByMinValueBetween(BigDecimal min, BigDecimal max);
	List<QualityStandard> findByMaxValue(BigDecimal maxValue);
	List<QualityStandard> findByMaxValueGreaterThan(BigDecimal maxValue);
	List<QualityStandard> findByMaxValueLessThan(BigDecimal maxValue);
	List<QualityStandard> findByMaxValueBetween(BigDecimal min, BigDecimal max);
	long countByMinValueIsNotNull();
	long countByMaxValueIsNotNull();
	long countByMinValue(BigDecimal minValue);
	long countByMaxValue(BigDecimal maxValue);
	
	List<QualityStandard> findByProduct_Id(Long productId);
	List<QualityStandard> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
	List<QualityStandard> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<QualityStandard> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
	List<QualityStandard> findByProduct_CurrentQuantityBetween(BigDecimal min, BigDecimal max);
	List<QualityStandard> findByProduct_NameContainingIgnoreCase(String productName);
	List<QualityStandard> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
	List<QualityStandard> findByProduct_SupplierType(SupplierType supplierType);
	List<QualityStandard> findByProduct_StorageType(StorageType storageType);
	List<QualityStandard> findByProduct_GoodsType(GoodsType goodsType);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.storage.id = :storageId")
	List<QualityStandard> findByProduct_StorageId(@Param("storageId") Long storageId);
	@Query("SELECT qs FROM QualityStandard qs WHERE LOWER(qs.product.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%'))")
	List<QualityStandard> findByProduct_StorageNameContainingIgnoreCase(@Param("storageName") String storageName);
	@Query("SELECT qs FROM QualityStandard qs WHERE LOWER(qs.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%'))")
	List<QualityStandard> findByProduct_StorageLocationContainingIgnoreCase(@Param("storageLocation") String storageLocation);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.storage.capacity = :capacity")
	List<QualityStandard> findByProduct_StorageCapacity(@Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.storage.capacity >= :capacity")
	List<QualityStandard> findByProduct_StorageCapacityGreaterThan(@Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.storage.capacity <= :capacity")
	List<QualityStandard> findByProduct_StorageCapacityLessThan(@Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.storage.capacity IS NOT NULL AND qs.product.storage.capacity BETWEEN :min AND :max")
	List<QualityStandard> findByProduct_StorageCapacityBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
	@Query("SELECT qs FROM QualityStandard qs " +
		       "WHERE qs.product.storage.capacity IS NOT NULL " +
		       "AND qs.product.storage.capacity BETWEEN :min AND :max " +
		       "AND qs.product.storage.status = :status")
	List<QualityStandard> findByProductStorageCapacityBetweenAndStatus( @Param("min") BigDecimal min, @Param("max") BigDecimal max,@Param("status") StorageStatus status);
	@Query("SELECT qs FROM QualityStandard qs " +
		       "WHERE qs.product.storage.capacity IS NOT NULL " +
		       "AND qs.product.storage.capacity BETWEEN :min AND :max " +
		       "AND (:status IS NULL OR qs.product.storage.status = :status) " +
		       "AND (:type IS NULL OR qs.product.storage.type = :type)")
	List<QualityStandard> findByProductStorageCapacityStatusAndType( @Param("min") BigDecimal min, @Param("max") BigDecimal max, @Param("status") StorageStatus status,@Param("type") StorageType type);
	
	@Query("SELECT qs FROM QualityStandard qs WHERE LOWER(qs.product.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%')) AND qs.product.storage.capacity = :capacity")
	List<QualityStandard> findByProduct_StorageNameContainingIgnoreCaseAndCapacity(@Param("storageName") String storageName, @Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE LOWER(qs.product.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%')) AND qs.product.storage.capacity >= :capacity")
	List<QualityStandard> findByProduct_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(@Param("storageName") String storageName, @Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE LOWER(qs.product.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%')) AND qs.product.storage.capacity <= :capacity")
	List<QualityStandard> findByProduct_StorageNameContainingIgnoreCaseAndCapacityLessThan(@Param("storageName") String storageName, @Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.storage.capacity IS NOT NULL"
			+ " AND LOWER(qs.product.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%')) AND qs.product.storage.capacity BETWEEN :min AND :max")
	List<QualityStandard> findByProduct_StorageNameContainingIgnoreCaseAndCapacityBetween(@Param("storageName") String storageName, @Param("min") BigDecimal min, @Param("max") BigDecimal max);
	@Query("SELECT qs FROM QualityStandard qs WHERE LOWER(qs.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND qs.product.storage.capacity = :capacity")
	List<QualityStandard> findByProduct_StorageLocationContainingIgnoreCaseAndCapacity(@Param("storageLocation") String storageLocation, @Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE LOWER(qs.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND qs.product.storage.capacity >= :capacity")
	List<QualityStandard> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(@Param("storageLocation") String storageLocation, @Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE LOWER(qs.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND qs.product.storage.capacity <= :capacity")
	List<QualityStandard> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityLessThan(@Param("storageLocation") String storageLocation, @Param("capacity") BigDecimal capacity);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.storage.capacity IS NOT NULL"
			+ " AND LOWER(qs.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%')) AND qs.product.storage.capacity BETWEEN :min AND :max")
	List<QualityStandard> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityBetween(@Param("storageLocation") String storageLocation, @Param("min") BigDecimal min, @Param("max") BigDecimal max);
	
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.storage.hasShelvesFor IS NULL")
	Optional<QualityStandard>findByProduct_StorageHasShelvesForIsNull();
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.supply.id = :supplyId")
	List<QualityStandard> findByProduct_SupplyId(@Param("supplyId") Long supplyId);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.shelf.id = :shelfId")
	List<QualityStandard> findByProduct_ShelfId(@Param("shelfId") Long shelfId);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.shelf.rowCount = :rowCount")
	List<QualityStandard> findByProduct_ShelfRowCount(@Param("rows") Integer rowCount);
	@Query("SELECT qs FROM QualityStandard qs WHERE qs.product.shelf.cols = :cols")
	List<QualityStandard> findByProduct_ShelfCols(@Param("cols") Integer cols);
	@Query("""
		       SELECT qs 
		       FROM QualityStandard qs
		       WHERE (:row IS NULL OR qs.product.shelf.rowCount = :row)
		         AND (:col IS NULL OR qs.product.shelf.cols = :col)
		       """)
	List<QualityStandard> findByProduct_ShelfRowAndColNullable(@Param("row") Integer row,@Param("col") Integer col);
	@Query("""
		    SELECT qs 
		    FROM QualityStandard qs
		    WHERE (:rowMin IS NULL OR qs.product.shelf.rowCount >= :rowMin)
		      AND (:rowMax IS NULL OR qs.product.shelf.rowCount <= :rowMax)
		      AND (:colMin IS NULL OR qs.product.shelf.cols >= :colMin)
		      AND (:colMax IS NULL OR qs.product.shelf.cols <= :colMax)
		""")
	List<QualityStandard> findByProduct_ShelfRowAndColBetweenNullable(@Param("rowMin") Integer rowMin,@Param("rowMax") Integer rowMax, @Param("colMin") Integer colMin, @Param("colMax") Integer colMax);
}
