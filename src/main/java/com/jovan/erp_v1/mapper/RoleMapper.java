package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Permission;
import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.request.RoleRequest;
import com.jovan.erp_v1.response.PermissionResponse;
import com.jovan.erp_v1.response.RoleResponse;
import com.jovan.erp_v1.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleMapper {

        public Role toEntity(RoleRequest request, Set<Permission> permissions) {
                return Role.builder()
                                .name(request.getName())
                                .roleTypes(request.getRoleTypes())
                                .permissions(permissions)
                                .users(new HashSet<>()) // ili ne postavljaj odmah korisnike
                                .build();
        }

        public RoleResponse toResponse(Role role) {
                Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                                .map(p -> new PermissionResponse(p.getId(), p.getResourceType(),p.getActionType()))
                                .collect(Collectors.toSet());
                List<UserResponse> userResponses = role.getUsers().stream()
                                .map(UserResponse::new) // koristim postojeci konstruktor
                                .collect(Collectors.toList());
                return RoleResponse.builder()
                                .id(role.getId())
                                .name(role.getName())
                                .type(role.getRoleTypes())
                                .permissions(permissionResponses)
                                .users(userResponses)
                                .build();
        }

        public void updateRole(RoleRequest request, Role role) {
                if (request.getName() != null) {
                        role.setName(request.getName());
                }
                if (request.getRoleTypes() != null) {
                        role.setRoleTypes(request.getRoleTypes());
                }
                if (request.getUsers() != null) {
                        role.setUsers(request.getUsers());
                }
        }

        /*
         * public void updateRole(RoleRequest request, Role role) {
         * if (request.getName() != null) {
         * role.setName("ROLE_" + request.getName().toUpperCase());
         * }
         * if (request.getRoleTypes() != null) {
         * role.setRoleTypes(request.getRoleTypes());
         * }
         * if (request.getUsers() != null) {
         * role.setUsers(request.getUsers());
         * }
         * // permissions se setuju u servisu!
         * }
         */

        public List<RoleResponse> toResponseList(List<Role> roles) {
        	if(roles == null || roles.isEmpty()) {
        		return Collections.emptyList();
        	}
                return roles.stream().map(this::toResponse)
                                .collect(Collectors.toList());
        }
}
