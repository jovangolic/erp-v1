package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.enumeration.TripTypeStatus;

/**
 *Generic request object for general-search method
 */
public record TripSearchRequest(
		Long id,
        Long idFrom,
        Long idTo,
        String startLocation,
        String endLocation,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime startTimeAfter,
        LocalDateTime startTimeBefore,
        LocalDateTime endTimeAfter,
        LocalDateTime endTimeBefore,
        TripStatus tripStatus,
        TripTypeStatus typeStatus,
        Long driverId,
        Long driverIdFrom,
        Long driverIdTo,
        String firstName,
        String lastName,
        String phone,
        DriverStatus driverStatus,
        Boolean confirmed
		) {

}
