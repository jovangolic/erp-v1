package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.PermissionType;
import com.jovan.erp_v1.model.Permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private Long id;
    private PermissionType permissionType;
    
    public PermissionResponse(Permission p) {
    	this.id = p.getId();
    	this.permissionType = p.getPermissionType();
    }
}