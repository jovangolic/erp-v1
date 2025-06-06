package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.model.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {

    private Long id;
    private String registrationNumber;
    private String model;
    private VehicleStatus status;

    public VehicleResponse(Vehicle ve) {
        this.id = ve.getId();
        this.registrationNumber = ve.getRegistrationNumber();
        this.model = ve.getModel();
        this.status = ve.getStatus();
    }
}
