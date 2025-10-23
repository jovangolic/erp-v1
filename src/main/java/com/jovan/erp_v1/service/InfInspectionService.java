package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.dto.InspectionQuantityAcceptedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityAcceptedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedSummaryDTO;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionStatus;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.InspectionRequest;
import com.jovan.erp_v1.response.InspectionResponse;
import com.jovan.erp_v1.save_as.InspectionSaveAsRequest;
import com.jovan.erp_v1.search_request.InspectionSearchRequest;
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

public interface InfInspectionService {

	InspectionResponse create(InspectionRequest request);
	InspectionResponse update(Long id, InspectionRequest request);
	void delete(Long id);
	InspectionResponse findOne(Long id);
	List<InspectionResponse> findAll();
	
	List<InspectionResponse> searchInspections(String storageName, String storageLocation,BigDecimal minCapacity, BigDecimal maxCapacity);
	InspectionQuantityInspectedDTO getQuantityInspected(Long inspectionId);
	InspectionQuantityAcceptedDTO getQuantityAccepted(Long inspectionId);
	InspectionQuantityRejectedDTO getQuantityRejected(Long inspectionId);
	InspectionQuantityInspectedSummaryDTO getQuantityInspectedSummary();
	InspectionQuantityAcceptedSummaryDTO getQuantityAcceptedSummary();
	InspectionQuantityRejectedSummaryDTO getQuantityRejectedSummary();
	
	boolean existsByCode(String code);
	List<InspectionResponse> findByCode(String code);
	List<InspectionResponse> findByType(InspectionType type);
	List<InspectionResponse> findByResult(InspectionResult result);
	List<InspectionResponse> findByNotes(String notes);
	List<InspectionResponse> findByTypeAndResult(InspectionType type, InspectionResult result);
	List<InspectionResponse> findByNotesAndType(String notes, InspectionType type);
	List<InspectionResponse> findByNotesAndResult(String notes, InspectionResult result);
	List<InspectionResponse> findByInspectionDate(LocalDateTime inspectionDate);
	List<InspectionResponse> findByInspectionDateBefore(LocalDateTime inspectionDate);
	List<InspectionResponse> findByInspectionDateAfter(LocalDateTime inspectionDate);
	List<InspectionResponse> findByInspectionDateBetween(LocalDateTime start, LocalDateTime end);
	List<InspectionResponse> findByInspectionDateAndResult(LocalDateTime inspectionDate, InspectionResult result);
	
	//batch
	List<InspectionResponse> findByBatchId(Long batchId);
	List<InspectionResponse> findByBatchCode(String batchCode);
	boolean existsByBatchCode(String batchCode);
	List<InspectionResponse> findByBatch_ExpiryDate(LocalDate expiryDate);
	List<InspectionResponse> findByBatch_ExpiryDateAfter(LocalDate expiryDate);
	List<InspectionResponse> findByBatch_ExpiryDateBefore(LocalDate expiryDate);
	List<InspectionResponse> findByBatch_ExpiryDateBetween(LocalDate start, LocalDate end);
	List<InspectionResponse> findByBatch_ProductionDate(LocalDate productionDate);
	List<InspectionResponse> findByBatch_ProductionDateAfter(LocalDate productionDate);
	List<InspectionResponse> findByBatch_ProductionDateBefore(LocalDate productionDate);
	List<InspectionResponse> findByBatch_ProductionDateBetween(LocalDate productionDateStart, LocalDate productionDateEnd);
	List<InspectionResponse> findByBatch_ProductionDateEqualsDateNow();
	List<InspectionResponse> findByBatch_ExpiryDateLessThanEqualDateNow();
	List<InspectionResponse> findByBatch_ProductionDateGreaterThanEqualDateNow();
	List<InspectionResponse> findByBatch_ExpiryDateGreaterThanEqual(LocalDate expiryDate);
	List<InspectionResponse> findByBatch_ProductionDateLessThanEqual(LocalDate productionDate);
	List<InspectionResponse> findByBatchExpiryDateAfterToday();
	List<InspectionResponse> findByBatch_ExpiryDateIsNotNull();
	List<InspectionResponse> findByBatch_ProductionDateIsNull();
	List<InspectionResponse> findByBatch_ProductionDateIsNotNull();
	List<InspectionResponse> findByBatch_ExpiryDateIsNull();
	
	List<InspectionResponse> findByBatch_QuantityProduced(Integer quantityProduced);
	List<InspectionResponse> findByBatch_QuantityProducedGreaterThan(Integer quantityProduced);
	List<InspectionResponse> findByBatch_QuantityProducedLessThan(Integer quantityProduced);
	List<InspectionResponse> findByBatch_QuantityProducedBetween(Integer min, Integer max);
	//batch -> Product
	List<InspectionResponse> findByBatch_ProductId( Long productId);
	List<InspectionResponse> findByBatch_ProductCurrentQuantity( BigDecimal currentQuantity);
	List<InspectionResponse> findByBatch_ProductCurrentQuantityGreaterThan( BigDecimal currentQuantity);
	List<InspectionResponse> findByBatch_ProductCurrentQuantityLessThan( BigDecimal currentQuantity);
	List<InspectionResponse> findByBatch_ProductCurrentQuantityBetween( BigDecimal min,  BigDecimal max);
	List<InspectionResponse> findByBatch_ProductNameContainingIgnoreCase( String productName);
	List<InspectionResponse> findByBatch_ProductUnitMeasure( UnitMeasure unitMeasure); 
	List<InspectionResponse> findByBatch_ProductSupplierType( SupplierType supplierType);
	List<InspectionResponse> findByBatch_ProductStorageType( StorageType storageType);
	List<InspectionResponse> findByBatch_ProductGoodsType( GoodsType goodsType);
	List<InspectionResponse> findByBatch_Product_StorageId( Long storageId);
	List<InspectionResponse> findByBatch_Product_ShelfId( Long shelfId);
	List<InspectionResponse> findByBatch_Product_SupplyId( Long supplyId);
	//Inspector
	List<InspectionResponse> findByInspectorId(Long inspectorId);
	List<InspectionResponse> findByInspectorFirstNameContainingIgnoreCaseAndInspectorLastNameContainingIgnoreCase(String firstName, String lastName);
	List<InspectionResponse> findByInspectorEmailLikeIgnoreCase(String inspectorEmail);
	List<InspectionResponse> findByInspectorPhoneNumberLikeIgnoreCase(String inspectorPhoneNumber);
	//product
	List<InspectionResponse> findByProductId(Long productId);
	List<InspectionResponse> findByProductCurrentQuantity(BigDecimal currentQuantity);
	List<InspectionResponse> findByProductCurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<InspectionResponse> findByProductCurrentQuantityLessThan(BigDecimal currentQuantity);
	List<InspectionResponse> findByProductCurrentQuantityBetween(BigDecimal min, BigDecimal max);
	List<InspectionResponse> findByProductNameContainingIgnoreCase(String productName);
	List<InspectionResponse> findByProductUnitMeasure(UnitMeasure unitMeasure);
	List<InspectionResponse> findByProductSupplierType(SupplierType supplierType);
	List<InspectionResponse> findByProductStorageType(StorageType storageType);
	List<InspectionResponse> findByProductGoodsType(GoodsType goodsType);
	List<InspectionResponse> findByProduct_SupplyId( Long supplyId);
	List<InspectionResponse> findByProduct_ShelfId( Long shelfId);
	InspectionResponse findByProduct_StorageHasShelvesForIsNull();
	List<InspectionResponse> findByProduct_ShelfRowCount( Integer rowCount);
	List<InspectionResponse> findByProduct_ShelfCols( Integer cols);
	
	List<InspectionResponse> findByProduct_ShelfRowAndColNullable( Integer row, Integer col);
	List<InspectionResponse> findByProduct_ShelfRowAndColBetweenNullable( Integer rowMin, Integer rowMax,  Integer colMin, Integer colMax);
	
	List<InspectionResponse> findByQuantityInspected(Integer quantityInspected);
	List<InspectionResponse> findByQuantityInspectedGreaterThan(Integer quantityInspected);
	List<InspectionResponse> findByQuantityInspectedLessThan(Integer quantityInspected);
	List<InspectionResponse> findByQuantityInspectedBetween(Integer min, Integer max);
	
	List<InspectionResponse> findByQuantityAccepted(Integer quantityAccepted);
	List<InspectionResponse> findByQuantityAcceptedGreaterThan(Integer quantityAccepted);
	List<InspectionResponse> findByQuantityAcceptedLessThan(Integer quantityAccepted);
	List<InspectionResponse> findByQuantityAcceptedBetween(Integer min, Integer max);
	
	List<InspectionResponse> findByQuantityRejected(Integer quantityRejected);
	List<InspectionResponse> findByQuantityRejectedGreaterThan(Integer quantityRejected);
	List<InspectionResponse> findByQuantityRejectedLessThan(Integer quantityRejected);
	List<InspectionResponse> findByQuantityRejectedBetween(Integer min, Integer max);

	//quality-check
	List<InspectionResponse> findByQualityCheckId(Long qualityCheckId);
	List<InspectionResponse> findByQualityCheckLocDate(LocalDateTime locDate);
	List<InspectionResponse> findByQualityCheckLocDateAfter(LocalDateTime locDate);
	List<InspectionResponse> findByQualityCheckLocDateBefore(LocalDateTime locDate);
	List<InspectionResponse> findByQualityCheckLocDateBetween(LocalDateTime start, LocalDateTime end);
	List<InspectionResponse> findByQualityCheckNotes(String notes);
	List<InspectionResponse> findByQualityCheckReferenceId(Long referenceId);
	List<InspectionResponse> findByQualityCheckReferenceType(ReferenceType referenceType);
	List<InspectionResponse> findByQualityCheck_CheckType(QualityCheckType checkType);
	List<InspectionResponse> findByQualityCheck_Status(QualityCheckStatus status);
	List<InspectionResponse> findByQualityCheck_ReferenceTypeAndQualityCheck_CheckType(ReferenceType referenceType, QualityCheckType checkType);
	List<InspectionResponse> findByQualityCheck_ReferenceTypeAndQualityCheck_Status(ReferenceType referenceType, QualityCheckStatus status);
	List<InspectionResponse> findByQualityCheck_CheckTypeAndQualityCheck_Status(QualityCheckType checkType, QualityCheckStatus status);
	List<InspectionResponse> findByQualityCheckInspectorId(Long inspectorId);
	List<InspectionResponse> findByQualityCheckInspectorEmailLikeIgnoreCase(String inspectorEmail);
	List<InspectionResponse> findByQualityCheckInspectorPhoneNumberLikeIgnoreCase(String inspectorPhoneNumber);
	List<InspectionResponse> findByQualityCheckInspectorFirstNameContainingIgnoreCaseAndQualityCheckInspectorLastNameContainingIgnoreCase( String firstName, String lastName);
	
	//nove metode
	InspectionResponse trackInspectionByInspectionDefect(Long id);
	InspectionResponse trackInspectionByTestMeasurement(Long id);
	List<InspectionResponse> findByReports(Long id, String notes);
	InspectionResponse confirmInspection(Long id);
	InspectionResponse cancelInspection(Long id);
	InspectionResponse closeInspection(Long id);
	InspectionResponse changeStatus(Long id, InspectionStatus status);
	InspectionResponse saveInspection(InspectionRequest request);
	InspectionResponse saveAs(InspectionSaveAsRequest request);
	List<InspectionResponse> saveAll(List<InspectionRequest> requests);
	List<InspectionResponse> generalSearch(InspectionSearchRequest request);
	List<QuantityInspectedByBatchStatDTO> countQuantityInspectedByBatch();
	List<QuantityRejectedByBatchStatDTO> countQuantityRejectedByBatch();
	List<QuantityAcceptedByBatchStatDTO> countQuantityAcceptedByBatch();
	List<QuantityInspectedByProductStatDTO> countQuantityInspectedByProduct();
	List<QuantityAcceptedByProductStatDTO> countQuantityAcceptedByProduct();
	List<QuantityRejectedByProductStatDTO> countQuantityRejectedByProduct();
	List<QuantityInspectedByInspectorStatDTO> countQuantityInspectedByInspector();
	List<QuantityAcceptedByInspectorStatDTO> countQuantityAcceptedByInspector();
	List<QuantityRejectedByInspectorStatDTO> countQuantityRejectedByInspector();
	List<QuantityInspectedByQualityCheckStatDTO> countQuantityInspectedByQualityCheck();
	List<QuantityAcceptedByQualityCheckStatDTO> countQuantityAcceptedByQualityCheck();
	List<QuantityRejectedByQualityCheckStatDTO> countQuantityRejectedByQualityCheck();
	List<InspectionTypeStatDTO> countInspectionByType();
	List<InspectionResultStatDTO> countInspectionByResult();
}


