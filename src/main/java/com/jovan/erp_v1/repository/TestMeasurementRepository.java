package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.TestMeasurement;

@Repository
public interface TestMeasurementRepository extends JpaRepository<TestMeasurement, Long>, JpaSpecificationExecutor<TestMeasurement> {

	List<TestMeasurement> findByMeasuredValue(BigDecimal measuredValue);
	List<TestMeasurement> findByMeasuredValueGreaterThan(BigDecimal measuredValue);
	List<TestMeasurement> findByMeasuredValueLessThan(BigDecimal measuredValue);
	List<TestMeasurement> findByMeasuredValueBetween(BigDecimal min, BigDecimal max);
	boolean existsByWithinSpec(Boolean withinSpec);
	List<TestMeasurement> findByInspectionId(Long inspectionId);
	List<TestMeasurement> findByInspection_CodeContainingIgnoreCase(String code);
	List<TestMeasurement> findByInspection_Type(InspectionType type);
	List<TestMeasurement> findByInspection_InspectionDate(LocalDateTime date);
	List<TestMeasurement> findByInspection_InspectionDateAfter(LocalDateTime date);
	List<TestMeasurement> findByInspection_InspectionDateBefore(LocalDateTime date);
	List<TestMeasurement> findByInspection_InspectionDateBetween(LocalDateTime start, LocalDateTime end);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.batch.id = :batchId")
	List<TestMeasurement> findByInspection_BatchId(@Param("batchId") Long batchId);
	//product methods for inspection
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.product.id = :productId")
	List<TestMeasurement> findByInspection_ProductId(@Param("productId") Long productId);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.product.currentQuantity = :currentQuantity")
	List<TestMeasurement> findByInspection_ProductCurrentQuantity(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.product.currentQuantity >= :currentQuantity")
	List<TestMeasurement> findByInspection_ProductCurrentQuantityGreaterThan(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.product.currentQuantity <= :currentQuantity")
	List<TestMeasurement> findByInspection_ProductCurrentQuantityLessThan(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT tm FROM TestMeasurement tm WHERE LOWER(tm.inspection.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))")
	List<TestMeasurement> findByInspection_ProductNameContainingIgnoreCase(@Param("productName") String productName);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.product.unitMeasure = :unitMeasure")
	List<TestMeasurement> findByInspection_ProductUnitMeasure(@Param("unitMeasure") UnitMeasure unitMeasure);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.product.supplierType = :supplierType")
	List<TestMeasurement> findByInspection_ProductSupplierType(@Param("supplierType")SupplierType supplierType);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.product.storageType = :storageType")
	List<TestMeasurement> findByInspection_ProductStorageType(@Param("storageType") StorageType storageType);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.product.goodsType =  :goodsType")
	List<TestMeasurement> findByInspection_ProductGoodsType(@Param("goodsType") GoodsType goodsType);
	List<TestMeasurement> findByInspection_InspectorId(Long inspectorId);
	List<TestMeasurement> findByInspection_Product_StorageId(Long storageId);
	List<TestMeasurement> findByInspection_Product_SupplyId(Long supplyId);
	List<TestMeasurement> findByInspection_Product_ShelfId(Long shelfId);
	
	List<TestMeasurement> findByInspection_QuantityInspected(BigDecimal quantityInspected);
	List<TestMeasurement> findByInspection_QuantityInspectedGreaterThan(BigDecimal quantityInspected);
	List<TestMeasurement> findByInspection_QuantityInspectedLessThan(BigDecimal quantityInspected);
	List<TestMeasurement> findByInspection_QuantityInspectedBetween(BigDecimal min, BigDecimal max);
	
	List<TestMeasurement> findByInspection_QuantityAccepted(BigDecimal quantityAccepted);
	List<TestMeasurement> findByInspection_QuantityAcceptedGreaterThan(BigDecimal quantityAccepted);
	List<TestMeasurement> findByInspection_QuantityAcceptedLessThan(BigDecimal quantityAccepted);
	List<TestMeasurement> findByInspection_QuantityAcceptedBetween(BigDecimal min, BigDecimal max);
	
	List<TestMeasurement> findByInspection_QuantityRejected(BigDecimal quantityRejected);
	List<TestMeasurement> findByInspection_QuantityRejectedGreaterThan(BigDecimal quantityRejected);
	List<TestMeasurement> findByInspection_QuantityRejectedLessThan(BigDecimal quantityRejected);
	List<TestMeasurement> findByInspection_QuantityRejectedBetween(BigDecimal min, BigDecimal max);
	
	List<TestMeasurement> findByInspection_Notes(String notes);
	List<TestMeasurement> findByInspection_Result(InspectionResult result);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.result = :result AND tm.inspection.type = :type")
	List<TestMeasurement> findByInspection_ResultAndType(@Param("result") InspectionResult result,@Param("type") InspectionType type);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.qualityCheck.Id = :qualityCheckId")
	List<TestMeasurement> findByInspection_QualityCheckId(@Param("qualityCheckId") Long qualityCheckId);
	
	List<TestMeasurement> findByInspection_QualityCheck_LocDate( LocalDateTime date);
	List<TestMeasurement> findByInspection_QualityCheck_LocDateAfter(LocalDateTime date);
	List<TestMeasurement> findByInspection_QualityCheck_LocDateBefore( LocalDateTime date);
	List<TestMeasurement> findByInspection_QualityCheck_LocDateBetween( LocalDateTime start, LocalDateTime end);
	List<TestMeasurement> findByInspection_QualityCheck_Notes(String notes);
	List<TestMeasurement> findByInspection_QualityCheck_ReferenceType(ReferenceType referenceType);
	List<TestMeasurement> findByInspection_QualityCheck_CheckType(QualityCheckType checkType);
	List<TestMeasurement> findByInspection_QualityCheck_Status(QualityCheckStatus status);
	List<TestMeasurement> findByInspection_QualityCheck_ReferenceId(Long referenceId);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.qualityCheck.status = :status AND tm.inspection.qualityCheck.checkType = :checkType")
	List<TestMeasurement> findByInspection_QualityCheck_StatusAndCheckType(@Param("status") QualityCheckStatus status,@Param("checkType") QualityCheckType checkType);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.inspection.qualityCheck.referenceType = :referenceType AND tm.inspection.qualityCheck.notes = :notes")
	List<TestMeasurement> findByInspection_QualityCheck_ReferenceType_Notes(@Param("referenceType") ReferenceType referenceType,@Param("notes") String notes);
	
	//QualityStandard methods
	List<TestMeasurement> findByStandard_Id(Long qualityStandardId);
	List<TestMeasurement> findByStandard_Description(String description);
	List<TestMeasurement> findByStandard_MinValue(BigDecimal minValue);
	List<TestMeasurement> findByStandard_MinValueGreaterThan(BigDecimal minValue);
	List<TestMeasurement> findByStandard_MinValueLessThan(BigDecimal minValue);
	List<TestMeasurement> findByStandard_MaxValue(BigDecimal maxValue);
	List<TestMeasurement> findByStandard_MaxValueGreaterThan(BigDecimal maxValue);
	List<TestMeasurement> findByStandard_MaxValueLessThan(BigDecimal maxValue);
	List<TestMeasurement> findByStandard_Unit(Unit unit);
	
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.id = :productId")
	List<TestMeasurement> findByStandard_Product_Id(@Param("productId") Long productId);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.currentQuantity = :currentQuantity")
	List<TestMeasurement> findByStandard_Product_CurrentQuantity(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.currentQuantity >= :currentQuantity")
	List<TestMeasurement> findByStandard_Product_CurrentQuantityGreaterThan(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.currentQuantity <= :currentQuantity")
	List<TestMeasurement> findByStandard_Product_CurrentQuantityLessThan(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT tm FROM TestMeasurement tm WHERE LOWER(tm.standard.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))")
	List<TestMeasurement> findByStandard_Product_NameContainingIgnoreCase(@Param("productName") String productName);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.unitMeasure = :unitMeasure")
	List<TestMeasurement> findByStandard_Product_UnitMeasure(@Param("unitMeasure") UnitMeasure unitMeasure);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.supplierType = :supplierType")
	List<TestMeasurement> findByStandard_Product_SupplierType(@Param("supplierType") SupplierType supplierType);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.storageType = :storageType")
	List<TestMeasurement> findByStandard_Product_StorageType(@Param("storageType") StorageType storageType);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.goodsType = :goodsType")
	List<TestMeasurement> findByStandard_Product_GoodsType(@Param("goodsType") GoodsType goodsType);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.storage.id = :storageId")
	List<TestMeasurement> findByStandard_Product_StorageId(@Param("storageId") Long storageId);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.storage.hasShelvesFor IS NULL")
	Optional<TestMeasurement> findByStandard_Product_StorageHasShelvesForIsNull();
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.supply.id = :supplyId")
	List<TestMeasurement> findByStandard_Product_SupplyId(@Param("supplyId") Long supplyId);
	@Query("SELECT tm FROM TestMeasurement tm WHERE tm.standard.product.shelf.id = :shelfId")
	List<TestMeasurement> findByStandard_Product_ShelfId(@Param("shelfId") Long shelfId);
}

