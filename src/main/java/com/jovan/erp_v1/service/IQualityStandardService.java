package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.QualityStandardRequest;
import com.jovan.erp_v1.response.QualityStandardResponse;

public interface IQualityStandardService {

	QualityStandardResponse create(QualityStandardRequest request);
	QualityStandardResponse update(Long id, QualityStandardRequest request);
	void delete(Long id);
	QualityStandardResponse findOne(Long id);
	List<QualityStandardResponse> findAll();
	
	List<QualityStandardResponse> findByUnit(Unit unit);
	List<QualityStandardResponse> findByDescriptionContainingIgnoreCase(String description);
	List<QualityStandardResponse> findByMinValue(BigDecimal minValue);
	List<QualityStandardResponse> findByMinValueGreaterThan(BigDecimal minValue);
	List<QualityStandardResponse> findByMinValueLessThan(BigDecimal minValue);
	List<QualityStandardResponse> findByMinValueBetween(BigDecimal min, BigDecimal max);
	List<QualityStandardResponse> findByMaxValue(BigDecimal maxValue);
	List<QualityStandardResponse> findByMaxValueGreaterThan(BigDecimal maxValue);
	List<QualityStandardResponse> findByMaxValueLessThan(BigDecimal maxValue);
	List<QualityStandardResponse> findByMaxValueBetween(BigDecimal min, BigDecimal max);
	long countByMinValueIsNotNull();
	long countByMaxValueIsNotNull();
	long countByMinValue(BigDecimal minValue);
	long countByMaxValue(BigDecimal maxValue);
	
	List<QualityStandardResponse> findByProduct_Id(Long productId);
	List<QualityStandardResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
	List<QualityStandardResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<QualityStandardResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
	List<QualityStandardResponse> findByProduct_CurrentQuantityBetween(BigDecimal min, BigDecimal max);
	List<QualityStandardResponse> findByProduct_NameContainingIgnoreCase(String productName);
	List<QualityStandardResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
	List<QualityStandardResponse> findByProduct_SupplierType(SupplierType supplierType);
	List<QualityStandardResponse> findByProduct_StorageType(StorageType storageType);
	List<QualityStandardResponse> findByProduct_GoodsType(GoodsType goodsType);
	List<QualityStandardResponse> findByProduct_StorageId( Long storageId);
	List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCase( String storageName);
	List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCase( String storageLocation);
	List<QualityStandardResponse> findByProduct_StorageCapacity( BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageCapacityGreaterThan( BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageCapacityLessThan( BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageCapacityBetween( BigDecimal min, BigDecimal max);
	List<QualityStandardResponse> findByProductStorageCapacityBetweenAndStatus( BigDecimal min,BigDecimal max,StorageStatus status);
	List<QualityStandardResponse> findByProductStorageCapacityStatusAndType(BigDecimal min,BigDecimal max, StorageStatus status,StorageType type);
	List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCaseAndCapacity( String storageName,  BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCaseAndCapacityGreaterThan( String storageName, BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCaseAndCapacityLessThan( String storageName,  BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageNameContainingIgnoreCaseAndCapacityBetween( String storageName,  BigDecimal min,  BigDecimal max);
	List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCaseAndCapacity( String storageLocation,  BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan( String storageLocation, BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityLessThan( String storageLocation,  BigDecimal capacity);
	List<QualityStandardResponse> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityBetween( String storageLocation,  BigDecimal min, BigDecimal max);
	QualityStandardResponse findByProduct_StorageHasShelvesForIsNull();
	List<QualityStandardResponse> findByProduct_SupplyId( Long supplyId);
	List<QualityStandardResponse> findByProduct_ShelfId( Long shelfId);
	List<QualityStandardResponse> findByProduct_ShelfRowCount( Integer rowCount);
	List<QualityStandardResponse> findByProduct_ShelfCols( Integer cols);
	List<QualityStandardResponse> findByProduct_ShelfRowAndColNullable( Integer row, Integer col);
	List<QualityStandardResponse> findByProduct_ShelfRowAndColBetweenNullable( Integer rowMin, Integer rowMax,  Integer colMin,  Integer colMax);

	List<QualityStandardResponse> searchQualityStandards(
            Long supplyId,
            BigDecimal productStorageMinCapacity,
            BigDecimal productStorageMaxCapacity,
            BigDecimal supplyMinQuantity,
            BigDecimal supplyMaxQuantity,
            Integer shelfRow,
            Integer shelfCol);
}
