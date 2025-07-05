package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PaymentRequest(
		Long id,
		@NotNull(message = "Iznos uplate je obavezan")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Iznos mora biti pozitivan")
	    BigDecimal amount,

	    @NotNull(message = "Datum uplate je obavezan")
	    LocalDateTime paymentDate,

	    @NotNull(message = "Način plaćanja je obavezan")
	    PaymentMethod method,

	    @NotNull(message = "Status uplate je obavezan")
	    PaymentStatus status,

	    @NotBlank(message = "Referentni broj je obavezan")
	    @Size(max = 100, message = "Referentni broj može imati najviše 100 karaktera")
	    String referenceNumber,

	    @NotNull(message = "ID kupca je obavezan")
	    Long buyerId,

	    @NotNull(message = "ID povezane prodaje je obavezan")
	    Long relatedSalesId
		) {
}
