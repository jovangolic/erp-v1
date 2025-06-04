package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.PermissionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private Long id;
    private PermissionType permissionType;
}