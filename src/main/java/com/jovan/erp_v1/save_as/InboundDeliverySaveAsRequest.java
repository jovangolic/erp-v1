package com.jovan.erp_v1.save_as;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.InboundDeliveryStatus;

import jakarta.validation.constraints.NotNull;

public record InboundDeliverySaveAsRequest(
		@NotNull Long sourceId,
		@NotNull Long supplyId,
		@NotNull DeliveryStatus status,
		@NotNull Boolean confirmed,
		@NotNull InboundDeliveryStatus inboundStatus
		) {
}
