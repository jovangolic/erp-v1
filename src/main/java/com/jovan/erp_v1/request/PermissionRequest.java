package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequest {

    @NotBlank
    private String permissionType; // npr. "CREATE_ORDER"
}
