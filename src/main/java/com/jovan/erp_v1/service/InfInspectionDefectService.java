package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionDefectStatus;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.InspectionDefectRequest;
import com.jovan.erp_v1.response.InspectionDefectResponse;
import com.jovan.erp_v1.save_as.InspectionDefectSaveAsRequest;
import com.jovan.erp_v1.search_request.InspectionDefectSearchRequest;
import com.jovan.erp_v1.statistics.inspection_defect.InspectionDefectQuantityAffectedSummaryDTO;
import com.jovan.erp_v1.statistics.inspection_defect.QuantityAffectedByDefectStatDTO;
import com.jovan.erp_v1.statistics.inspection_defect.QuantityAffectedByInspectionStatDTO;

public interface InfInspectionDefectService {

	InspectionDefectResponse create(InspectionDefectRequest request);
	InspectionDefectResponse update(Long id, InspectionDefectRequest request);
	void delete(Long id);
	InspectionDefectResponse findOne(Long id);
	List<InspectionDefectResponse> findAll();
	
	List<InspectionDefectResponse> findByQuantityAffected(Integer quantityAffected);
	List<InspectionDefectResponse> findByQuantityAffectedGreaterThan(Integer quantityAffected);
	List<InspectionDefectResponse> findByQuantityAffectedLessThan(Integer quantityAffected);
	List<InspectionDefectResponse> findByQuantityAffectedBetween(Integer min, Integer max);
	
	//inspection
	List<InspectionDefectResponse> findByInspectionId(Long inspectionId);
	boolean existsByInspectionCode(String inspectionCode);
	List<InspectionDefectResponse> findByInspection_CodeLikeIgnoreCase(String inspectionCode);
	List<InspectionDefectResponse> findByInspection_Type(InspectionType type);
	List<InspectionDefectResponse> findByInspection_Result(InspectionResult result);
	List<InspectionDefectResponse> findByInspection_Notes(String notes);
	List<InspectionDefectResponse> findByInspection_TypeAndInspection_Result(InspectionType type, InspectionResult result);
	List<InspectionDefectResponse> findByInspection_NotesAndInspection_Type(String notes, InspectionType type);
	List<InspectionDefectResponse> findByInspection_NotesAndInspection_Result(String notes, InspectionResult result);
	List<InspectionDefectResponse> findByInspection_InspectionDate(LocalDateTime inspectionDate);
	List<InspectionDefectResponse> findByInspection_InspectionDateBefore(LocalDateTime inspectionDate);
	List<InspectionDefectResponse> findByInspection_InspectionDateAfter(LocalDateTime inspectionDate);
	List<InspectionDefectResponse> findByInspection_InspectionDateBetween(LocalDateTime start, LocalDateTime end);
	List<InspectionDefectResponse> findByInspection_InspectionDateAndInspection_Result(LocalDateTime inspectionDate, InspectionResult result);
	
	List<InspectionDefectResponse> findByInspection_InspectorId(Long inspectorId);
	List<InspectionDefectResponse> findByInspection_InspectorFirstNameContainingIgnoreCaseAndInspection_InspectorLastNameContainingIgnoreCase(String firstName, String lastName);
	List<InspectionDefectResponse> findByInspection_InspectorEmailLikeIgnoreCase(String inspectorEmail);
	List<InspectionDefectResponse> findByInspection_InspectorPhoneNumberLikeIgnoreCase(String inspectorPhoneNumber);
	
	List<InspectionDefectResponse> findByInspection_QuantityInspected(Integer quantityInspected);
	List<InspectionDefectResponse> findByInspection_QuantityInspectedGreaterThan(Integer quantityInspected);
	List<InspectionDefectResponse> findByInspection_QuantityInspectedLessThan(Integer quantityInspected);
	List<InspectionDefectResponse> findByInspection_QuantityInspectedBetween(Integer min, Integer max);
	
	List<InspectionDefectResponse> findByInspection_QuantityAccepted(Integer quantityAccepted);
	List<InspectionDefectResponse> findByInspection_QuantityAcceptedGreaterThan(Integer quantityAccepted);
	List<InspectionDefectResponse> findByInspection_QuantityAcceptedLessThan(Integer quantityAccepted);
	List<InspectionDefectResponse> findByInspection_QuantityAcceptedBetween(Integer min, Integer max);
	
	List<InspectionDefectResponse> findByInspection_QuantityRejected(Integer quantityRejected);
	List<InspectionDefectResponse> findByInspection_QuantityRejectedGreaterThan(Integer quantityRejected);
	List<InspectionDefectResponse> findByInspection_QuantityRejectedLessThan(Integer quantityRejected);
	List<InspectionDefectResponse> findByInspection_QuantityRejectedBetween(Integer min, Integer max);
	
	List<InspectionDefectResponse> findByInspection_QualityCheck_Id(Long qualityCheckId);
	List<InspectionDefectResponse> findByInspection_QualityCheck_LocDate(LocalDateTime locDate);
	List<InspectionDefectResponse> findByInspection_QualityCheck_LocDateAfter(LocalDateTime locDate);
	List<InspectionDefectResponse> findByInspection_QualityCheck_LocDateBefore(LocalDateTime locDate);
	List<InspectionDefectResponse> findByInspection_QualityCheck_LocDateBetween(LocalDateTime start, LocalDateTime end);
	List<InspectionDefectResponse> findByInspection_QualityCheck_Notes(String notes);
	List<InspectionDefectResponse> findByInspection_QualityCheck_ReferenceId(Long referenceId);
	List<InspectionDefectResponse> findByInspection_QualityCheck_ReferenceType(ReferenceType referenceType);
	List<InspectionDefectResponse> findByInspection_QualityCheck_CheckType(QualityCheckType checkType);
	List<InspectionDefectResponse> findByInspection_QualityCheck_Status(QualityCheckStatus status);
	List<InspectionDefectResponse> findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_CheckType(ReferenceType referenceType, QualityCheckType checkType);
	List<InspectionDefectResponse> findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_Status(ReferenceType referenceType, QualityCheckStatus status);
	List<InspectionDefectResponse> findByInspection_QualityCheck_CheckTypeAndInspection_QualityCheck_Status(QualityCheckType checkType, QualityCheckStatus status);
	
	List<InspectionDefectResponse> findByInspection_Product_Id(Long productId);
	List<InspectionDefectResponse> findByInspection_Product_CurrentQuantity(BigDecimal currentQuantity);
	List<InspectionDefectResponse> findByInspection_Product_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<InspectionDefectResponse> findByInspection_Product_CurrentQuantityLessThan(BigDecimal currentQuantity);
	List<InspectionDefectResponse> findByInspection_Product_CurrentQuantityBetween(BigDecimal min, BigDecimal max);
	List<InspectionDefectResponse> findByInspection_Product_NameContainingIgnoreCase(String productName);
	List<InspectionDefectResponse> findByInspection_Product_UnitMeasure(UnitMeasure unitMeasure);
	List<InspectionDefectResponse> findByInspection_Product_SupplierType(SupplierType supplierType);
	List<InspectionDefectResponse> findByInspection_Product_StorageType(StorageType storageType);
	List<InspectionDefectResponse> findByInspection_Product_GoodsType(GoodsType goodsType);
	List<InspectionDefectResponse> findByInspection_Batch_Id(Long batchId);
	
	//defect
	List<InspectionDefectResponse> findByDefect_CodeContainingIgnoreCase(String code);
	List<InspectionDefectResponse> findByDefect_NameContainingIgnoreCase(String name);
	List<InspectionDefectResponse> findByDefect_DescriptionContainingIgnoreCase(String description);
	List<InspectionDefectResponse> findByDefect_CodeContainingIgnoreCaseAndDefect_NameContainingIgnoreCase(String code, String name);
	List<InspectionDefectResponse> findByDefect_Severity(SeverityLevel severity);
	List<InspectionDefectResponse> findByDefect_CodeContainingIgnoreCaseAndDefect_Severity(String code, SeverityLevel severity);
	List<InspectionDefectResponse> findByDefect_NameContainingIgnoreCaseAndDefect_Severity(String name, SeverityLevel severity);
	List<InspectionDefectResponse> findByDefect_SeverityAndDefect_DescriptionContainingIgnoreCase(SeverityLevel severity, String descPart);
	Long countByDefect_Severity(SeverityLevel severity);
	Long countByDefect_Code(String code);
	Long countByDefect_Name(String name);
	Boolean existsByDefect_Code(String code);
	Boolean existsByDefect_Name(String name);
	
	List<InspectionDefectResponse> findByConfirmed(Boolean confirmed);
	List<InspectionDefectResponse> findByDefectIdAndConfirmed(Long defectId, Boolean confirmed);
	
	//nove metode
	InspectionDefectResponse trackInspectionDefec(Long id);
	InspectionDefectResponse confirmInspectionDefect(Long id);
	InspectionDefectResponse cancelInspectionDefect(Long id);
	InspectionDefectResponse closeInspectionDefect(Long id);
	InspectionDefectResponse changeStatus(Long id, InspectionDefectStatus status);
	InspectionDefectResponse saveInspectionDefect(InspectionDefectRequest request);
	InspectionDefectResponse saveAs(InspectionDefectSaveAsRequest request);
	List<InspectionDefectResponse> saveAll(List<InspectionDefectRequest> request);
	List<InspectionDefectResponse> generalSearch(InspectionDefectSearchRequest request);
	InspectionDefectQuantityAffectedSummaryDTO getQuantityAffectedSummary();
	List<QuantityAffectedByInspectionStatDTO> countQuantityAffectedByInspection();
	List<QuantityAffectedByDefectStatDTO> countQuantityAffectedByDefect();
}
