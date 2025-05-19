package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SalesRequest(
		Long id,
		@NotNull(message = "ID kupca je obavezan")
	    Long buyerId,

	    @NotEmpty(message = "Lista prodatih stavki ne sme biti prazna")
	    @Valid
	    List<ItemSalesRequest> itemSales,

	    @NotNull(message = "Vreme prodaje je obavezno")
	    LocalDateTime createdAt,

	    @NotNull(message = "Ukupna cena je obavezna")
	    @DecimalMin(value = "0.0", inclusive = true, message = "Ukupna cena mora biti pozitivna")
	    BigDecimal totalPrice,

	    @Size(max = 255, message = "Opis prodaje ne sme biti duži od 255 karaktera")
	    String salesDescription
		) {
}
