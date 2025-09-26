package com.jovan.erp_v1.save_as;

import jakarta.validation.constraints.NotNull;

/**
 *Light way version of save_as method
 */
public record DefectSaveAsRequest(
		@NotNull Long sourceId,
	    String code,
	    String name,
	    String description
		) {
}
