package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SystemSettingUpdateRequest(

		@NotNull(message = "Id must not be null") Long id,
	    @NotBlank(message = "Value must not be blank") String value,
	    String description,
	    @NotNull(message = "Category must not be null") SystemSettingCategory category,
	    @NotNull(message = "Data type must not be null") SettingDataType dataType,
	    Boolean editable,
	    Boolean isVisible,
	    String defaultValue) {
}
