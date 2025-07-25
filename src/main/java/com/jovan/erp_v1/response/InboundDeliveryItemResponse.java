package com.jovan.erp_v1.response;

import java.time.LocalDate;
import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.model.InboundDelivery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboundDeliveryItemResponse {

	private Long id;
    private LocalDate deliveryDate;
    private Long supplyId;
    private DeliveryStatus status;
    
    public InboundDeliveryItemResponse(InboundDelivery inbound) {
        this.id = inbound.getId();
        this.deliveryDate = inbound.getDeliveryDate();
        this.supplyId = inbound.getSupply().getId();
        this.status = inbound.getStatus();
    }
}
