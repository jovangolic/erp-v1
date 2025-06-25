package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.AuditActionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuditLogRequest(
                @NotNull Long userId,
                @NotBlank AuditActionType action,
                String details,
                String ipAddress,
                String userAgent) {

}
