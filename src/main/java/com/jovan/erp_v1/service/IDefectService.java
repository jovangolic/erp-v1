package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.DefectStatus;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.request.DefectRequest;
import com.jovan.erp_v1.response.DefectResponse;
import com.jovan.erp_v1.save_as.DefectSaveAsRequest;
import com.jovan.erp_v1.statistics.defects.DefectConfirmedStatDTO;
import com.jovan.erp_v1.statistics.defects.DefectMonthlyStatDTO;
import com.jovan.erp_v1.statistics.defects.DefectSeverityStatDTO;
import com.jovan.erp_v1.statistics.defects.DefectStatusSeverityStatDTO;
import com.jovan.erp_v1.statistics.defects.DefectStatusStatDTO;


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
			SeverityLevel severity, DefectStatus status, Boolean confirmed,LocalDateTime created,
	         LocalDateTime createdAfter,LocalDateTime createdBefore);
	
	List<DefectResponse> findByReports( Long id,  String description);
	
	DefectResponse trackDefect(Long id);
	
	//posebne metode za svaku konstantu iz defect-status enuma
	DefectResponse confirmDefect(Long id);
	DefectResponse closeDefect(Long id);
	DefectResponse cancelDefect(Long id);
	//genericka metoda za dodavanje novih defekt-statusa
	DefectResponse changeStatus(Long id, DefectStatus newStatus);
	
	//date-time
	List<DefectResponse> findByCreatedDate(LocalDateTime createdDate);
	List<DefectResponse> findByCreatedDateAfter(LocalDateTime createdDate);
	List<DefectResponse> findByCreatedDateBefore(LocalDateTime createdDate);
	List<DefectResponse> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
	Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
	//defect stats
	List<DefectSeverityStatDTO> countDefectsBySeverity();
	List<DefectStatusStatDTO> countDefectsByStatus();
	List<DefectConfirmedStatDTO> countDefectsByConfirmed();
	List<DefectStatusSeverityStatDTO> countDefectsByStatusAndSeverity();
	List<DefectMonthlyStatDTO> countDefectsByYearAndMonth();
	
	//general search with date-only
	List<DefectResponse> generalSearch(
	        Long id,
	        Long idFrom,
	        Long idTo,
	        String code,
	        String name,
	        String description,
	        SeverityLevel severity,
	        DefectStatus status,
	        Boolean confirmed,
	        LocalDate dateOnly
	);
	
	List<DefectResponse> searchByDateOnly(LocalDate dateOnly);
	
	//metode za save, save_as, save_all
	DefectResponse saveDefects(DefectRequest request);
	//DefectResponse saveAs(DefectSaveAsRequest request);
	//List<DefectResponse> saveAll(List<DefectRequest> requests);
	
	//genericke metode za saveAs i saveAll
	DefectResponse saveAs(DefectSaveAsRequest request);
	List<DefectResponse> saveAll(List<DefectRequest> defects);
}
