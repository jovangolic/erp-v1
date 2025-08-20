package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.TestMeasurementRequest;
import com.jovan.erp_v1.response.TestMeasurementResponse;

public interface ITestMeasurementService {

	List<TestMeasurementResponse> search(Long inspectionId,String productName,QualityCheckStatus status, BigDecimal minMeasuredValue,LocalDateTime startDate,LocalDateTime endDate);
	List<TestMeasurementResponse> deepSearch(String productName,LocalDateTime supplyUpdatedAfter,InspectionResult result);
	List<TestMeasurementResponse> searchMeasurements(
	        String storageName,String storageLocation, BigDecimal minCapacity,BigDecimal maxCapacity,
	        StorageStatus status,StorageType  type,String firstName,String lastName,String email,String phone);
	List<TestMeasurementResponse> searchTestMeasurements(
            String storageName,
            String storageLocation,
            BigDecimal storageCapacityMin,
            BigDecimal storageCapacityMax,
            StorageType storageType,
            StorageStatus storageStatus,
            BigDecimal supplyQuantityMin,
            BigDecimal supplyQuantityMax,
            LocalDateTime supplyUpdatedAfter,
            LocalDateTime supplyUpdatedBefore
    );
	TestMeasurementResponse create(TestMeasurementRequest request);
	TestMeasurementResponse update(Long id, TestMeasurementRequest request);
	void delete(Long id);
	TestMeasurementResponse findOne(Long id);
	List<TestMeasurementResponse> findAll();
	List<TestMeasurementResponse> findByMeasuredValue(BigDecimal measuredValue);
	List<TestMeasurementResponse> findByMeasuredValueGreaterThan(BigDecimal measuredValue);
	List<TestMeasurementResponse> findByMeasuredValueLessThan(BigDecimal measuredValue);
	boolean existsByWithinSpec(Boolean withinSpec);
	List<TestMeasurementResponse> findByInspectionId(Long inspectionId);
	List<TestMeasurementResponse> findByInspection_CodeContainingIgnoreCase(String code);
	List<TestMeasurementResponse> findByInspection_Type(InspectionType type);
	List<TestMeasurementResponse> findByInspection_Date(LocalDateTime date);
	List<TestMeasurementResponse> findByInspection_DateAfter(LocalDateTime date);
	List<TestMeasurementResponse> findByInspection_DateBefore(LocalDateTime date);
	List<TestMeasurementResponse> findByInspection_DateBetween(LocalDateTime start, LocalDateTime end);
	List<TestMeasurementResponse> findByInspection_BatchId( Long batchId);
	//product methods for inspection

	List<TestMeasurementResponse> findByInspection_ProductId( Long productId);
	List<TestMeasurementResponse> findByInspection_ProductCurrentQuantity( BigDecimal currentQuantity);
	List<TestMeasurementResponse> findByInspection_ProductCurrentQuantityGreaterThan( BigDecimal currentQuantity);
	List<TestMeasurementResponse> findByInspection_ProductCurrentQuantityLessThan( BigDecimal currentQuantity);
	List<TestMeasurementResponse> findByInspection_ProductNameContainingIgnoreCase( String productName);
	List<TestMeasurementResponse> findByInspection_ProductUnitMeasure( UnitMeasure unitMeasure);
	List<TestMeasurementResponse> findByInspection_ProductSupplierType( SupplierType supplierType);
	List<TestMeasurementResponse> findByInspection_ProductStorageType( StorageType storageType);
	List<TestMeasurementResponse> findByInspection_ProductGoodsType( GoodsType goodsType);
	List<TestMeasurementResponse> findByInspection_InspectorId(Long inspectorId);
	List<TestMeasurementResponse> findByInspection_Product_StorageId(Long storageId);
	List<TestMeasurementResponse> findByInspection_Product_SupplyId(Long supplyId);
	List<TestMeasurementResponse> findByInspection_Product_ShelfId(Long shelfId);
	
	List<TestMeasurementResponse> findByInspection_QuantityInspected(BigDecimal quantityInspected);
	List<TestMeasurementResponse> findByInspection_QuantityInspectedGreaterThan(BigDecimal quantityInspected);
	List<TestMeasurementResponse> findByInspection_QuantityInspectedLessThan(BigDecimal quantityInspected);
	
	List<TestMeasurementResponse> findByInspection_QuantityAccepted(BigDecimal quantityAccepted);
	List<TestMeasurementResponse> findByInspection_QuantityAcceptedGreaterThan(BigDecimal quantityAccepted);
	List<TestMeasurementResponse> findByInspection_QuantityAcceptedLessThan(BigDecimal quantityAccepted);
	
	List<TestMeasurementResponse> findByInspection_QuantityRejected(BigDecimal quantityRejected);
	List<TestMeasurementResponse> findByInspection_QuantityRejectedGreaterThan(BigDecimal quantityRejected);
	List<TestMeasurementResponse> findByInspection_QuantityRejectedLessThan(BigDecimal quantityRejected);
	
	List<TestMeasurementResponse> findByInspection_Notes(String notes);
	List<TestMeasurementResponse> findByInspection_Result(InspectionResult result);
	List<TestMeasurementResponse> findByInspection_ResultAndType( InspectionResult result, InspectionType type);
	List<TestMeasurementResponse> findByInspection_QualityCheckId( Long qualityCheckId);
	
	List<TestMeasurementResponse> findByInspection_QualityCheck_LocDate( LocalDateTime date);
	List<TestMeasurementResponse> findByInspection_QualityCheck_LocDateAfter(LocalDateTime date);
	List<TestMeasurementResponse> findByInspection_QualityCheck_LocDateBefore( LocalDateTime date);
	List<TestMeasurementResponse> findByInspection_QualityCheck_LocDateBetween( LocalDateTime start, LocalDateTime end);
	List<TestMeasurementResponse> findByInspection_QualityCheck_Notes(String notes);
	List<TestMeasurementResponse> findByInspection_QualityCheck_ReferenceType(ReferenceType referenceType);
	List<TestMeasurementResponse> findByInspection_QualityCheck_CheckType(QualityCheckType checkType);
	List<TestMeasurementResponse> findByInspection_QualityCheck_Status(QualityCheckStatus status);
	List<TestMeasurementResponse> findByInspection_QualityCheck_ReferenceId(Long referenceId);
	List<TestMeasurementResponse> findByInspection_QualityCheck_StatusAndCheckType( QualityCheckStatus status, QualityCheckType checkType);
	List<TestMeasurementResponse> findByInspection_QualityCheck_ReferenceType_Notes( ReferenceType referenceType, String notes);
	
	//QualityStandard methods
	List<TestMeasurementResponse> findByStandard_Id(Long qualityStandardId);
	List<TestMeasurementResponse> findByStandard_Description(String description);
	List<TestMeasurementResponse> findByStandard_MinValue(BigDecimal minValue);
	List<TestMeasurementResponse> findByStandard_MinValueGreaterThan(BigDecimal minValue);
	List<TestMeasurementResponse> findByStandard_MinValueLessThan(BigDecimal minValue);
	List<TestMeasurementResponse> findByStandard_MaxValue(BigDecimal maxValue);
	List<TestMeasurementResponse> findByStandard_MaxValueGreaterThan(BigDecimal maxValue);
	List<TestMeasurementResponse> findByStandard_MaxValueLessThan(BigDecimal maxValue);
	List<TestMeasurementResponse> findByStandard_Unit(Unit unit);
	
	List<TestMeasurementResponse> findByStandard_Product_Id( Long productId);
	List<TestMeasurementResponse> findByStandard_Product_CurrentQuantity(BigDecimal currentQuantity);
	List<TestMeasurementResponse> findByStandard_Product_CurrentQuantityGreaterThan( BigDecimal currentQuantity);
	List<TestMeasurementResponse> findByStandard_Product_CurrentQuantityLessThan( BigDecimal currentQuantity);
	List<TestMeasurementResponse> findByStandard_Product_NameContainingIgnoreCase( String productName);
	List<TestMeasurementResponse> findByStandard_Product_UnitMeasure( UnitMeasure unitMeasure);
	List<TestMeasurementResponse> findByStandard_Product_SupplierType( SupplierType supplierType);
	List<TestMeasurementResponse> findByStandard_Product_StorageType( StorageType storageType);
	List<TestMeasurementResponse> findByStandard_Product_GoodsType( GoodsType goodsType);
	List<TestMeasurementResponse> findByStandard_Product_StorageId( Long storageId);
	TestMeasurementResponse findByStandard_Product_StorageHasShelvesForIsNull();
	List<TestMeasurementResponse> findByStandard_Product_SupplyId( Long supplyId);
	List<TestMeasurementResponse> findByStandard_Product_ShelfId( Long shelfId);
}
