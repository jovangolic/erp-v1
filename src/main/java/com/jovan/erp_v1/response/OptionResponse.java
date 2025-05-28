package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.OptionCategory;

public record OptionResponse(
        Long id,
        String label,
        String value,
        OptionCategory category,
        boolean active) {

}
