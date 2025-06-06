package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.request.VehicleRequest;
import com.jovan.erp_v1.response.VehicleResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleMapper {

    public Vehicle toEntity(VehicleRequest request) {
        Vehicle v = new Vehicle();
        v.setModel(request.model());
        v.setRegistrationNumber(request.registrationNumber());
        v.setStatus(request.status());
        return v;
    }

    public VehicleResponse toResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setModel(vehicle.getModel());
        response.setRegistrationNumber(vehicle.getRegistrationNumber());
        response.setStatus(vehicle.getStatus());
        return response;
    }

    public List<VehicleResponse> toResponseList(List<Vehicle> vs) {
        return vs.stream().map(this::toResponse).collect(Collectors.toList());
    }
}