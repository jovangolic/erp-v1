package com.jovan.erp_v1.service;

import java.util.List;
import com.jovan.erp_v1.enumeration.GoToCategory;
import com.jovan.erp_v1.enumeration.GoToType;
import com.jovan.erp_v1.request.GoToRequest;
import com.jovan.erp_v1.response.GoToResponse;

public interface IGoToService {

	GoToResponse create(GoToRequest request);
	GoToResponse update(Long id, GoToRequest request);
	void delete(Long id);
	GoToResponse findOne(Long id);
	List<GoToResponse> findAll();
	List<GoToResponse> findByCategoryAndActiveTrue(GoToCategory category);
	List<GoToResponse> findByLabelContainingIgnoreCase(String label);
	List<GoToResponse> findByDescriptionContainingIgnoreCase(String description);
	List<GoToResponse> findByRolesContainingIgnoreCase(String roles);
	List<GoToResponse> findByCategory(GoToCategory category);
	List<GoToResponse> findByType(GoToType type);
	List<GoToResponse> findByCategoryAndType(GoToCategory category, GoToType type);
	List<GoToResponse> findByPathContainingIgnoreCase(String path);
	List<GoToResponse> findByIcon(String icon);
	List<GoToResponse> findByActiveTrue();
	List<GoToResponse> findByLabelContainingIgnoreCaseAndCategory(String label, GoToCategory category);
	List<GoToResponse> findByDescriptionContainingIgnoreCaseAndCategory(String description, GoToCategory category);
	List<GoToResponse> searchByKeyword( String keyword);
	List<GoToResponse> findByActiveTrueOrderByLabelAsc();
	List<GoToResponse> findByCategoryIn(List<GoToCategory> categories);
	List<GoToResponse> findByLabelContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String label, String description);
	
	Boolean existsByLabel(String label);
	Boolean existsByCategory(GoToCategory category);
	Boolean existsByRolesContainingIgnoreCase(String roles);
	Boolean existsByPathContainingIgnoreCase(String path);
	Boolean existsByIcon(String icon);
	Boolean existsByLabelAndCategory(String label, GoToCategory category);
	Boolean existsByPath(String path);
}
