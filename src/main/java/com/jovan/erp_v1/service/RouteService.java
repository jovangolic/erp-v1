package com.jovan.erp_v1.service;

import java.math.BigDecimal;
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
        validateString(request.origin());
        validateString(request.destination());
        validateBigDecimal(request.distanceKm());
        route.setOrigin(request.origin());
        route.setDestination(request.destination());
        route.setDistanceKm(request.distanceKm());
        Route saved = routeRepository.save(route);
        return new RouteResponse(saved);
    }

    @Transactional
    @Override
    public RouteResponse update(Long id, RouteRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found " + id));
        validateString(request.origin());
        validateString(request.destination());
        validateBigDecimal(request.distanceKm());
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
    	validateString(origin);
        return routeRepository.findByOrigin(origin).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDestination(String destination) {
    	validateString(destination);
        return routeRepository.findByDestination(destination).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByOriginAndDestination(String origin, String destination) {
    	validateString(destination);
    	validateString(origin);
        return routeRepository.findByOriginAndDestination(origin, destination).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDistanceKmGreaterThan(BigDecimal distance) {
    	validateBigDecimal(distance);
        return routeRepository.findByDistanceKmGreaterThan(distance).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDistanceKmLessThan(BigDecimal distance) {
    	validateBigDecimal(distance);
        return routeRepository.findByDistanceKmLessThan(distance).stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new IllegalArgumentException("String must not be empty nor null");
    	}
    }
    
    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new IllegalArgumentException("Number must be positive");
    	}
    }
}
