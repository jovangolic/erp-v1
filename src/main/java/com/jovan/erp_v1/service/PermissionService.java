package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.PermissionType;
import com.jovan.erp_v1.exception.PermissionErrorException;
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
    	validatePermisionType(request.getPermissionType());
        Permission permission = permissionMapper.toEntity(request);
        permissionRepository.save(permission);
        return permissionMapper.toResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {
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
        validatePermisionType(request.getPermissionType());
        permission.setPermissionType(request.getPermissionType());
        Permission updated = permissionRepository.save(permission);

        return permissionMapper.toResponse(updated);
    }

	@Override
	public PermissionResponse findByPermissionType(PermissionType type) {
		Permission p = validatePermisionType(type);
		return new PermissionResponse(p);
	}
	
	private Permission validatePermisionType(PermissionType type) {
		if(type == null) {
			throw new IllegalArgumentException("Type must not be null");
		}
		return permissionRepository.findByPermissionType(type).orElseThrow(() -> new PermissionErrorException("PermissionType type not found wuth type "+type));
	}

}
