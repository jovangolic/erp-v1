package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.dto.InspectionQuantityAcceptedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedSummaryDTO;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.statistics.inspection.InspectionResultStatDTO;
import com.jovan.erp_v1.statistics.inspection.InspectionTypeStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByQualityCheckStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByQualityCheckStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByQualityCheckStatDTO;



@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long>, JpaSpecificationExecutor<Inspection> {

	boolean existsByCode(String code);
	List<Inspection> findByCode(String code);
	List<Inspection> findByType(InspectionType type);
	List<Inspection> findByResult(InspectionResult result);
	List<Inspection> findByNotes(String notes);
	List<Inspection> findByTypeAndResult(InspectionType type, InspectionResult result);
	List<Inspection> findByNotesAndType(String notes, InspectionType type);
	List<Inspection> findByNotesAndResult(String notes, InspectionResult result);
	List<Inspection> findByInspectionDate(LocalDateTime inspectionDate);
	List<Inspection> findByInspectionDateBefore(LocalDateTime inspectionDate);
	List<Inspection> findByInspectionDateAfter(LocalDateTime inspectionDate);
	List<Inspection> findByInspectionDateBetween(LocalDateTime start, LocalDateTime end);
	List<Inspection> findByInspectionDateAndResult(LocalDateTime inspectionDate, InspectionResult result);
	
	//batch
	List<Inspection> findByBatchId(Long batchId);
	List<Inspection> findByBatchCode(String batchCode);
	boolean existsByBatchCode(String batchCode);
	List<Inspection> findByBatch_ExpiryDate(LocalDate expiryDate);
	List<Inspection> findByBatch_ExpiryDateAfter(LocalDate expiryDate);
	List<Inspection> findByBatch_ExpiryDateBefore(LocalDate expiryDate);
	List<Inspection> findByBatch_ExpiryDateBetween(LocalDate start, LocalDate end);
	List<Inspection> findByBatch_ProductionDate(LocalDate productionDate);
	List<Inspection> findByBatch_ProductionDateAfter(LocalDate productionDate);
	List<Inspection> findByBatch_ProductionDateBefore(LocalDate productionDate);
	List<Inspection> findByBatch_ProductionDateBetween(LocalDate productionDateStart, LocalDate productionDateEnd);
	List<Inspection> findByBatch_ProductionDateEquals(LocalDate today);
	List<Inspection> findByBatch_ExpiryDateLessThanEqual(LocalDate today);
	List<Inspection> findByBatch_ProductionDateGreaterThanEqual(LocalDate today);
	List<Inspection> findByBatch_ExpiryDateGreaterThanEqual(LocalDate expiryDate);
	List<Inspection> findByBatch_ProductionDateLessThanEqual(LocalDate productionDate);
	List<Inspection> findByBatch_ExpiryDateGreaterThan(LocalDate today);
	List<Inspection> findByBatch_ExpiryDateIsNotNull();
	List<Inspection> findByBatch_ProductionDateIsNull();
	List<Inspection> findByBatch_ProductionDateIsNotNull();
	List<Inspection> findByBatch_ExpiryDateIsNull();
	
	List<Inspection> findByBatch_QuantityProduced(Integer quantityProduced);
	List<Inspection> findByBatch_QuantityProducedGreaterThan(Integer quantityProduced);
	List<Inspection> findByBatch_QuantityProducedLessThan(Integer quantityProduced);
	List<Inspection> findByBatch_QuantityProducedBetween(Integer min, Integer max);
	//batch -> Product
	@Query("SELECT i FROM Inspection i  WHERE i.batch.product.id = :productId")
	List<Inspection> findByBatch_ProductId(@Param("productId") Long productId);
	@Query("SELECT i FROM Inspection i  WHERE i.batch.product.currentQuantity = :currentQuantity")
	List<Inspection> findByBatch_ProductCurrentQuantity(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT i FROM Inspection i  WHERE i.batch.product.currentQuantity >= :currentQuantity")
	List<Inspection> findByBatch_ProductCurrentQuantityGreaterThan(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT i FROM Inspection i  WHERE i.batch.product.currentQuantity <= :currentQuantity")
	List<Inspection> findByBatch_ProductCurrentQuantityLessThan(@Param("currentQuantity") BigDecimal currentQuantity);
	@Query("SELECT i FROM Inspection i  WHERE i.batch.product.currentQuantity  BETWEEN :min AND :max")
	List<Inspection> findByBatch_ProductCurrentQuantityBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
	@Query("SELECT i FROM Inspection i  WHERE LOWER(i.batch.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))")
	List<Inspection> findByBatch_ProductNameContainingIgnoreCase(@Param("productName") String productName);
	@Query("SELECT i FROM Inspection i WHERE i.batch.product.unitMeasure = :unitMeasure")
	List<Inspection> findByBatch_ProductUnitMeasure(@Param("unitMeasure") UnitMeasure unitMeasure); 
	@Query("SELECT i FROM Inspection i WHERE i.batch.product.supplierType = :supplierType")
	List<Inspection> findByBatch_ProductSupplierType(@Param("supplierType") SupplierType supplierType);
	@Query("SELECT i FROM Inspection i WHERE i.batch.product.storageType = :storageType")
	List<Inspection> findByBatch_ProductStorageType(@Param("storageType") StorageType storageType);
	@Query("SELECT i FROM Inspection i WHERE i.batch.product.goodsType = :goodsType")
	List<Inspection> findByBatch_ProductGoodsType(@Param("goodsType") GoodsType goodsType);
	@Query("SELECT i FROM Inspection i WHERE i.batch.product.storage.id = :storageId")
	List<Inspection> findByBatch_Product_StorageId(@Param("storageId") Long storageId);
	@Query("SELECT i FROM Inspection i WHERE i.batch.product.shelf.id = :shelfId")
	List<Inspection> findByBatch_Product_ShelfId(@Param("shelfId") Long shelfId);
	@Query("SELECT i FROM Inspection i WHERE i.batch.product.supply.id = :supplyId")
	List<Inspection> findByBatch_Product_SupplyId(@Param("supplyId") Long supplyId);
	//Inspector
	List<Inspection> findByInspectorId(Long inspectorId);
	List<Inspection> findByInspectorFirstNameContainingIgnoreCaseAndInspectorLastNameContainingIgnoreCase(String firstName, String lastName);
	List<Inspection> findByInspectorEmailLikeIgnoreCase(String inspectorEmail);
	List<Inspection> findByInspectorPhoneNumberLikeIgnoreCase(String inspectorPhoneNumber);
	//product
	List<Inspection> findByProductId(Long productId);
	List<Inspection> findByProductCurrentQuantity(BigDecimal currentQuantity);
	List<Inspection> findByProductCurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<Inspection> findByProductCurrentQuantityLessThan(BigDecimal currentQuantity);
	List<Inspection> findByProductCurrentQuantityBetween(BigDecimal min, BigDecimal max);
	List<Inspection> findByProductNameContainingIgnoreCase(String productName);
	List<Inspection> findByProductUnitMeasure(UnitMeasure unitMeasure);
	List<Inspection> findByProductSupplierType(SupplierType supplierType);
	List<Inspection> findByProductStorageType(StorageType storageType);
	List<Inspection> findByProductGoodsType(GoodsType goodsType);
	@Query("SELECT i FROM Inspection i WHERE i.product.supply.id = :supplyId")
	List<Inspection> findByProduct_SupplyId(@Param("supplyId") Long supplyId);
	@Query("SELECT i FROM Inspection i WHERE i.product.shelf.id = :shelfId")
	List<Inspection> findByProduct_ShelfId(@Param("shelfId") Long shelfId);
	@Query("SELECT i FROM Inspection i WHERE i.product.storage.hasShelvesFor IS NULL")
	Optional<Inspection>findByProduct_StorageHasShelvesForIsNull();
	@Query("SELECT i FROM Inspection i WHERE i.product.shelf.rowCount = :rowCount")
	List<Inspection> findByProduct_ShelfRowCount(@Param("rowCount") Integer rowCount);
	@Query("SELECT i FROM Inspection i WHERE i.product.shelf.cols = :cols")
	List<Inspection> findByProduct_ShelfCols(@Param("cols") Integer cols);
	@Query("""
		       SELECT i 
		       FROM Inspection i
		       WHERE (:row IS NULL OR i.product.shelf.rowCount = :row)
		         AND (:col IS NULL OR i.product.shelf.cols = :col)
		       """)
	List<Inspection> findByProduct_ShelfRowAndColNullable(@Param("row") Integer row,@Param("col") Integer col);
	@Query("""
		    SELECT i 
		    FROM Inspection i
		    WHERE (:rowMin IS NULL OR i.product.shelf.rowCount >= :rowMin)
		      AND (:rowMax IS NULL OR i.product.shelf.rowCount <= :rowMax)
		      AND (:colMin IS NULL OR i.product.shelf.cols >= :colMin)
		      AND (:colMax IS NULL OR i.product.shelf.cols <= :colMax)
		""")
	List<Inspection> findByProduct_ShelfRowAndColBetweenNullable(@Param("rowMin") Integer rowMin,@Param("rowMax") Integer rowMax, @Param("colMin") Integer colMin, @Param("colMax") Integer colMax);
	
	List<Inspection> findByQuantityInspected(Integer quantityInspected);
	List<Inspection> findByQuantityInspectedGreaterThan(Integer quantityInspected);
	List<Inspection> findByQuantityInspectedLessThan(Integer quantityInspected);
	List<Inspection> findByQuantityInspectedBetween(Integer min, Integer max);
	
	List<Inspection> findByQuantityAccepted(Integer quantityAccepted);
	List<Inspection> findByQuantityAcceptedGreaterThan(Integer quantityAccepted);
	List<Inspection> findByQuantityAcceptedLessThan(Integer quantityAccepted);
	List<Inspection> findByQuantityAcceptedBetween(Integer min, Integer max);
	
	List<Inspection> findByQuantityRejected(Integer quantityRejected);
	List<Inspection> findByQuantityRejectedGreaterThan(Integer quantityRejected);
	List<Inspection> findByQuantityRejectedLessThan(Integer quantityRejected);
	List<Inspection> findByQuantityRejectedBetween(Integer min, Integer max);
	
	@Query("SELECT new com.jovan.erp_v1.dto.InspectionQuantityInspectedSummaryDTO(COUNT(i), SUM(i.quantityInspected)) " +
	           "FROM Inspection i")
	InspectionQuantityInspectedSummaryDTO getQuantityInspectedSummary();

	@Query("SELECT new com.jovan.erp_v1.dto.InspectionQuantityAcceptedSummaryDTO(COUNT(i), SUM(i.quantityAccepted)) " +
	           "FROM Inspection i")
	InspectionQuantityAcceptedSummaryDTO getQuantityAcceptedSummary();

	@Query("SELECT new com.jovan.erp_v1.dto.InspectionQuantityRejectedSummaryDTO(COUNT(i), SUM(i.quantityRejected)) " +
	           "FROM Inspection i")
	InspectionQuantityRejectedSummaryDTO getQuantityRejectedSummary();
	//quality-check
	List<Inspection> findByQualityCheckId(Long qualityCheckId);
	List<Inspection> findByQualityCheckLocDate(LocalDateTime locDate);
	List<Inspection> findByQualityCheckLocDateAfter(LocalDateTime locDate);
	List<Inspection> findByQualityCheckLocDateBefore(LocalDateTime locDate);
	List<Inspection> findByQualityCheckLocDateBetween(LocalDateTime start, LocalDateTime end);
	List<Inspection> findByQualityCheckNotes(String notes);
	List<Inspection> findByQualityCheckReferenceId(Long referenceId);
	List<Inspection> findByQualityCheckReferenceType(ReferenceType referenceType);
	List<Inspection> findByQualityCheck_CheckType(QualityCheckType checkType);
	List<Inspection> findByQualityCheck_Status(QualityCheckStatus status);
	List<Inspection> findByQualityCheck_ReferenceTypeAndQualityCheck_CheckType(ReferenceType referenceType, QualityCheckType checkType);
	List<Inspection> findByQualityCheck_ReferenceTypeAndQualityCheck_Status(ReferenceType referenceType, QualityCheckStatus status);
	List<Inspection> findByQualityCheck_CheckTypeAndQualityCheck_Status(QualityCheckType checkType, QualityCheckStatus status);
	List<Inspection> findByQualityCheckInspectorId(Long inspectorId);
	List<Inspection> findByQualityCheckInspectorEmailLikeIgnoreCase(String inspectorEmail);
	List<Inspection> findByQualityCheckInspectorPhoneNumberLikeIgnoreCase(String inspectorPhoneNumber);
	@Query("SELECT i FROM Inspection i WHERE LOWER(i.qualityCheck.inspector.firstName) LIKE LOWER(CONCAT('%', :firstName,'%')) AND LOWER(i.qualityCheck.inspector.firstName) LIKE LOWER(CONCAT('%', :lastName,'%'))")
	List<Inspection> findByQualityCheckInspectorFirstNameContainingIgnoreCaseAndQualityCheckInspectorLastNameContainingIgnoreCase(@Param("firstName") String firstName,@Param("lastName") String lastName);
	
	//nove metode
	@Query("SELECT i FROM Inspection i LEFT JOIN FETCH i.defects WHERE i.id = :id")
	List<Inspection> trackInspectionByInspectionDefect(@Param("id") Long id);
	@Query("SELECT i FROM Inspection i LEFT JOIN FETCH i.measurements WHERE i.id = :id")
	List<Inspection> trackInspectionByTestMeasurement(@Param("id") Long id);
	
	@Query("SELECT i FROM Inspection i WHERE (:id IS NULL OR i.id = :id) "
			+ "AND (:notes IS NULL OR LOWER(i.notes) LIKE LOWER(CONCAT('%', :notes, '%')))")
	List<Inspection> findByReports(@Param("id") Long id, @Param("notes") String notes);
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityInspectedByBatchStatDTO(
			COUNT(i),
			SUM(i.quantityInspected),
			i.batch.id,
			i.batch.code
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.batch.id, i.batch.code
			""")
	List<QuantityInspectedByBatchStatDTO> countQuantityInspectedByBatch();
	
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityRejectedByBatchStatDTO(
			COUNT(i),
			SUM(i.quantityRejected),
			i.batch.id,
			i.batch.code
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.batch.id, i.batch.code
			""")
	List<QuantityRejectedByBatchStatDTO> countQuantityRejectedByBatch();
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByBatchStatDTO(
			COUNT(i),
			SUM(i.quantityAccepted),
			i.batch.id,
			i.batch.code
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.batch.id, i.batch.code
			""")
	List<QuantityAcceptedByBatchStatDTO> countQuantityAcceptedByBatch();
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityInspectedByProductStatDTO(
			COUNT(i),
			SUM(i.quantityInspected),
			i.product.id,
			i.product.name
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.product.id, i.product.name
			""")
	List<QuantityInspectedByProductStatDTO> countQuantityInspectedByProduct();
	
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByProductStatDTO(
			COUNT(i),
			SUM(i.quantityAccepted),
			i.product.id,
			i.product.name
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.product.id, i.product.name
			""")
	List<QuantityAcceptedByProductStatDTO> countQuantityAcceptedByProduct();
	
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityRejectedByProductStatDTO(
			COUNT(i),
			SUM(i.quantityRejected),
			i.product.id,
			i.product.name
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.product.id, i.product.name
			""")
	List<QuantityRejectedByProductStatDTO> countQuantityRejectedByProduct();
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityInspectedByInspectorStatDTO(
			COUNT(i),
			SUM(i.quantityInspected),
			i.inspector.id,
			i.inspector.firstName, 
			i.inspector.lastName
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.inspector.id, i.inspector.firstName, i.inspector.lastName
			""")
	List<QuantityInspectedByInspectorStatDTO> countQuantityInspectedByInspector();
	
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByInspectorStatDTO(
			COUNT(i),
			SUM(i.quantityAccepted),
			i.inspector.id,
			i.inspector.firstName, 
			i.inspector.lastName
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.inspector.id, i.inspector.firstName, i.inspector.lastName
			""")
	List<QuantityAcceptedByInspectorStatDTO> countQuantityAcceptedByInspector();
	
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityRejectedByInspectorStatDTO(
			COUNT(i),
			SUM(i.quantityRejected),
			i.inspector.id,
			i.inspector.firstName, 
			i.inspector.lastName
			)
			FROM i Inspection i
			WHERE i.confirmed = true
    	    GROUP BY i.inspector.id, i.inspector.firstName, i.inspector.lastName
			""")
	List<QuantityRejectedByInspectorStatDTO> countQuantityRejectedByInspector();
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityInspectedByQualityCheckStatDTO(
			COUNT(i),
			SUM(i.quantityInspected),
			i.qualityCheck.id
			)
			FROM i Inspection i
			WHERE i.confirmed = true
			GROUP BY i.qualityCheck.id
			""")
	List<QuantityInspectedByQualityCheckStatDTO> countQuantityInspectedByQualityCheck();
	
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByQualityCheckStatDTO(
			COUNT(i),
			SUM(i.quantityAccepted),
			i.qualityCheck.id
			)
			FROM i Inspection i
			WHERE i.confirmed = true
			GROUP BY i.qualityCheck.id
			""")
	List<QuantityAcceptedByQualityCheckStatDTO> countQuantityAcceptedByQualityCheck();
	
	@Query("""
			SELECT new com.jovan.erp_v1.statistics.inspection.QuantityRejectedByQualityCheckStatDTO(
			COUNT(i),
			SUM(i.quantityRejected),
			i.qualityCheck.id
			)
			FROM i Inspection i
			WHERE i.confirmed = true
			GROUP BY i.qualityCheck.id
			""")
	List<QuantityRejectedByQualityCheckStatDTO> countQuantityRejectedByQualityCheck();
	
	@Query("SELECT new com.jovan.erp_v1.statistics.inspection.InspectionTypeStatDTO(i.type, COUNT(i))"
			+ "FROM Inspection i GROUP BY i.type ")
	List<InspectionTypeStatDTO> countInspectionByType();
	@Query("SELECT new com.jovan.erp_v1.statistics.inspection.InspectionResultStatDTO(i.result, COUNT(i))"
			+ "FROM Inspection i GROUP BY i.result ")
	List<InspectionResultStatDTO> countInspectionByResult();
}
