package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.dto.CheckTypeCountResponse;
import com.jovan.erp_v1.dto.DateCountResponse;
import com.jovan.erp_v1.dto.InspectorCountResponse;
import com.jovan.erp_v1.dto.InspectorNameCountResponse;
import com.jovan.erp_v1.dto.ReferenceTypeCountResponse;
import com.jovan.erp_v1.dto.StatusCountResponse;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.request.QualityCheckRequest;
import com.jovan.erp_v1.response.QualityCheckResponse;

public interface IQualityCheckService {

	QualityCheckResponse create(QualityCheckRequest request);
	QualityCheckResponse update(Long id, QualityCheckRequest request);
	void delete(Long id);
	QualityCheckResponse findOne(Long id);
	List<QualityCheckResponse> findAll();
	
	List<QualityCheckResponse> findByReferenceType(ReferenceType referenceType);
	List<QualityCheckResponse> findByCheckType(QualityCheckType checkType);
	List<QualityCheckResponse> findByStatus(QualityCheckStatus status);
	List<QualityCheckResponse> findByCheckTypeAndStatus(QualityCheckType checkType, QualityCheckStatus status);
	List<QualityCheckResponse> findByStatusAndReferenceType(QualityCheckStatus status,ReferenceType referenceType);
	List<QualityCheckResponse> findByCheckTypeAndReferenceType(QualityCheckType checkType, ReferenceType referenceType);
	List<QualityCheckResponse> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
	List<QualityCheckResponse> findByReferenceIdAndCheckType(Long referenceId, QualityCheckType checkType);
	List<QualityCheckResponse> findByReferenceIdAndStatus(Long referenceId, QualityCheckStatus status);
	List<QualityCheckResponse> findByReferenceTypeIn(List<ReferenceType> referenceType);
	List<QualityCheckResponse> findByCheckTypeIn(List<QualityCheckType> checkType);
	List<QualityCheckResponse> findByStatusIn(List<QualityCheckStatus> status);
	List<QualityCheckResponse> findByReferenceIdAndReferenceTypeAndStatus(Long referenceId, ReferenceType referenceType, QualityCheckStatus status);
	List<QualityCheckResponse> findByReferenceIdAndReferenceTypeAndCheckType(Long referenceId, ReferenceType referenceType, QualityCheckType checkType);
	List<QualityCheckResponse> findByNotes(String notes);
	List<QualityCheckResponse> findByReferenceId(Long referenceId);
	List<QualityCheckResponse> findByLocDate(LocalDateTime date);
	List<QualityCheckResponse> findByLocDateBefore(LocalDateTime date);
	List<QualityCheckResponse> findByLocDateAfter(LocalDateTime date);
	List<QualityCheckResponse> findByLocDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	List<QualityCheckResponse> findByStatusAndLocDateBetween(QualityCheckStatus status, LocalDateTime startDate, LocalDateTime endDate);
	List<QualityCheckResponse> findByCheckTypeAndLocDateBetween(QualityCheckType checkType, LocalDateTime startDate, LocalDateTime endDate);
	List<QualityCheckResponse> findByReferenceTypeAndLocDateBetween(ReferenceType referenceType, LocalDateTime startDate, LocalDateTime endDate);
	List<QualityCheckResponse> findByReferenceIdOrderByLocDateDesc(Long referenceId);
	List<QualityCheckResponse> findByInspectorIdOrderByLocDateDesc(Long inspectorId);
	boolean existsByReferenceIdAndReferenceTypeAndStatus(Long referenceId, ReferenceType referenceType, QualityCheckStatus status);
	boolean existsByInspectorIdAndLocDateBetween(Long inspectorId, LocalDateTime startDate, LocalDateTime endDate);
	long countByStatus(QualityCheckStatus status);
	long countByCheckType(QualityCheckType checkType);
	long countByReferenceType(ReferenceType referenceType);
	long countByInspectorId(Long inspectorId);
	long countByReferenceTypeAndStatus(ReferenceType referenceType, QualityCheckStatus status);
	long countByLocDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	
	List<QualityCheckResponse> findByInspectorId(Long inspectorId);
	List<QualityCheckResponse> findByInspectorEmailLikeIgnoreCase(String email);
	List<QualityCheckResponse> findByInspectorPhoneNumberLikeIgnoreCase(String phoneNumber);
	List<QualityCheckResponse> findByInspector_FirstNameContainingIgnoreCaseAndInspector_LastNameContainingIgnoreCase(String firstName, String lastName);
	List<QualityCheckResponse> findByInspectorIdAndStatus(Long inspectorId, QualityCheckStatus status);
	List<QualityCheckResponse> findByInspectorIdAndCheckType(Long inspectorId, QualityCheckType checkType);
	List<QualityCheckResponse> findByInspectorIdAndReferenceType(Long inspectorId, ReferenceType referenceType);
	
	//grupisane metode
	List<StatusCountResponse> countByStatusGrouped();
	List<CheckTypeCountResponse> countByCheckTypeGrouped();
	List<ReferenceTypeCountResponse> countByReferenceTypeGrouped();
	List<InspectorCountResponse> countByInspectorGrouped();
	List<InspectorNameCountResponse> countByInspectorNameGrouped();
	List<DateCountResponse> countByDateGrouped();
}
