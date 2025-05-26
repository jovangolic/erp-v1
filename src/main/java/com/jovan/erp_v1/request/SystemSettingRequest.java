package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;

public record SystemSettingRequest(
                Long id,
                @NotBlank(message = "Key is required") String key,
                @NotBlank(message = "Value is required") String value) {

}
