package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.HelpCategory;

import jakarta.validation.constraints.NotBlank;

public record HelpRequest(
		Long id,
        @NotBlank String title,
        @NotBlank String content,
        HelpCategory category,
        boolean isVisible) {

}
