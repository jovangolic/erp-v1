package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.RoleTypes;

import jakarta.validation.constraints.NotBlank;

public record EmployeeEmailDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank RoleTypes types) {
}