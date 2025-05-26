package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SystemSettingUpdateRequest(

        @NotNull(message = "ID is required") Long id,

        @NotBlank(message = "Value is required") String value) {
}
