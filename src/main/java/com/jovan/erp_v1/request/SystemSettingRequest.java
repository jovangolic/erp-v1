package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SystemSettingRequest(
        Long id,
        @NotBlank(message = "Key is required")
        String settingKey,
        @NotBlank(message = "Value is required")
        String value,
        String description,
        SystemSettingCategory category,
        @NotNull(message = "Data type is required")
        SettingDataType dataType,
        Boolean editable,
        Boolean isVisible,
        String defaultValue) {
}
