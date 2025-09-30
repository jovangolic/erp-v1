package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.enumeration.TripTypeStatus;
import com.jovan.erp_v1.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {

	private Long id;
	private String startLocation;
    private String endLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TripStatus status;
    private TripTypeStatus typeStatus;
    private DriverResponse driverResponse;
    private Boolean confirmed;
    private BigDecimal fare;
	
	public TripResponse(Trip t) {
		this.id = t.getId();
		this.startLocation = t.getStartLocation();
		this.endLocation = t.getEndLocation();
		this.startTime = t.getStartTime();
		this.endTime = t.getEndTime();
		this.status = t.getStatus();
		this.typeStatus = t.getTypeStatus();
		this.driverResponse = t.getDriver() != null ? new DriverResponse(t.getDriver()) : null;
		this.confirmed = t.getConfirmed();
		this.fare = t.getFare();
	}
}
