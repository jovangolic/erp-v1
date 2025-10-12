package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.DeliveryItemStatus;
import com.jovan.erp_v1.model.DeliveryItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryItemResponse {

    private Long id;
    private DeliveryItemProductResponse deliveryItemProductResponse;
    private BigDecimal quantity;
    private InboundDeliveryItemResponse inboundDeliveryItemResponse;
    private OutboundDeliveryItemResponse outboundDeliveryItemResponse;
    private Boolean confirmed;
    private DeliveryItemStatus status;

    public DeliveryItemResponse(DeliveryItem item) {
        this.id = item.getId();
        this.deliveryItemProductResponse = item.getProduct() != null ? new DeliveryItemProductResponse(item.getProduct()) : null;
        this.quantity = item.getQuantity();
        this.inboundDeliveryItemResponse = item.getInboundDelivery() != null ? new InboundDeliveryItemResponse(item.getInboundDelivery()) : null;
        this.outboundDeliveryItemResponse = item.getOutboundDelivery() != null ? new OutboundDeliveryItemResponse(item.getOutboundDelivery()) : null;
        this.confirmed = item.getConfirmed();
        this.status = item.getStatus();
    }
}
