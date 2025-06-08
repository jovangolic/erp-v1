package com.jovan.erp_v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jovan.erp_v1.model.Route;
import com.jovan.erp_v1.request.RouteRequest;
import com.jovan.erp_v1.response.RouteResponse;

@Mapper(componentModel = "Spring")
public interface RouteMapper {

    public Route toEntity(RouteRequest request);

    public RouteResponse toResponse(Route route);

    public List<RouteResponse> toResponseList(List<Route> routes);
}
