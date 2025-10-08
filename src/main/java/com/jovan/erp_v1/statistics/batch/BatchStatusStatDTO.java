package com.jovan.erp_v1.statistics.batch;

import com.jovan.erp_v1.enumeration.BatchStatus;

public record BatchStatusStatDTO(
		BatchStatus status,
		Long count
		) {
}
