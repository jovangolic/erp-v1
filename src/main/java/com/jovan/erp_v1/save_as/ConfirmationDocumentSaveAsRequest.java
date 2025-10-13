package com.jovan.erp_v1.save_as;

import com.jovan.erp_v1.enumeration.ConfirmationDocumentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConfirmationDocumentSaveAsRequest(
		@NotNull Long sourceId,
		@NotBlank String filePath,
		@NotNull Long userId,
		@NotNull Long shiftId,
		@NotNull ConfirmationDocumentStatus status,
		@NotNull Boolean confirmed
		) {
}
