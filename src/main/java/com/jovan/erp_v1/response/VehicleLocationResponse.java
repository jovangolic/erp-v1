package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.jovan.erp_v1.model.VehicleLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLocationResponse {

	private Long id;
	private VehicleResponse vehicleResponse;
	private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime recordedAt;
	
	public VehicleLocationResponse(VehicleLocation vl) {
		this.id  = vl.getId();
		this.vehicleResponse = vl.getVehicle() != null ? new VehicleResponse(vl.getVehicle()) : null;
		this.latitude = vl.getLatitude();
		this.longitude = vl.getLongitude();
		this.recordedAt = vl.getRecordedAt();
	}
}
