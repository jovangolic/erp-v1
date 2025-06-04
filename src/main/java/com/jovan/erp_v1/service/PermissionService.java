package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return null;
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

        permission.setPermissionType(request.getPermissionType());
        Permission updated = permissionRepository.save(permission);

        return permissionMapper.toResponse(updated);
    }

}
