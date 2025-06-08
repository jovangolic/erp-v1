package com.jovan.erp_v1.service;

import com.jovan.erp_v1.request.RouteRequest;
import com.jovan.erp_v1.response.RouteResponse;
import java.util.List;

public interface IRouteService {

    RouteResponse create(RouteRequest request);

    RouteResponse update(Long id, RouteRequest request);

    void delete(Long id);

    RouteResponse findOneById(Long id);

    List<RouteResponse> findAll();

    List<RouteResponse> findByOrigin(String origin);

    List<RouteResponse> findByDestination(String destination);

    List<RouteResponse> findByOriginAndDestination(String origin, String destination);

    List<RouteResponse> findByDistanceKmGreaterThan(Double distance);

    List<RouteResponse> findByDistanceKmLessThan(Double distance);
}
