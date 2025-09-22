package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;

public record SecuritySettingRequest(
        Long id,
        @NotBlank String settingName,
        @NotBlank String value) {

}
