package com.jovan.erp_v1.response;


import com.jovan.erp_v1.enumeration.PermissionActionType;
import com.jovan.erp_v1.enumeration.PermissionResourceType;
import com.jovan.erp_v1.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private Long id;
    private PermissionResourceType resourceType;
    private PermissionActionType actionType;
    
    public PermissionResponse(Permission p) {
    	this.id = p.getId();
    	this.resourceType = p.getResourceType();
    	this.actionType = p.getActionType();
    }
}