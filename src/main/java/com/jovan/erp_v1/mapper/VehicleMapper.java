package com.jovan.erp_v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.request.VehicleRequest;
import com.jovan.erp_v1.response.VehicleResponse;

@Mapper(componentModel = "Spring")
public interface VehicleMapper {

    public Vehicle toEntity(VehicleRequest request);

    public VehicleResponse toResponse(Vehicle vehicle);

    public List<VehicleResponse> toResponseList(List<Vehicle> vehicles);
}
