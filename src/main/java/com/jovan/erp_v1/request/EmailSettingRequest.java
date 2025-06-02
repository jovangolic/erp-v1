package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;

public record EmailSettingRequest(
		@NotBlank String smtpServer,
	    @NotBlank String smtpPort,
	    @NotBlank String fromEmail,
	    @NotBlank String fromName,
	    @NotBlank String emailPassword
		) {
}
