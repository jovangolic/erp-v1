package com.jovan.erp_v1.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.mapper.RoleMapper;
import com.jovan.erp_v1.model.Permission;
import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.PermissionRepository;
import com.jovan.erp_v1.repository.RoleRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.RoleRequest;
import com.jovan.erp_v1.response.RoleResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    @Override
    public RoleResponse createRole(RoleRequest request) {
        String roleName = "ROLE_" + request.getName().toUpperCase();
        if (roleRepository.existsByName(roleName)) {
            throw new RuntimeException("Role already exists with name: " + roleName);
        }
        Set<Permission> permissions = new HashSet<>();
        if (request.getPermissionIds() != null) {
            permissions = request.getPermissionIds().stream()
                    .map(id -> permissionRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + id)))
                    .collect(Collectors.toSet());
        }
        Role role = roleMapper.toEntity(request, permissions);
        role.setName(roleName); 
        Role savedRole = roleRepository.save(role);
        return roleMapper.toResponse(savedRole);
    }

    @Transactional
    @Override
    public RoleResponse updateRole(Long roleId, RoleRequest updatedRequest) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        Set<Permission> permissions = new HashSet<>();
        if (updatedRequest.getPermissionIds() != null) {
            permissions = updatedRequest.getPermissionIds().stream()
                    .map(id -> permissionRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + id)))
                    .collect(Collectors.toSet());
        }

        roleMapper.updateRole(updatedRequest, existingRole);
        existingRole.setPermissions(permissions);
        Role updated = roleRepository.save(existingRole);
        return roleMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        role.removeAllUsersFromRole();
        roleRepository.delete(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toResponseList(roles);
    }

    @Override
    public RoleResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
        return roleMapper.toResponse(role);
    }

    @Override
    @Transactional
    public void assignUserToRole(Long roleId, Long userId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        role.assignRoleToUser(user);
        roleRepository.save(role); 
    }

}
