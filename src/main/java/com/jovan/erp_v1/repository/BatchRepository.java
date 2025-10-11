package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Batch;
import com.jovan.erp_v1.statistics.batch.BatchConfirmedStatDTO;
import com.jovan.erp_v1.statistics.batch.BatchMonthlyStatDTO;
import com.jovan.erp_v1.statistics.batch.BatchStatusStatDTO;


@Repository
public interface BatchRepository extends JpaRepository<Batch, Long>, JpaSpecificationExecutor<Batch> {

	boolean existsByCode(String code);
	List<Batch> findByCodeContainingIgnoreCase(String code);
	List<Batch> findByQuantityProduced(Integer quantityProduced);
	List<Batch> findByQuantityProducedGreaterThan(Integer quantityProduced);
	List<Batch> findByQuantityProducedLessThan(Integer quantityProduced);
	List<Batch> findByProductionDate(LocalDate productionDate);
	List<Batch> findByProductionDateBefore(LocalDate productionDate);
	List<Batch> findByProductionDateAfter(LocalDate productionDate);
	List<Batch> findByProductionDateBetween(LocalDate startDate, LocalDate endDate);
	List<Batch> findByExpiryDate(LocalDate expiryDate);
	List<Batch> findByExpiryDateBefore(LocalDate expiryDate);
	List<Batch> findByExpiryDateAfter(LocalDate expiryDate);
	List<Batch> findByExpiryDateBetween(LocalDate expiryDateStart, LocalDate expiryDateEnd);
	//za danas
	List<Batch> findByProductionDateEquals(LocalDate today);
	//do danas
	List<Batch> findByExpiryDateLessThanEqual(LocalDate today);
	//Od danas pa nadalje
	List<Batch> findByProductionDateGreaterThanEqual(LocalDate today);
	List<Batch> findByExpiryDateGreaterThanEqual(LocalDate expiryDate);
	List<Batch> findByProductionDateLessThanEqual(LocalDate productionDate);
	List<Batch> findByExpiryDateGreaterThan(LocalDate today);
	List<Batch> findByExpiryDateIsNotNull();
	List<Batch> findByProductionDateIsNull();
	List<Batch> findByProductionDateIsNotNull();
	List<Batch> findByExpiryDateIsNull();
	List<Batch> findByExpiryDateAfterAndProductId(LocalDate date, Long productId);
	
	List<Batch> findByProductId(Long productId);
	List<Batch> findByProductCurrentQuantity(BigDecimal currentQuantity);
	List<Batch> findByProductCurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<Batch> findByProductCurrentQuantityLessThan(BigDecimal currentQuantity);
	List<Batch> findByProductCurrentQuantityBetween(BigDecimal min, BigDecimal max);
	List<Batch> findByProductIdAndExpiryDateLessThanEqual(Long productId, LocalDate today);
	List<Batch> findByProductIdAndProductionDateAfter(Long productId, LocalDate date);
	List<Batch> findByProductIdAndExpiryDateBetween(Long productId, LocalDate startDate, LocalDate endDate);
	
	List<Batch> findByProduct_NameContainingIgnoreCase(String productName);
	List<Batch> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
	List<Batch> findByProduct_SupplierType(SupplierType supplierType);
	List<Batch> findByProduct_StorageType(StorageType storageType);
	List<Batch> findByProduct_GoodsType(GoodsType goodsType);
	@Query("SELECT b FROM Batch b WHERE b.product.storage.id = :storageId")
	List<Batch> findByProduct_StorageId(Long storageId);
	@Query("SELECT b FROM Batch b WHERE LOWER(b.product.storage.name) LIKE LOWER(CONCAT('%', :storageName ,'%'))")
	List<Batch> findByProduct_StorageNameContainingIgnoreCase(@Param("storageName") String storageName);
	@Query("SELECT b FROM Batch b WHERE LOWER(b.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation ,'%'))")
	List<Batch> findByProduct_StorageLocationContainingIgnoreCase(@Param("storageLocation") String storageLocation);
	@Query("SELECT b FROM Batch b WHERE b.product.storage.capacity = :capacity")
	List<Batch> findByProduct_StorageCapacity(@Param("capacity") BigDecimal capacity);
	@Query("SELECT b FROM Batch b WHERE b.product.storage.capacity >= :capacity")
	List<Batch> findByProduct_StorageCapacityGreaterThan(@Param("capacity") BigDecimal capacity);
	@Query("SELECT b FROM Batch b WHERE b.product.storage.capacity <= :capacity")
	List<Batch> findByProduct_StorageCapacityLessThan(@Param("capacity") BigDecimal capacity);
	@Query("SELECT b FROM Batch b WHERE b.product.storage.status = :status")
	List<Batch> findByProduct_StorageStatus(@Param("status") StorageStatus status);
	@Query("SELECT b FROM Batch b WHERE b.product.storage.hasShelvesFor  IS NULL")
	List<Batch> findByProduct_StoragehasShelvesForIsNull();
	@Query("SELECT b FROM Batch b WHERE b.product.supply.id = :supplyId")
	List<Batch> findByProduct_SupplyId(Long supplyId);
	@Query("SELECT b FROM Batch b WHERE b.product.shelf.id = :shelfId")
	List<Batch> findByProduct_ShelfId(Long shelfId);
	@Query("SELECT b FROM Batch b WHERE b.product.shelf.rowCount = :rowCount")
	List<Batch> findByProduct_ShelfRowCount(@Param("rowCount") Integer rowCount);
	@Query("SELECT b FROM Batch b WHERE b.product.shelf.cols = :cols")
	List<Batch> findByProduct_ShelfCols(@Param("cols") Integer cols);
	@Query("SELECT COUNT(b) > 0 FROM Batch b WHERE b.product.shelf.rowCount = :rows AND b.product.storage.id = :storageId")
	boolean existsByRowCountAndStorageId(@Param("rows") Integer rows,@Param("storageId") Long storageId);
	@Query("SELECT COUNT(b) > 0 FROM Batch b WHERE  b.product.shelf.cols = :cols AND b.product.storage.id = :storageId")
	boolean existsByColsAndStorageId(@Param("cols") Integer cols,@Param("storageId") Long storageId);
	@Query("SELECT COUNT(b) > 0 FROM Batch b WHERE b.product.shelf.rowCount = :rows AND b.product.shelf.cols = :cols AND b.product.storage.id = :storageId")
	boolean existsByRowCountAndColsAndStorageId(@Param("row") Integer rows,@Param("cols") Integer cols,@Param("storageId") Long storageId);
	@Query("SELECT b FROM Batch b WHERE b.product.shelf.rowCount = :rows AND b.product.shelf.cols = :cols AND b.product.storage.id = :storageId")
    Optional<Batch> findByRowCountAndColsAndStorageId(@Param("rows") Integer rows,@Param("cols") Integer cols,@Param("storageId") Long storageId);
	@Query("SELECT b FROM Batch b WHERE b.product.shelf.rowCount = :rows AND b.product.storage.id = :storageId")
    List<Batch> findByRowCountAndStorageId(@Param("rows") Integer rows,@Param("storageId") Long storageId);
	@Query("SELECT b FROM Batch b WHERE  b.product.shelf.cols = :cols AND b.product.storage.id = :storageId")
    List<Batch> findByColsAndStorageId(@Param("cols") Integer cols,@Param("storageId") Long storageId);
	
	//nove metode
	@Query("SELECT b FROM Batch b LEFT JOIN FETCH b.inspections WHERE b.id = :id")
	List<Batch> trackBatch(@Param("id") Long id);
	
	@Query("SELECT new com.jovan.erp_v1.statistics.batch.BatchStatusStatDTO(b.status, COUNT(b)) " +
		       "FROM Batch b GROUP BY b.status")
	List<BatchStatusStatDTO> countBatchesByStatus();
	@Query("SELECT new com.jovan.erp_v1.statistics.batch.BatchConfirmedStatDTO(b.confirmed, COUNT(b)) "+
			"FROM Batch b GROUP BY b.confirmed")
	List<BatchConfirmedStatDTO> countBatchesByConfirmed();
	
	@Query("SELECT new com.jovan.erp_v1.statistics.batch.BatchMonthlyStatDTO(" +
		       "CAST(FUNCTION('YEAR', b.productionDate) AS integer), "+
				"CAST(FUNCTION('MONTH', b.productionDate) AS integer), "+
		       	"COUNT(b)) "+
				"FROM Batch b "+
		       	"GROUP BY FUNCTION('YEAR', b.productionDate), FUNCTION('MONTH', b.productionDate) "+
				"ORDER BY FUNCTION('YEAR', b.productionDate), FUNCTION('MONTH', b.productionDate)")
	List<BatchMonthlyStatDTO> countBatchesByYearAndMonth();
}
