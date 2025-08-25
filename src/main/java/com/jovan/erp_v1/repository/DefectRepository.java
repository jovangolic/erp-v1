package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.model.Defect;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {

	List<Defect> findByCodeContainingIgnoreCase(String code);
	List<Defect> findByNameContainingIgnoreCase(String name);
	List<Defect> findByDescriptionContainingIgnoreCase(String description);
	List<Defect> findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(String code, String name);
	List<Defect> findBySeverity(SeverityLevel severity);
	List<Defect> findByCodeContainingIgnoreCaseAndSeverity(String code, SeverityLevel severity);
	List<Defect> findByNameContainingIgnoreCaseAndSeverity(String name, SeverityLevel severity);
	List<Defect> findAllByOrderBySeverityAsc();
	List<Defect> findAllByOrderBySeverityDesc();
	List<Defect> findBySeverityAndDescriptionContainingIgnoreCase(SeverityLevel severity, String descPart);
	Long countBySeverity(SeverityLevel severity);
	Long countByCode(String code);
	Long countByName(String name);
	@Query("SELECT d FROM Defect d WHERE d.severity IN :levels")
	List<Defect> findBySeverityIn(@Param("levels") List<SeverityLevel> levels);
	
	Boolean existsByNameContainingIgnoreCase(String name);
	Boolean existsByCodeContainingIgnoreCase(String code);
}
