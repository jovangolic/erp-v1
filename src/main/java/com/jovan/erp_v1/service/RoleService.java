package com.jovan.erp_v1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.mapper.RoleMapper;
import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.User;
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

    @Transactional
    @Override
    public RoleResponse createRole(RoleRequest request) {
        String roleName = "ROLE_" + request.getName().toUpperCase();
        if (roleRepository.existsByName(roleName)) {
            throw new RuntimeException("Role already exists with name: " + roleName);
        }
        Role role = new Role(roleName);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toResponse(savedRole);
    }

    @Transactional
    @Override
    public RoleResponse updateRole(Long roleId, RoleRequest updatedRequest) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        roleMapper.updateRole(updatedRequest, existingRole);
        Role updated = roleRepository.save(existingRole);
        return roleMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        role.removeAllUsersFromRole(); // očisti relaciju pre brisanja
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
        roleRepository.save(role); // opcionalno jer @Transactional može odraditi flush automatski
    }

}
