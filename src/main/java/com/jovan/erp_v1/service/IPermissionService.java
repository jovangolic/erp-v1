package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.PermissionActionType;
import com.jovan.erp_v1.enumeration.PermissionResourceType;
import com.jovan.erp_v1.request.PermissionRequest;
import com.jovan.erp_v1.response.PermissionResponse;

public interface IPermissionService {

    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
    PermissionResponse getById(Long id);
    void delete(Long id);
    PermissionResponse update(Long id, PermissionRequest request);
    List<PermissionResponse> findByActionType(PermissionActionType actionType);
    List<PermissionResponse> findByResourceType(PermissionResourceType resourceType);
    List<PermissionResponse> findByActionTypeAndResourceType(PermissionActionType actionType, PermissionResourceType resourceType);

    boolean existsByActionType(PermissionActionType actionType);
    boolean existsByResourceType(PermissionResourceType resourceType);
    
    PermissionResponse savePermission(PermissionRequest request);
}
