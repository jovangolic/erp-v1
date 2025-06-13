package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.RoleTypes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanyEmailDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String address,
        @NotBlank String phoneNumber,
        @NotNull RoleTypes types) {
}