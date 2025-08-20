package com.jovan.erp_v1.dto;

import com.jovan.erp_v1.enumeration.QualityCheckType;

public record CheckTypeCountResponse(QualityCheckType checkType, Long count) {

}
