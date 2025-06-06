package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.VehicleStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record VehicleRequest(
        Long id,
        @NotBlank String registrationNumber,
        @NotEmpty String model,
        @NotBlank VehicleStatus status) {
}
