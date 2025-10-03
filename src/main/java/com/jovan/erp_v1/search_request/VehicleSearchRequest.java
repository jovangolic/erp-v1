package com.jovan.erp_v1.search_request;

import com.jovan.erp_v1.enumeration.VehicleFuel;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.enumeration.VehicleTypeStatus;

/**
 *Generic request object for general-search method
 */
public record VehicleSearchRequest(
		Long id,
        Long idFrom,
        Long idTo,
        String registrationNumber,
        String model,
        VehicleStatus status,
        VehicleFuel fuel,
        VehicleTypeStatus typeStatus,
        Boolean confirmed
		) {

}
