package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.SettingDataType;

import jakarta.validation.constraints.NotBlank;

public record SystemSettingCreateRequest(

                @NotBlank String settingKey,
                @NotBlank String value,
                String description,
                String category,
                SettingDataType dataType,
                Boolean editable,
                Boolean isVisible,
                String defaultValue) {
}
