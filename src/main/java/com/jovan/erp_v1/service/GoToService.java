package com.jovan.erp_v1.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.GoToCategory;
import com.jovan.erp_v1.enumeration.GoToType;
import com.jovan.erp_v1.enumeration.RoleTypes;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.GoToMapper;
import com.jovan.erp_v1.model.GoTo;
import com.jovan.erp_v1.repository.GoToRepository;
import com.jovan.erp_v1.request.GoToRequest;
import com.jovan.erp_v1.response.GoToResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoToService implements IGoToService {

	private final GoToRepository goToRepository;
	private final GoToMapper goToMapper;
	
	@Transactional
	@Override
	public GoToResponse create(GoToRequest request) {
		validateGoToRequest(request);
		GoTo gt = goToMapper.toEntity(request);
		GoTo saved = goToRepository.save(gt);
		return new GoToResponse(saved);
	}
	
	@Transactional
	@Override
	public GoToResponse update(Long id, GoToRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		GoTo gt = goToRepository.findById(id).orElseThrow(() -> new ValidationException("GoTo not found with id "+id));
		validateUpdateRequest(request);
		if (!gt.getLabel().equals(request.label()) && goToRepository.existsByLabel(request.label())) {
		    throw new ValidationException("Label already exists");
		}
		if(!gt.getIcon().equals(request.icon()) && goToRepository.existsByIcon(request.icon())) {
			throw new ValidationException("Icon already exists");
		}
		if(!gt.getPath().equals(request.path()) && goToRepository.existsByPathContainingIgnoreCase(request.path())) {
			throw new ValidationException("Path already exists");
		}
		goToMapper.toEntityUpdate(gt, request);
		GoTo saved = goToRepository.save(gt);
		return new GoToResponse(saved);
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		if(!goToRepository.existsById(id)) {
			throw new ValidationException("GoTo not found with id "+id);
		}
		goToRepository.deleteById(id);
	}
	
	@Override
	public GoToResponse findOne(Long id) {
		GoTo gt = goToRepository.findById(id).orElseThrow(() -> new ValidationException("GoTo not found with id "+id));
		return new GoToResponse(gt);
	}
	
	@Override
	public List<GoToResponse> findAll() {
		List<GoTo> items = goToRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("GoTo list is empty");
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByCategoryAndActiveTrue(GoToCategory category) {
		validateGoToCategory(category);
		List<GoTo> items = goToRepository.findByCategoryAndActiveTrue(category);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for category %s and active is true, found", category);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByLabelContainingIgnoreCase(String label) {
		validateString(label);
		List<GoTo> items = goToRepository.findByLabelContainingIgnoreCase(label);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for label %s, is found", label);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByDescriptionContainingIgnoreCase(String description) {
		validateString(description);
		List<GoTo> items = goToRepository.findByDescriptionContainingIgnoreCase(description);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for description %s, is found", description);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByRolesContainingIgnoreCase(String roles) {
		validateString(roles);
		List<GoTo> items = goToRepository.findByRolesContainingIgnoreCase(roles);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for roles %s, is found", roles);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByCategory(GoToCategory category) {
		validateGoToCategory(category);
		List<GoTo> items = goToRepository.findByCategory(category);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for category %s, is found", category);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByType(GoToType type) {
		validateGoToType(type);
		List<GoTo> items = goToRepository.findByType(type);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for type %s, is found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByCategoryAndType(GoToCategory category, GoToType type) {
		validateGoToCategory(category);
		validateGoToType(type);
		List<GoTo> items = goToRepository.findByCategoryAndType(category, type);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for category %s and type %s, is found", category,type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByPathContainingIgnoreCase(String path) {
		validateString(path);
		List<GoTo> items = goToRepository.findByPathContainingIgnoreCase(path);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for path %s, is found", path);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByIcon(String icon) {
		validateString(icon);
		List<GoTo> items = goToRepository.findByIcon(icon);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for icon %s, is found", icon);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByActiveTrue() {
		List<GoTo> items = goToRepository.findByActiveTrue();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No GoTo for active equal 'true', is found");
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByLabelContainingIgnoreCaseAndCategory(String label, GoToCategory category) {
		validateString(label);
		validateGoToCategory(category);
		List<GoTo> items = goToRepository.findByLabelContainingIgnoreCaseAndCategory(label, category);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for label %s and category %s, is found", 
					label,category);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByDescriptionContainingIgnoreCaseAndCategory(String description,
			GoToCategory category) {
		validateString(description);
		validateGoToCategory(category);
		List<GoTo> items = goToRepository.findByDescriptionContainingIgnoreCaseAndCategory(description, category);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for description %s and category %s, is found",
					description,category);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> searchByKeyword(String keyword) {
		validateString(keyword);
		List<GoTo> items = goToRepository.searchByKeyword(keyword);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for searched keyword %s, is found", keyword);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByActiveTrueOrderByLabelAsc() {
		List<GoTo> items = goToRepository.findByActiveTrue();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No GoTo for active equal true, order by label in ascening order");
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByCategoryIn(List<GoToCategory> categories) {
		validateEnumList(categories, "GoTo Category-list");
		List<GoTo> items = goToRepository.findByCategoryIn(categories);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for categories %s, is found", categories);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<GoToResponse> findByLabelContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String label,
			String description) {
		validateString(label);
		validateString(description);
		List<GoTo> items = goToRepository.findByLabelContainingIgnoreCaseAndDescriptionContainingIgnoreCase(label, description);
		if(items.isEmpty()) {
			String msg = String.format("No GoTo for label %s and description %s, is found", 
					label,description);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(goToMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public Boolean existsByLabel(String label) {
		validateString(label);
		return goToRepository.existsByLabel(label);
	}
	
	@Override
	public Boolean existsByCategory(GoToCategory category) {
		validateGoToCategory(category);
		return goToRepository.existsByCategory(category);
	}
	
	@Override
	public Boolean existsByRolesContainingIgnoreCase(String roles) {
		validateString(roles);
		return goToRepository.existsByRolesContainingIgnoreCase(roles);
	}
	
	@Override
	public Boolean existsByPathContainingIgnoreCase(String path) {
		validateString(path);
		return goToRepository.existsByPathContainingIgnoreCase(path);
	}
	
	@Override
	public Boolean existsByIcon(String icon) {
		validateString(icon);
		return goToRepository.existsByIcon(icon);
	}
	
	@Override
	public Boolean existsByLabelAndCategory(String label, GoToCategory category) {
		validateString(label);
		validateGoToCategory(category);
		return goToRepository.existsByLabelAndCategory(label, category);
	}
	
	@Override
	public Boolean existsByPath(String path) {
		validateString(path);
		return goToRepository.existsByPath(path);
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("Textual character/s must not be null nor empty");
		}
	}
	
	private void validateGoToCategory(GoToCategory category) {
		Optional.ofNullable(category)
			.orElseThrow(() -> new ValidationException("GoToCategory category must not be null"));
	}
	
	private void validateGoToType(GoToType type) {
		Optional.ofNullable(type)
			.orElseThrow(() -> new ValidationException("GoToType type must not be null"));
	}
	
	private <T extends Enum<T>> void validateEnumList(List<T> category, String enumName) {
		if (category == null || category.isEmpty()) {
	        throw new ValidationException(enumName + " list must not be null nor empty");
	    }
		for(T status : category) {
			if(status == null) {
				throw new ValidationException(enumName+" value must not be null");
			}
		}
	}
	
	private static final Set<String> VALID_ROLES = Arrays.stream(RoleTypes.values())
	        .map(Enum::name)
	        .collect(Collectors.toSet());

	private void validateRoles(String roles) {
	    Arrays.stream(roles.split(","))
	            .map(String::trim)
	            .forEach(role -> {
	                if (!VALID_ROLES.contains(role)) {
	                    throw new ValidationException("Invalid role: " + role);
	                }
	            });
	}
	
	private void validateGoToRequest(GoToRequest request) {
		if (request == null) {
	        throw new ValidationException("GoTo request must not be null");
	    }
	    validateString(request.label());
	    if (goToRepository.existsByLabel(request.label())) {
	        throw new ValidationException("Label already exists");
	    }
	    validateString(request.description());
	    validateGoToCategory(request.category());
	    validateGoToType(request.type());
	    validateString(request.path());
	    if (goToRepository.existsByPath(request.path())) {
	        throw new ValidationException("Path already exists");
	    }
	    validateString(request.icon());
	    if (goToRepository.existsByIcon(request.icon())) {
	        throw new ValidationException("Icon already exists");
	    }
	    if (request.active() == null) {
	        throw new ValidationException("Active flag must not be null");
	    }
	    validateString(request.roles());
	    validateRoles(request.roles());
	}
	
	private void validateUpdateRequest(GoToRequest request) {
		if (request == null) {
	        throw new ValidationException("GoTo request must not be null");
	    }
		validateString(request.label());
		validateString(request.description());
	    validateGoToCategory(request.category());
	    validateGoToType(request.type());
	    validateString(request.path());
	    validateString(request.icon());
	    validateString(request.roles());
	}
}
