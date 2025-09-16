package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DefectStatus;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.model.Defect;
import com.jovan.erp_v1.statistics.defects.DefectConfirmedStatDTO;
import com.jovan.erp_v1.statistics.defects.DefectMonthlyStatDTO;
import com.jovan.erp_v1.statistics.defects.DefectSeverityStatDTO;
import com.jovan.erp_v1.statistics.defects.DefectStatusSeverityStatDTO;
import com.jovan.erp_v1.statistics.defects.DefectStatusStatDTO;

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
	
	@Query("""
		    SELECT d FROM Defect d
		     WHERE (:id IS NULL OR d.id = :id)
		       AND (:idFrom IS NULL OR d.id >= :idFrom)
		       AND (:idTo IS NULL OR d.id <= :idTo)
		       AND (:code IS NULL OR LOWER(d.code) LIKE LOWER(CONCAT('%', :code, '%')))
		       AND (:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')))
		       AND (:description IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT('%', :description, '%')))
		       AND (:severity IS NULL OR d.severity = :severity)
		       AND (:status IS NULL OR d.status = :status)
		       AND (:confirmed IS NULL OR d.confirmed = :confirmed)
		       AND (:created IS NULL OR d.createdDate = :created)
		       AND (:createdAfter IS NULL OR d.createdDate >= :createdAfter)
		       AND (:createdBefore IS NULL OR d.createdDate <= :createdBefore)
		""")
	List<Defect> generalSearch(
	        @Param("id") Long id,
	        @Param("idFrom") Long idFrom,
	        @Param("idTo") Long idTo,
	        @Param("code") String code,
	        @Param("name") String name,
	        @Param("description") String description,
	        @Param("severity") SeverityLevel severity,
	        @Param("status") DefectStatus status,
	        @Param("confirmed") Boolean confirmed,
	        @Param("created") LocalDateTime created,
	        @Param("createdAfter") LocalDateTime createdAfter,
	        @Param("createdBefore") LocalDateTime createdBefore
	);
	
	@Query("SELECT d FROM Defect d " +
		       "WHERE d.createdDate >= :startOfDay AND d.createdDate <= :endOfDay")
	List<Defect> findByCreatedDateOnly(@Param("startOfDay") LocalDateTime startOfDay,
		                                   @Param("endOfDay") LocalDateTime endOfDay);
	
	@Query("SELECT d FROM Defect d " +
	           "WHERE (:id IS NULL OR d.id = :id) " +
	           "AND (:description IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT('%', :description, '%')))")
	List<Defect> findByReports(@Param("id") Long id,
	                               @Param("description") String description);
	//metoda za pracenje jednog defekta
	@Query("SELECT d FROM Defect d LEFT JOIN FETCH d.inspections WHERE d.id = :id")
    List<Defect> trackDefect(@Param("id") Long id);
	
	Boolean existsByNameContainingIgnoreCase(String name);
	Boolean existsByCodeContainingIgnoreCase(String code);
	
	//date-time
	List<Defect> findByCreatedDate(LocalDateTime createdDate);
	List<Defect> findByCreatedDateAfter(LocalDateTime createdDate);
	List<Defect> findByCreatedDateBefore(LocalDateTime createdDate);
	List<Defect> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
	Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
	
	//defect stats
	@Query("SELECT new com.jovan.erp_v1.statistics.defects.DefectSeverityStatDTO(d.severity, COUNT(d)) " +
		       "FROM Defect d GROUP BY d.severity")
	List<DefectSeverityStatDTO> countDefectsBySeverity();

	@Query("SELECT new com.jovan.erp_v1.statistics.defects.DefectStatusStatDTO(d.status, COUNT(d)) " +
		       "FROM Defect d GROUP BY d.status")
	List<DefectStatusStatDTO> countDefectsByStatus();

	@Query("SELECT new com.jovan.erp_v1.statistics.defects.DefectConfirmedStatDTO(d.confirmed, COUNT(d)) " +
		       "FROM Defect d GROUP BY d.confirmed")
	List<DefectConfirmedStatDTO> countDefectsByConfirmed();

	@Query("SELECT new com.jovan.erp_v1.statistics.defects.DefectStatusSeverityStatDTO(d.status, d.severity, COUNT(d)) " +
		       "FROM Defect d GROUP BY d.status, d.severity")
	List<DefectStatusSeverityStatDTO> countDefectsByStatusAndSeverity();

	@Query("SELECT new com.jovan.erp_v1.statistics.defects.DefectMonthlyStatDTO(" +
		       "CAST(FUNCTION('YEAR', d.createdDate) AS integer), " +
		       "CAST(FUNCTION('MONTH', d.createdDate) AS integer), " +
		       "COUNT(d)) " +
		       "FROM Defect d " +
		       "GROUP BY FUNCTION('YEAR', d.createdDate), FUNCTION('MONTH', d.createdDate) " +
		       "ORDER BY FUNCTION('YEAR', d.createdDate), FUNCTION('MONTH', d.createdDate)")
	List<DefectMonthlyStatDTO> countDefectsByYearAndMonth();
}
