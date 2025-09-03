package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.InspectionDefect;

@Repository
public interface InspectionDefectRepository extends JpaRepository<InspectionDefect, Long> {

	List<InspectionDefect> findByQuantityAffected(Integer quantityAffected);
	List<InspectionDefect> findByQuantityAffectedGreaterThan(Integer quantityAffected);
	List<InspectionDefect> findByQuantityAffectedLessThan(Integer quantityAffected);
	List<InspectionDefect> findByQuantityAffectedBetween(Integer min, Integer max);
	
	//inspection
	List<InspectionDefect> findByInspectionId(Long inspectionId);
	boolean existsByInspectionCode(String inspectionCode);
	List<InspectionDefect> findByInspection_CodeLikeIgnoreCase(String inspectionCode);
	List<InspectionDefect> findByInspection_Type(InspectionType type);
	List<InspectionDefect> findByInspection_Result(InspectionResult result);
	List<InspectionDefect> findByInspection_Notes(String notes);
	List<InspectionDefect> findByInspection_TypeAndInspection_Result(InspectionType type, InspectionResult result);
	List<InspectionDefect> findByInspection_NotesAndInspection_Type(String notes, InspectionType type);
	List<InspectionDefect> findByInspection_NotesAndInspection_Result(String notes, InspectionResult result);
	List<InspectionDefect> findByInspection_InspectionDate(LocalDateTime inspectionDate);
	List<InspectionDefect> findByInspection_InspectionDateBefore(LocalDateTime inspectionDate);
	List<InspectionDefect> findByInspection_InspectionDateAfter(LocalDateTime inspectionDate);
	List<InspectionDefect> findByInspection_InspectionDateBetween(LocalDateTime start, LocalDateTime end);
	List<InspectionDefect> findByInspection_InspectionDateAndInspection_Result(LocalDateTime inspectionDate, InspectionResult result);
	
	List<InspectionDefect> findByInspection_InspectorId(Long inspectorId);
	List<InspectionDefect> findByInspection_InspectorFirstNameContainingIgnoreCaseAndInspection_InspectorLastNameContainingIgnoreCase(String firstName, String lastName);
	List<InspectionDefect> findByInspection_InspectorEmailLikeIgnoreCase(String inspectorEmail);
	List<InspectionDefect> findByInspection_InspectorPhoneNumberLikeIgnoreCase(String inspectorPhoneNumber);
	
	List<InspectionDefect> findByInspection_QuantityInspected(Integer quantityInspected);
	List<InspectionDefect> findByInspection_QuantityInspectedGreaterThan(Integer quantityInspected);
	List<InspectionDefect> findByInspection_QuantityInspectedLessThan(Integer quantityInspected);
	List<InspectionDefect> findByInspection_QuantityInspectedBetween(Integer min, Integer max);
	
	List<InspectionDefect> findByInspection_QuantityAccepted(Integer quantityAccepted);
	List<InspectionDefect> findByInspection_QuantityAcceptedGreaterThan(Integer quantityAccepted);
	List<InspectionDefect> findByInspection_QuantityAcceptedLessThan(Integer quantityAccepted);
	List<InspectionDefect> findByInspection_QuantityAcceptedBetween(Integer min, Integer max);
	
	List<InspectionDefect> findByInspection_QuantityRejected(Integer quantityRejected);
	List<InspectionDefect> findByInspection_QuantityRejectedGreaterThan(Integer quantityRejected);
	List<InspectionDefect> findByInspection_QuantityRejectedLessThan(Integer quantityRejected);
	List<InspectionDefect> findByInspection_QuantityRejectedBetween(Integer min, Integer max);
	
	List<InspectionDefect> findByInspection_QualityCheck_Id(Long qualityCheckId);
	List<InspectionDefect> findByInspection_QualityCheck_LocDate(LocalDateTime locDate);
	List<InspectionDefect> findByInspection_QualityCheck_LocDateAfter(LocalDateTime locDate);
	List<InspectionDefect> findByInspection_QualityCheck_LocDateBefore(LocalDateTime locDate);
	List<InspectionDefect> findByInspection_QualityCheck_LocDateBetween(LocalDateTime start, LocalDateTime end);
	List<InspectionDefect> findByInspection_QualityCheck_Notes(String notes);
	List<InspectionDefect> findByInspection_QualityCheck_ReferenceId(Long referenceId);
	List<InspectionDefect> findByInspection_QualityCheck_ReferenceType(ReferenceType referenceType);
	List<InspectionDefect> findByInspection_QualityCheck_CheckType(QualityCheckType checkType);
	List<InspectionDefect> findByInspection_QualityCheck_Status(QualityCheckStatus status);
	List<InspectionDefect> findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_CheckType(ReferenceType referenceType, QualityCheckType checkType);
	List<InspectionDefect> findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_Status(ReferenceType referenceType, QualityCheckStatus status);
	List<InspectionDefect> findByInspection_QualityCheck_CheckTypeAndInspection_QualityCheck_Status(QualityCheckType checkType, QualityCheckStatus status);
	
	List<InspectionDefect> findByInspection_Product_Id(Long productId);
	List<InspectionDefect> findByInspection_Product_CurrentQuantity(BigDecimal currentQuantity);
	List<InspectionDefect> findByInspection_Product_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
	List<InspectionDefect> findByInspection_Product_CurrentQuantityLessThan(BigDecimal currentQuantity);
	List<InspectionDefect> findByInspection_Product_CurrentQuantityBetween(BigDecimal min, BigDecimal max);
	List<InspectionDefect> findByInspection_Product_NameContainingIgnoreCase(String productName);
	List<InspectionDefect> findByInspection_Product_UnitMeasure(UnitMeasure unitMeasure);
	List<InspectionDefect> findByInspection_Product_SupplierType(SupplierType supplierType);
	List<InspectionDefect> findByInspection_Product_StorageType(StorageType storageType);
	List<InspectionDefect> findByInspection_Product_GoodsType(GoodsType goodsType);
	List<InspectionDefect> findByInspection_Batch_Id(Long batchId);
	
	//defect
	List<InspectionDefect> findByDefect_CodeContainingIgnoreCase(String code);
	List<InspectionDefect> findByDefect_NameContainingIgnoreCase(String name);
	List<InspectionDefect> findByDefect_DescriptionContainingIgnoreCase(String description);
	List<InspectionDefect> findByDefect_CodeContainingIgnoreCaseAndDefect_NameContainingIgnoreCase(String code, String name);
	List<InspectionDefect> findByDefect_Severity(SeverityLevel severity);
	List<InspectionDefect> findByDefect_CodeContainingIgnoreCaseAndDefect_Severity(String code, SeverityLevel severity);
	List<InspectionDefect> findByDefect_NameContainingIgnoreCaseAndDefect_Severity(String name, SeverityLevel severity);
	List<InspectionDefect> findByDefect_SeverityAndDefect_DescriptionContainingIgnoreCase(SeverityLevel severity, String descPart);
	Long countByDefect_Severity(SeverityLevel severity);
	Long countByDefect_Code(String code);
	Long countByDefect_Name(String name);
	Boolean existsByDefect_Code(String code);
	Boolean existsByDefect_Name(String name);
	
	List<InspectionDefect> findByConfirmed(Boolean confirmed);
	List<InspectionDefect> findByDefectIdAndConfirmed(Long defectId, Boolean confirmed);
}
