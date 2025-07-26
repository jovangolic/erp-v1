package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.EditOptType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditOptRequest(
        Long id,
        @NotBlank(message = "Naziv opcije je obavezan") String name,
        @NotBlank(message = "Vrednost opcije je obavezna") String value,
        @NotNull(message = "Tip opcije je obavezan") EditOptType type,
        boolean editable,
        boolean visible) {

}
