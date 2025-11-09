package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.ItemSalesStatus;

import jakarta.validation.constraints.NotNull;

public record ItemSalesSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull Long goodsId,
		@NotNull Long salesId,
		@NotNull Long procurementId,
		@NotNull Long salesOrderId,
		@NotNull BigDecimal quantity,
		@NotNull BigDecimal unitPrice,
		@NotNull ItemSalesStatus status,
		@NotNull Boolean confirmed
		) {
}
