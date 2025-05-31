package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.SystemStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SystemStateRequest(
        @NotNull Boolean maintenanceMode,
        @NotNull Boolean registrationEnabled,
        @NotBlank @Size(max = 50) String systemVersion,
        @NotNull SystemStatus statusMessage) {

}
