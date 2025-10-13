package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.ConfirmationDocumentStatus;

import jakarta.validation.constraints.NotNull;

public record ConfirmationDocumentRequest(
        @NotNull Long id,
        String filePath,
        @NotNull LocalDateTime createdAt,
        @NotNull Long userId,
        @NotNull Long shiftId,
        @NotNull ConfirmationDocumentStatus status,
        @NotNull Boolean confirmed) {

}
