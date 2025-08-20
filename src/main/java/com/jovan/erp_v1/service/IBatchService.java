package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.BatchRequest;
import com.jovan.erp_v1.response.BatchResponse;

public interface IBatchService {

	BatchResponse create(BatchRequest request);
	BatchResponse update(Long id, BatchRequest request);
	void delete(Long id);
	BatchResponse findOne(Long id);
	List<BatchResponse> findAll();
	List<BatchResponse> getExpiredBatches();
    List<BatchResponse> getActiveBatches();
    List<BatchResponse> getUpcomingBatches(Integer daysAhead);
    List<BatchResponse> getBatchesProducedBetween(LocalDate startDate, LocalDate endDate);
    List<BatchResponse> getBatchesExpiringBetween(LocalDate startDate, LocalDate endDate);
    
    boolean existsByCode(String code);
	List<BatchResponse> findByCodeContainingIgnoreCase(String code);
	List<BatchResponse> findByQuantityProduced(Integer quantityProduced);
	List<BatchResponse> findByQuantityProducedGreaterThan(Integer quantityProduced);
	List<BatchResponse> findByQuantityProducedLessThan(Integer quantityProduced);
	List<BatchResponse> findByProductionDate(LocalDate productionDate);
	List<BatchResponse> findByProductionDateBefore(LocalDate productionDate);
	List<BatchResponse> findByProductionDateAfter(LocalDate productionDate);
	List<BatchResponse> findByProductionDateBetween(LocalDate startDate, LocalDate endDate);
	List<BatchResponse> findByExpiryDate(LocalDate expiryDate);
	List<BatchResponse> findByExpiryDateBefore(LocalDate expiryDate);
	List<BatchResponse> findByExpiryDateAfter(LocalDate expiryDate);
	List<BatchResponse> findByExpiryDateBetween(LocalDate expiryDateStart, LocalDate expiryDateEnd);
	//za danas
	List<BatchResponse> findByProductionDateEquals(LocalDate today);
	//do danas
	List<BatchResponse> findByExpiryDateLessThanEqual(LocalDate today);
	//Od danas pa nadalje
	List<BatchResponse> findByProductionDateGreaterThanEqual(LocalDate today);
	List<BatchResponse> findByExpiryDateGreaterThanEqual(LocalDate expiryDate);
	List<BatchResponse> findByProductionDateLessThanEqual(LocalDate productionDate);
	List<BatchResponse> findByExpiryDateGreaterThan(LocalDate today);
	List<BatchResponse> findByExpiryDateIsNotNull();
	List<BatchResponse> findByProductionDateIsNull();
	List<BatchResponse> findByProductionDateIsNotNull();
	List<BatchResponse> findByExpiryDateIsNull();
	List<BatchResponse> findByExpiryDateAfterAndProductId(LocalDate date, Long productId);
	
	List<BatchResponse> findByProductId(Long productId);
	List<BatchResponse> findByProductCurrentQuantity(BigDecimal currentQuantity);
	List<BatchResponse> findByProductCurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<BatchResponse> findByProductCurrentQuantityLessThan(BigDecimal currentQuantity);
	List<BatchResponse> findByProductCurrentQuantityBetween(BigDecimal min, BigDecimal max);
	List<BatchResponse> findByProductIdAndExpiryDateLessThanEqual(Long productId, LocalDate today);
	List<BatchResponse> findByProductIdAndProductionDateAfter(Long productId, LocalDate date);
	List<BatchResponse> findByProductIdAndExpiryDateBetween(Long productId, LocalDate startDate, LocalDate endDate);
	
	List<BatchResponse> findByProduct_NameContainingIgnoreCase(String productName);
	List<BatchResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
	List<BatchResponse> findByProduct_SupplierType(SupplierType supplierType);
	List<BatchResponse> findByProduct_StorageType(StorageType storageType);
	List<BatchResponse> findByProduct_GoodsType(GoodsType goodsType);
	List<BatchResponse> findByProduct_StorageId(Long storageId);
	List<BatchResponse> findByProduct_StorageNameContainingIgnoreCase( String storageName);
	List<BatchResponse> findByProduct_StorageLocationContainingIgnoreCase( String storageLocation);
	List<BatchResponse> findByProduct_StorageCapacity( BigDecimal capacity);
	List<BatchResponse> findByProduct_StorageCapacityGreaterThan( BigDecimal capacity);
	List<BatchResponse> findByProduct_StorageCapacityLessThan( BigDecimal capacity);
	List<BatchResponse> findByProduct_StorageStatus( StorageStatus status);
	List<BatchResponse> findByProduct_StoragehasShelvesForIsNull();
	List<BatchResponse> findByProduct_SupplyId(Long supplyId);
	List<BatchResponse> findByProduct_ShelfId(Long shelfId);
	List<BatchResponse> findByProduct_ShelfRowCount( Integer rowCount);
	List<BatchResponse> findByProduct_ShelfCols( Integer cols);
	boolean existsByRowCountAndStorageId( Integer rows, Long storageId);
	boolean existsByColsAndStorageId( Integer cols, Long storageId);
	boolean existsByRowCountAndColsAndStorageId( Integer rows, Integer cols, Long storageId);
    BatchResponse findByRowCountAndColsAndStorageId( Integer rows, Integer cols, Long storageId);
    List<BatchResponse> findByRowCountAndStorageId( Integer rows, Long storageId);
    List<BatchResponse> findByColsAndStorageId( Integer cols, Long storageId);
}
