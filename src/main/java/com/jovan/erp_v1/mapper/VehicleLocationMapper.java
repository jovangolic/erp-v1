package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.model.VehicleLocation;
import com.jovan.erp_v1.request.VehicleLocationRequest;
import com.jovan.erp_v1.response.VehicleLocationResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class VehicleLocationMapper extends AbstractMapper<VehicleLocationRequest> {

	
	public VehicleLocation toEntity(VehicleLocationRequest request, Vehicle vehicle) {
		Objects.requireNonNull(request, "VehicleLocationRequest must not be null");
		Objects.requireNonNull(vehicle, "Vehicle must not be null");
		validateIdForCreate(request, VehicleLocationRequest::id);
		VehicleLocation vl = new VehicleLocation();
		vl.setId(request.id());
		vl.setVehicle(vehicle);
		vl.setLongitude(request.longitude());
		vl.setLatitude(request.latitude());
		return vl;
	}
	
	public VehicleLocation toEntityUpdate(VehicleLocationRequest request, VehicleLocation vl, Vehicle vehicle) {
		Objects.requireNonNull(request, "VehicleLocationRequest must not be null");
		Objects.requireNonNull(vl, "VehicleLocation must not be null");
		Objects.requireNonNull(vehicle, "Vehicle must not be null");
		validateIdForUpdate(request, VehicleLocationRequest::id);
		vl.setVehicle(vehicle);
		vl.setLongitude(request.longitude());
		vl.setLatitude(request.latitude());
		return vl;
	}
	
	public VehicleLocationResponse toResponse(VehicleLocation vl) {
		Objects.requireNonNull(vl, "VehicleLocation must not be null");
		return new VehicleLocationResponse(vl);
	}
	
	public List<VehicleLocationResponse> toResponseList(List<VehicleLocation> vl){
		if(vl == null || vl.isEmpty()) {
			return Collections.emptyList();
		}
		return vl.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
