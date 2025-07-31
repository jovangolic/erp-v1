package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.PermissionType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequest {

	private Long id;
    @NotBlank
    private PermissionType permissionType; // npr. "CREATE_ORDER"
}
