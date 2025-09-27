package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.model.Driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private DriverStatus status;
    private Boolean confirmed;

    public DriverResponse(Driver dr) {
        this.id = dr.getId();
        this.firstName = dr.getFirstName();
        this.lastName = dr.getLastName();
        this.phone = dr.getPhone();
        this.status = dr.getStatus();
        this.confirmed = dr.getConfirmed();
    }
}
