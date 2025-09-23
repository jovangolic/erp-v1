package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.PermissionActionType;
import com.jovan.erp_v1.enumeration.PermissionResourceType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.PermissionErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.PermissionMapper;
import com.jovan.erp_v1.model.Permission;
import com.jovan.erp_v1.repository.PermissionRepository;
import com.jovan.erp_v1.request.PermissionRequest;
import com.jovan.erp_v1.response.PermissionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(PermissionRequest request) {
    	validatePermissionActionType(request.getActionType());
    	validatePermissionResourceType(request.getResourceType());
        Permission permission = permissionMapper.toEntity(request);
        permissionRepository.save(permission);
        return permissionMapper.toResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {
    	List<Permission> items = permissionRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Permission list is empty");
    	}
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionResponse getById(Long id) {
    	Permission p = permissionRepository.findById(id).orElseThrow(() -> new PermissionErrorException("PermissionType id not found wuth type "+id));
        return new PermissionResponse(p);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new PermissionErrorException("Permission not found " + id);
        }
        permissionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PermissionResponse update(Long id, PermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission with ID " + id + " not found"));
        validatePermissionActionType(request.getActionType());
    	validatePermissionResourceType(request.getResourceType());
        permission.setActionType(request.getActionType());
        permission.setResourceType(request.getResourceType());
        Permission updated = permissionRepository.save(permission);
        return permissionMapper.toResponse(updated);
    }


	@Override
	public List<PermissionResponse> findByActionType(PermissionActionType actionType) {
		validatePermissionActionType(actionType);
		List<Permission> items = permissionRepository.findByActionType(actionType);
		if(items.isEmpty()) {
			String msg = String.format("No Permission for action-type %s, is found", actionType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(permissionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<PermissionResponse> findByResourceType(PermissionResourceType resourceType) {
		validatePermissionResourceType(resourceType);
		List<Permission> items = permissionRepository.findByResourceType(resourceType);
		if(items.isEmpty()) {
			String msg = String.format("No Permission for resource-type %s, is found", resourceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(permissionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<PermissionResponse> findByActionTypeAndResourceType(PermissionActionType actionType,
			PermissionResourceType resourceType) {
		validatePermissionActionType(actionType);
		validatePermissionResourceType(resourceType);
		List<Permission> items = permissionRepository.findByActionTypeAndResourceType(actionType, resourceType);
		if(items.isEmpty()) {
			String msg = String.format("No Permission for action-type %s and resource-type %, is found", actionType,resourceType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(permissionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public boolean existsByActionType(PermissionActionType actionType) {
		validatePermissionActionType(actionType);
		return permissionRepository.existsByActionType(actionType);
	}

	@Override
	public boolean existsByResourceType(PermissionResourceType resourceType) {
		validatePermissionResourceType(resourceType);
		return permissionRepository.existsByResourceType(resourceType);
	}
	
	@Override
	public PermissionResponse savePermission(PermissionRequest request) {
		Permission p = Permission.builder()
				.id(request.getId())
				.actionType(request.getActionType())
				.resourceType(request.getResourceType())
				.build();
		Permission saved = permissionRepository.save(p);
		return new PermissionResponse(saved);
	}
	
	private void validatePermissionActionType(PermissionActionType actionType) {
		Optional.ofNullable(actionType)
			.orElseThrow(() -> new ValidationException("PermissionActionType actionType must not be null"));
	}

	private void validatePermissionResourceType(PermissionResourceType resourceType) {
		Optional.ofNullable(resourceType)
			.orElseThrow(() -> new ValidationException("PermissionResourceType resourceType must not be null"));
	}

}
