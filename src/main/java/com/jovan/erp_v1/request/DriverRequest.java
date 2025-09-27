package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.DriverStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DriverRequest(
        Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phone,
        @NotNull DriverStatus status,
        Boolean confirmed) {
}