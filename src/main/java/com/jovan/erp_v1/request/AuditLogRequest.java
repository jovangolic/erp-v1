package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuditLogRequest(
        @NotNull Long userId,
        @NotBlank String action,
        String details) {

}
