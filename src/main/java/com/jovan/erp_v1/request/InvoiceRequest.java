package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.InvoiceTypeStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InvoiceRequest(
		
		Long id,
	    @NotNull(message = "Datum izdavanja je obavezan")
	    LocalDateTime issueDate,
	    @NotNull(message = "Datum dospeća je obavezan")
	    LocalDateTime dueDate,
	    @NotNull(message = "Status fakture je obavezan")
	    InvoiceStatus status,
	    @NotNull(message = "Ukupan iznos je obavezan")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Ukupan iznos mora biti pozitivan")
	    BigDecimal totalAmount,
	    @NotNull(message = "ID kupca je obavezan")
	    Long buyerId,
	    @NotNull(message = "ID prodaje je obavezan")
	    Long salesId,
	    @NotNull(message = "ID plaćanja je obavezan")
	    Long paymentId,
	    @Size(max = 500, message = "Napomena može imati najviše 500 karaktera")
	    String note,
	    Long salesOrderId,
	    @NotNull(message = "ID osobe koja je kreirala fakturu je obavezan")
	    Long createdById,
	    @NotNull InvoiceTypeStatus typeStatus,
	    @NotNull Boolean confirmed
		) {
}
