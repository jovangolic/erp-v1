package com.jovan.erp_v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    public Driver toEntity(DriverRequest request);

    public DriverResponse toResponse(Driver driver);

    public List<DriverResponse> toResponseList(List<Driver> drivers);
}
