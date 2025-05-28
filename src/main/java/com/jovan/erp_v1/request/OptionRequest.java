package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.OptionCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OptionRequest(
        Long id,
        @NotBlank(message = "Labela je obavezna") String label,

        @NotBlank(message = "Vrednost je obavezna") String value,

        @NotNull(message = "Kategorija je obavezna") OptionCategory category,

        boolean active) {

}
