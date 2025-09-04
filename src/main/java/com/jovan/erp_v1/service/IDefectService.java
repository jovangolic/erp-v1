package com.jovan.erp_v1.service;

import java.util.List;
import com.jovan.erp_v1.enumeration.DefectStatus;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.request.DefectRequest;
import com.jovan.erp_v1.response.DefectResponse;

public interface IDefectService {

	DefectResponse create(DefectRequest request);
	DefectResponse update(Long id, DefectRequest request);
	void delete(Long id);
	DefectResponse findOne(Long id);
	List<DefectResponse> findAll();
	List<DefectResponse> findByCodeContainingIgnoreCase(String code);
	List<DefectResponse> findByNameContainingIgnoreCase(String name);
	List<DefectResponse> findByDescriptionContainingIgnoreCase(String description);
	List<DefectResponse> findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(String code, String name);
	List<DefectResponse> findBySeverity(SeverityLevel severity);
	List<DefectResponse> findByCodeContainingIgnoreCaseAndSeverity(String code, SeverityLevel severity);
	List<DefectResponse> findByNameContainingIgnoreCaseAndSeverity(String name, SeverityLevel severity);
	List<DefectResponse> findAllByOrderBySeverityAsc();
	List<DefectResponse> findAllByOrderBySeverityDesc();
	List<DefectResponse> findBySeverityAndDescriptionContainingIgnoreCase(SeverityLevel severity, String descPart);
	Long countBySeverity(SeverityLevel severity);
	Long countByCode(String code);
	Long countByName(String name);
	List<DefectResponse> findBySeverityIn( List<SeverityLevel> levels);
	Boolean existsByNameContainingIgnoreCase(String name);
	Boolean existsByCodeContainingIgnoreCase(String code);
	
	List<DefectResponse> searchDefects( SeverityLevel severity, String descPart, DefectStatus status, Boolean confirmed);
	
	List<DefectResponse> generalSearch( Long id, Long idFrom,  Long idTo, String code, String name, String description,
			SeverityLevel severity, DefectStatus status, Boolean confirmed);
	
	List<DefectResponse> findByReports( Long id,  String description);
	
	DefectResponse trackDefect(Long id);
	
	//posebne metode za svaku konstantu iz defect-status enuma
	DefectResponse confirmDefect(Long id);
	DefectResponse closeDefect(Long id);
	DefectResponse cancelDefect(Long id);
	//genericka metoda za dodavanje novih defekt-statusa
	DefectResponse changeStatus(Long id, DefectStatus newStatus);
}
