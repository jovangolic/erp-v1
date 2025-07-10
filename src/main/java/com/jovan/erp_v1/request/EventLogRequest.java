package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventLogRequest(
		Long id,
		@NotNull LocalDateTime timestamp,
		@NotBlank String description,
		@NotNull Long shipmentId
		) {
}
