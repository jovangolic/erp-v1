package com.jovan.erp_v1.request;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ShelfRequest(
		Long id,
		@NotNull @Min(1) Integer rowCount,
	    @NotNull @Min(1) Integer cols,
	    @NotNull Long storageId,
	    List<Long> goods
		) {
}
