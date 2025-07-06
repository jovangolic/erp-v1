package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.PermissionType;
import com.jovan.erp_v1.request.PermissionRequest;
import com.jovan.erp_v1.response.PermissionResponse;

public interface IPermissionService {

    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAll();

    PermissionResponse getById(Long id);

    void delete(Long id);

    PermissionResponse update(Long id, PermissionRequest request);
    
    PermissionResponse findByPermissionType(PermissionType type);
}
