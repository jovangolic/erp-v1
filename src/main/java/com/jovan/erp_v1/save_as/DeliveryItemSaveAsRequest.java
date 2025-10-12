package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.DeliveryItemStatus;

import jakarta.validation.constraints.NotNull;

public record DeliveryItemSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull BigDecimal quantity,
		Long productId, //opcioni
		Long inboundDeliveryId, //opcioni
		Long outboundDeliveryId, //opcioni
 		@NotNull DeliveryItemStatus status,
		@NotNull Boolean confirmed
		) {
}
