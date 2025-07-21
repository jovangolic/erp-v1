package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.request.VehicleRequest;
import com.jovan.erp_v1.response.VehicleResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class VehicleMapper extends AbstractMapper<VehicleRequest> {

    public Vehicle toEntity(VehicleRequest request) {
    	Objects.requireNonNull(request, "VehicleRequest must not be null");
    	validateIdForCreate(request, VehicleRequest::id);
        Vehicle v = new Vehicle();
        v.setModel(request.model());
        v.setRegistrationNumber(request.registrationNumber());
        v.setStatus(request.status());
        return v;
    }
    
    public Vehicle toEntityUpdate(Vehicle v, VehicleRequest request) {
    	Objects.requireNonNull(request, "VehicleRequest must not be null");
    	Objects.requireNonNull(v, "Vehicle must not be null");
    	validateIdForUpdate(request, VehicleRequest::id);
    	v.setRegistrationNumber(request.registrationNumber());
    	v.setModel(request.model());
    	v.setStatus(request.status());
    	return v;
    }

    public VehicleResponse toResponse(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "Vehicle must not be null");
        return new VehicleResponse(vehicle);
    }

    public List<VehicleResponse> toResponseList(List<Vehicle> vs) {
    	if(vs == null || vs.isEmpty()) {
    		return Collections.emptyList();
    	}
        return vs.stream().map(this::toResponse).collect(Collectors.toList());
    }
}