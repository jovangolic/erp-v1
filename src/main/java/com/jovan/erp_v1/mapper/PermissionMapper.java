package com.jovan.erp_v1.mapper;

import java.util.Objects;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Permission;
import com.jovan.erp_v1.request.PermissionRequest;
import com.jovan.erp_v1.response.PermissionResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class PermissionMapper extends AbstractMapper<PermissionRequest> {

    public Permission toEntity(PermissionRequest request) {
    	Objects.requireNonNull(request,"PermissionRequest must not be null");
        Permission entity = new Permission();
        entity.setId(request.getId());
        entity.setPermissionType(request.getPermissionType());
        return entity;
    }

    public PermissionResponse toResponse(Permission entity) {
    	Objects.requireNonNull(entity,"Permission must not be null");
        return new PermissionResponse(entity.getId(), entity.getPermissionType());
    }
}
