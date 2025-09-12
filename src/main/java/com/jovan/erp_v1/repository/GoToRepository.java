package com.jovan.erp_v1.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoToCategory;
import com.jovan.erp_v1.enumeration.GoToType;
import com.jovan.erp_v1.model.GoTo;

@Repository
public interface GoToRepository extends JpaRepository<GoTo, Long> {

	List<GoTo> findByCategoryAndActiveTrue(GoToCategory category);
	List<GoTo> findByLabelContainingIgnoreCase(String label);
	List<GoTo> findByDescriptionContainingIgnoreCase(String description);
	List<GoTo> findByRolesContainingIgnoreCase(String roles);
	List<GoTo> findByCategory(GoToCategory category);
	List<GoTo> findByType(GoToType type);
	List<GoTo> findByCategoryAndType(GoToCategory category, GoToType type);
	List<GoTo> findByPathContainingIgnoreCase(String path);
	List<GoTo> findByIcon(String icon);
	List<GoTo> findByActiveTrue();
	List<GoTo> findByLabelContainingIgnoreCaseAndCategory(String label, GoToCategory category);
	List<GoTo> findByDescriptionContainingIgnoreCaseAndCategory(String description, GoToCategory category);
	@Query("SELECT g FROM GoTo g WHERE " +
		       "LOWER(g.label) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<GoTo> searchByKeyword(@Param("keyword") String keyword);
	List<GoTo> findByActiveTrueOrderByLabelAsc();
	List<GoTo> findByCategoryIn(List<GoToCategory> categories);
	List<GoTo> findByLabelContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String label, String description);
	
	Boolean existsByLabel(String label);
	Boolean existsByCategory(GoToCategory category);
	Boolean existsByRolesContainingIgnoreCase(String roles);
	Boolean existsByPathContainingIgnoreCase(String path);
	Boolean existsByIcon(String icon);
	Boolean existsByLabelAndCategory(String label, GoToCategory category);
	Boolean existsByPath(String path);
}
