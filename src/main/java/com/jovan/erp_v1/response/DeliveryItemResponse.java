package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.DeliveryItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Double quantity;
    private Long inboundDeliveryId;
    private Long outboundDeliveryId;

    public DeliveryItemResponse(DeliveryItem item) {
        this.id = item.getId();
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.quantity = item.getQuantity();
        this.inboundDeliveryId = item.getInboundDelivery().getId();
        this.outboundDeliveryId = item.getOutboundDelivery().getId();
    }
}
