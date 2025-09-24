package com.jovan.erp_v1.save_as;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *Genericka metoda za cuvanje jednostavnih podataka zajedno sa novim nazivom
 */
public record GenericSaveAsRequest(
		@NotNull Long sourceId,
		@NotBlank String newLabel
		) {

}