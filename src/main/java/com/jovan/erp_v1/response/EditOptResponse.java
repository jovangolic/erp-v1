package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.EditOptType;

public record EditOptResponse(

        Long id,
        String name,
        String value,
        EditOptType type,
        boolean editable,
        boolean visible) {

}
