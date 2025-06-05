package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record VehicleRequest(
        @NotBlank String registrationNumber,
        @NotEmpty String model) {
}
