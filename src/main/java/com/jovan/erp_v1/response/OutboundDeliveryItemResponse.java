package com.jovan.erp_v1.response;

import java.time.LocalDate;
import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.model.OutboundDelivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboundDeliveryItemResponse {

	private Long id;
    private LocalDate deliveryDate;
    private Long buyerId;
    private DeliveryStatus status;
    
    public OutboundDeliveryItemResponse(OutboundDelivery delivery) {
        this.id = delivery.getId();
        this.deliveryDate = delivery.getDeliveryDate();
        this.buyerId = delivery.getBuyer().getId();
        this.status = delivery.getStatus();
    }
}
