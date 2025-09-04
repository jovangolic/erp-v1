package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DefectStatus;
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
	
	@Query("SELECT d FROM Defect d " +
	           "WHERE (:severity IS NULL OR d.severity = :severity) " +
	           "AND (:descPart IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT('%', :descPart, '%'))) " +
	           "AND (:status IS NULL OR d.status = :status) " +
	           "AND (:confirmed IS NULL OR d.confirmed = :confirmed)")
	List<Defect> searchDefects(
		    @Param("severity") SeverityLevel severity, @Param("descPart") String descPart,@Param("status") DefectStatus status,@Param("confirmed") Boolean confirmed);
	
	@Query("SELECT d FROM Defect d "+
			" WHERE ( :id IS NULL OR d.id = :id) "+
			" AND (:idFrom IS NULL OR :idTo IS NULL OR (d.id BETWEEN :idFrom AND :idTo))" +
			" AND ( :idFrom IS NULL OR d.id >= :idFrom)"+
			" AND ( :idTo IS NULL OR d.id <= :idTo)"+
			" AND ( :code IS NULL OR LOWER(d.code) LIKE LOWER(CONCAT('%', :code, '%')))" +
			" AND ( :name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')))" +
			" AND ( :description IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT('%', :description,'%')))" +
			" AND ( :SEVERITY is null or d.severity = :severity)" +
			" AND ( :status IS NULL OR d.status = :status)" +
			" AND ( :confirmed IS NULL OR d.confirmed = :confirmed)")
	List<Defect> generalSearch(@Param("id") Long id,@Param("idFrom") Long idFrom, @Param("idTo") Long idTo,
			@Param("code") String code,@Param("name") String name,@Param("description") String description,
			@Param("severity") SeverityLevel severity,@Param("status") DefectStatus status,@Param("confirmed") Boolean confirmed);
	
	//metoda za pracenje jednog defekta
	@Query("SELECT d FROM Defect d LEFT JOIN FETCH d.inspections WHERE d.id = :id")
    List<Defect> trackDefect(@Param("id") Long id);
	
	Boolean existsByNameContainingIgnoreCase(String name);
	Boolean existsByCodeContainingIgnoreCase(String code);
}
