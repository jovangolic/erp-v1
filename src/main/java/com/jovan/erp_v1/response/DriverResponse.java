package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.Driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {

    private Long id;
    private String name;
    private String phone;

    public DriverResponse(Driver dr) {
        this.id = dr.getId();
        this.name = dr.getName();
        this.phone = dr.getPhone();
    }
}
