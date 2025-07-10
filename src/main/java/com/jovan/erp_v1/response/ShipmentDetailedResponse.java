package com.jovan.erp_v1.response;

import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.EventLog;
import com.jovan.erp_v1.model.Shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDetailedResponse extends ShipmentResponse {

    private String internalNotes;
    private List<EventLogResponse> eventLogs;

    public ShipmentDetailedResponse(Shipment shipment, String internalNotes, List<EventLog> logs) {
        super(shipment); // koristi konstruktor iz ShipmentResponse
        this.internalNotes = internalNotes;
        this.eventLogs = logs.stream()
                .map(EventLogResponse::new)
                .collect(Collectors.toList());
    }
}
