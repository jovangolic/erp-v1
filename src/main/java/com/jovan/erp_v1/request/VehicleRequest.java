package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.VehicleFuel;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.enumeration.VehicleTypeStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record VehicleRequest(
        Long id,
        @NotBlank String registrationNumber,
        @NotEmpty String model,
        @NotBlank VehicleStatus status,
        @NotBlank VehicleFuel fuel,
        VehicleTypeStatus typeStatus, //opciono
        Boolean confirmed ) {		  // opciono
}
