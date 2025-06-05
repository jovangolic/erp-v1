package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;

public record DriversRequest(
        @NotBlank String name,
        @NotBlank String phone) {
}