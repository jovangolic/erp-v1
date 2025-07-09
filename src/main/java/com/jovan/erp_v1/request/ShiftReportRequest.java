package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record ShiftReportRequest(
		Long id,
		@NotBlank(message = "Opis je obavezan") String description,
		@NotNull(message = "Datum kreiranja je obavezan")
		@PastOrPresent(message = "Datum kreiranja ne može biti u budućnosti")
		LocalDateTime createdAt,

		@NotNull(message = "ID osobe koja kreira izveštaj je obavezan") Long createdById,

		@NotNull(message = "ID smene na koju se izveštaj odnosi je obavezan") Long relatedShiftId,

		@Size(max = 255, message = "Putanja fajla ne sme biti duža od 255 karaktera") String filePath// opcionalno ako
																										// odmah
																										// upisuješ
																										// putanju
) {
}
