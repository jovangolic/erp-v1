package com.jovan.erp_v1.dto;

import com.jovan.erp_v1.enumeration.QualityCheckStatus;

public record StatusCountResponse(QualityCheckStatus status, Long count) {

}
