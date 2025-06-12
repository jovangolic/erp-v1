package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.RoleTypes;

public record CompanyEmailResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        RoleTypes role) {

}
