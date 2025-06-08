package com.jovan.erp_v1.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.model.OutboundDelivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboundDeliveryResponse {

    private Long id;
    private LocalDate deliveryDate;
    private Long buyerId;
    private DeliveryStatus status;
    private List<DeliveryItemResponse> items;

    public OutboundDeliveryResponse(OutboundDelivery delivery) {
        this.id = delivery.getId();
        this.deliveryDate = delivery.getDeliveryDate();
        this.buyerId = delivery.getBuyer().getId();
        this.status = delivery.getStatus();
        this.items = delivery.getItems().stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }
}
