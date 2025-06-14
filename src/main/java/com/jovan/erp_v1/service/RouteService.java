package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.RouteNotFoundException;
import com.jovan.erp_v1.model.Route;
import com.jovan.erp_v1.repository.RouteRepository;
import com.jovan.erp_v1.request.RouteRequest;
import com.jovan.erp_v1.response.RouteResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService implements IRouteService {

    private final RouteRepository routeRepository;

    @Transactional
    @Override
    public RouteResponse create(RouteRequest request) {
        Route route = new Route();
        route.setOrigin(request.origin());
        route.setDestination(request.destination());
        route.setDistanceKm(request.distanceKm());
        Route saved = routeRepository.save(route);
        return new RouteResponse(saved);
    }

    @Transactional
    @Override
    public RouteResponse update(Long id, RouteRequest request) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found " + id));
        route.setOrigin(request.origin());
        route.setDestination(request.destination());
        route.setDistanceKm(request.distanceKm());
        Route saved = routeRepository.save(route);
        return new RouteResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new RouteNotFoundException("Route not found " + id);
        }
        routeRepository.deleteById(id);
    }

    @Override
    public RouteResponse findOneById(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found " + id));
        return new RouteResponse(route);
    }

    @Override
    public List<RouteResponse> findAll() {
        return routeRepository.findAll().stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByOrigin(String origin) {
        return routeRepository.findByOrigin(origin).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDestination(String destination) {
        return routeRepository.findByDestination(destination).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByOriginAndDestination(String origin, String destination) {
        return routeRepository.findByOriginAndDestination(origin, destination).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDistanceKmGreaterThan(Double distance) {
        return routeRepository.findByDistanceKmGreaterThan(distance).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDistanceKmLessThan(Double distance) {
        return routeRepository.findByDistanceKmLessThan(distance).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }
}
