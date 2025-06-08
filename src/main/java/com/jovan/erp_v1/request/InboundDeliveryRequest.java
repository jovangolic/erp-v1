package com.jovan.erp_v1.request;

import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.DeliveryStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InboundDeliveryRequest(
                Long id,
                @NotNull LocalDate deliveryDate,
                @NotNull Long supplyId,
                @NotNull DeliveryStatus status,
                @NotEmpty @Valid List<DeliveryItemInboundRequest> itemRequest) {
}
