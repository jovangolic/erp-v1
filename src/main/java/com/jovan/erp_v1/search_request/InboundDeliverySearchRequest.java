package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.InboundDeliveryStatus;

public record InboundDeliverySearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		LocalDate deliveryDate,
		LocalDate deliveryDateBefore,
		LocalDate deliveryDateAfter,
		LocalDate deliveryDateStart,
		LocalDate deliveryDateEnd,
		Long supplyId,
		Long supplyIdFrom,
		Long supplyIdTo,
		Long storageId,
		Long storageIdFrom,
		Long storageIdTo,
		BigDecimal quantity,
		BigDecimal quantityMin,
		BigDecimal quantityMax,
		LocalDateTime updates,
		LocalDateTime updatesBefore,
		LocalDateTime updatesAfter,
		LocalDateTime updatesStart,
		LocalDateTime updatesEnd,
		DeliveryStatus status,
		Boolean confirmed,
		InboundDeliveryStatus inboundStatus
		) {
}
