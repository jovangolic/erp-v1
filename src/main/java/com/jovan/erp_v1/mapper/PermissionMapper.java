package com.jovan.erp_v1.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.enumeration.PermissionType;
import com.jovan.erp_v1.model.Permission;
import com.jovan.erp_v1.request.PermissionRequest;
import com.jovan.erp_v1.response.PermissionResponse;

@Component
public class PermissionMapper {

    public Permission toEntity(PermissionRequest request) {
        Permission entity = new Permission();
        entity.setPermissionType(request.getPermissionType());
        return entity;
    }

    public PermissionResponse toResponse(Permission entity) {
    	Objects.requireNonNull(entity,"Permission must not be null");
        return new PermissionResponse(entity.getId(), entity.getPermissionType());
    }
}
