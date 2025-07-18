package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public record SystemSettingCreateRequest(
		@Null(message = "Id must be null when creating a setting") Long id,
                @NotBlank String settingKey,
                @NotBlank String value,
                String description,
                SystemSettingCategory category,
                SettingDataType dataType,
                Boolean editable,
                Boolean isVisible,
                String defaultValue) {
}
