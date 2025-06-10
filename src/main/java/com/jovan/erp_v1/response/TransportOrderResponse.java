package com.jovan.erp_v1.response;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.model.TransportOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportOrderResponse {

    private Long id;
    private LocalDate scheduledDate;
    private VehicleResponse vehicleResponse;
    private DriverResponse driverResponse;
    private TransportStatus status;
    private OutboundDeliveryResponse outboundResponse;

    public TransportOrderResponse(TransportOrder to) {
        this.id = to.getId();
        this.scheduledDate = to.getScheduledDate();
        this.vehicleResponse = new VehicleResponse(to.getVehicle());
        this.driverResponse = new DriverResponse(to.getDriver());
        this.status = to.getStatus();
        this.outboundResponse = new OutboundDeliveryResponse(to.getOutboundDelivery());
    }
}
