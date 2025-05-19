package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.OrderStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SalesOrderRequest(
		Long id,
		@NotNull(message = "ID kupca je obavezan")
	    Long buyerId,

	    @NotEmpty(message = "Lista stavki je obavezna")
	    List<@Valid ItemSalesRequest> items,

	    @NotNull(message = "Datum porudžbine je obavezan")
	    LocalDateTime orderDate,

	    @NotNull(message = "Ukupan iznos je obavezan")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Ukupan iznos mora biti pozitivan")
	    BigDecimal totalAmount,

	    @Size(max = 500, message = "Napomena može imati najviše 500 karaktera")
	    String note,

	    @NotNull(message = "Status narudžbine je obavezan")
	    OrderStatus status,

	    Long invoiceId,

	    @NotBlank(message = "Broj porudžbine je obavezan")
	    String orderNumber
		) {
}
