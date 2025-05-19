package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.RoleRequest;
import com.jovan.erp_v1.response.RoleResponse;

public interface IRoleService {

	RoleResponse createRole(RoleRequest request);
    RoleResponse updateRole(Long roleId, RoleRequest updatedRole);
    void deleteRole(Long roleId);
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(Long id);
    void assignUserToRole(Long roleId, Long userId);
}
