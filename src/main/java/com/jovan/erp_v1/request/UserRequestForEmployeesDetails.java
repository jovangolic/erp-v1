package com.jovan.erp_v1.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record UserRequestForEmployeesDetails(
        @NotBlank String phoneNumber,
        @NotBlank String address,
        @NotBlank Set<Long> roleIds) {
}
