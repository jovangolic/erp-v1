package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.RoleTypes;

import jakarta.validation.constraints.NotBlank;

public record CompanyEmailResponse(
        String email,
        String firstName,
        String lastName,
        RoleTypes role,
        LocalDateTime createdAt,
        String password) {
}
