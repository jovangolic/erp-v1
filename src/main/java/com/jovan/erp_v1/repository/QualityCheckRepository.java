package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.dto.CheckTypeCountResponse;
import com.jovan.erp_v1.dto.DateCountResponse;
import com.jovan.erp_v1.dto.InspectorCountResponse;
import com.jovan.erp_v1.dto.InspectorNameCountResponse;
import com.jovan.erp_v1.dto.ReferenceTypeCountResponse;
import com.jovan.erp_v1.dto.StatusCountResponse;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.model.QualityCheck;

@Repository
public interface QualityCheckRepository extends JpaRepository<QualityCheck, Long> {

	List<QualityCheck> findByReferenceType(ReferenceType referenceType);
	List<QualityCheck> findByCheckType(QualityCheckType checkType);
	List<QualityCheck> findByStatus(QualityCheckStatus status);
	List<QualityCheck> findByCheckTypeAndStatus(QualityCheckType checkType, QualityCheckStatus status);
	List<QualityCheck> findByStatusAndReferenceType(QualityCheckStatus status,ReferenceType referenceType);
	List<QualityCheck> findByCheckTypeAndReferenceType(QualityCheckType checkType, ReferenceType referenceType);
	List<QualityCheck> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
	List<QualityCheck> findByReferenceIdAndCheckType(Long referenceId, QualityCheckType checkType);
	List<QualityCheck> findByReferenceIdAndStatus(Long referenceId, QualityCheckStatus status);
	List<QualityCheck> findByReferenceTypeIn(List<ReferenceType> referenceType);
	List<QualityCheck> findByCheckTypeIn(List<QualityCheckType> checkType);
	List<QualityCheck> findByStatusIn(List<QualityCheckStatus> status);
	List<QualityCheck> findByReferenceIdAndReferenceTypeAndStatus(Long referenceId, ReferenceType referenceType, QualityCheckStatus status);
	List<QualityCheck> findByReferenceIdAndReferenceTypeAndCheckType(Long referenceId, ReferenceType referenceType, QualityCheckType checkType);
	List<QualityCheck> findByNotes(String notes);
	List<QualityCheck> findByReferenceId(Long referenceId);
	List<QualityCheck> findByLocDate(LocalDateTime date);
	List<QualityCheck> findByLocDateBefore(LocalDateTime date);
	List<QualityCheck> findByLocDateAfter(LocalDateTime date);
	List<QualityCheck> findByLocDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	List<QualityCheck> findByStatusAndLocDateBetween(QualityCheckStatus status, LocalDateTime startDate, LocalDateTime endDate);
	List<QualityCheck> findByCheckTypeAndLocDateBetween(QualityCheckType checkType, LocalDateTime startDate, LocalDateTime endDate);
	List<QualityCheck> findByReferenceTypeAndLocDateBetween(ReferenceType referenceType, LocalDateTime startDate, LocalDateTime endDate);
	List<QualityCheck> findByReferenceIdOrderByLocDateDesc(Long referenceId);
	List<QualityCheck> findByInspectorIdOrderByLocDateDesc(Long inspectorId);
	boolean existsByReferenceIdAndReferenceTypeAndStatus(Long referenceId, ReferenceType referenceType, QualityCheckStatus status);
	boolean existsByInspectorIdAndLocDateBetween(Long inspectorId, LocalDateTime startDate, LocalDateTime endDate);
	long countByStatus(QualityCheckStatus status);
	long countByCheckType(QualityCheckType checkType);
	long countByReferenceType(ReferenceType referenceType);
	long countByInspectorId(Long inspectorId);
	long countByReferenceTypeAndStatus(ReferenceType referenceType, QualityCheckStatus status);
	long countByLocDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	
	List<QualityCheck> findByInspectorId(Long inspectorId);
	List<QualityCheck> findByInspectorEmailLikeIgnoreCase(String email);
	List<QualityCheck> findByInspectorPhoneNumberLikeIgnoreCase(String phoneNumber);
	List<QualityCheck> findByInspector_FirstNameContainingIgnoreCaseAndInspector_LastNameContainingIgnoreCase(String firstName, String lastName);
	List<QualityCheck> findByInspectorIdAndStatus(Long inspectorId, QualityCheckStatus status);
	List<QualityCheck> findByInspectorIdAndCheckType(Long inspectorId, QualityCheckType checkType);
	List<QualityCheck> findByInspectorIdAndReferenceType(Long inspectorId, ReferenceType referenceType);
	
	//grupisane metode
	@Query("SELECT new com.jovan.erp_v1.dto.StatusCountResponse(qc.status, COUNT(qc)) " +
		       "FROM QualityCheck qc GROUP BY qc.status")
	List<StatusCountResponse> countByStatusGrouped();
	
	@Query("SELECT new com.jovan.erp_v1.dto.CheckTypeCountResponse(qc.checkType, COUNT(qc)) " +
		       "FROM QualityCheck qc GROUP BY qc.checkType")
	List<CheckTypeCountResponse> countByCheckTypeGrouped();
	@Query("SELECT new com.jovan.erp_v1.dto.ReferenceTypeCountResponse(qc.referenceType, COUNT(qc)) " +
		       "FROM QualityCheck qc GROUP BY qc.referenceType")
	List<ReferenceTypeCountResponse> countByReferenceTypeGrouped();
	@Query("SELECT new com.jovan.erp_v1.dto.InspectorCountResponse(qc.inspector.id, COUNT(qc)) " +
		       "FROM QualityCheck qc GROUP BY qc.inspector.id")
	List<InspectorCountResponse> countByInspectorGrouped();
	@Query("SELECT new com.jovan.erp_v1.dto.InspectorNameCountResponse(" +
		       "CONCAT(qc.inspector.firstName, ' ', qc.inspector.lastName), COUNT(qc)) " +
		       "FROM QualityCheck qc GROUP BY qc.inspector.firstName, qc.inspector.lastName")
	List<InspectorNameCountResponse> countByInspectorNameGrouped();
	@Query("SELECT new com.jovan.erp_v1.dto.DateCountResponse(CAST(qc.locDate AS LocalDate), COUNT(qc)) " +
		       "FROM QualityCheck qc GROUP BY CAST(qc.locDate AS LocalDate) ORDER BY CAST(qc.locDate AS LocalDate)")
	List<DateCountResponse> countByDateGrouped();
}
