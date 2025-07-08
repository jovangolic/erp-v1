package com.jovan.erp_v1.request;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotNull;

public record ShiftRequest(
		Long id,
		@NotNull(message = "Vreme početka smene je obavezno")
	    LocalDateTime startTime,

	    @NotNull(message = "Vreme završetka smene je obavezno")
	    LocalDateTime endTime,

	    @NotNull(message = "ID supervizora smene je obavezan")
	    Long shiftSupervisorId
	    //List<ConfirmationDocumentRequest> documents  -> ako bude potrebno, samo odkomentarisi
		) {
}
