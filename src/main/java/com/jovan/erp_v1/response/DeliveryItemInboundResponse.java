package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.DeliveryItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryItemInboundResponse {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal quantity;
    private Long inboundDeliveryId;

    public DeliveryItemInboundResponse(DeliveryItem item) {
        this.id = item.getId();
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.quantity = item.getQuantity();
        this.inboundDeliveryId = item.getInboundDelivery().getId();

    }
}
