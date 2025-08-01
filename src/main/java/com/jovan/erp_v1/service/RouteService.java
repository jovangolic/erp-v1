package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.RouteNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.RouteMapper;
import com.jovan.erp_v1.model.Route;
import com.jovan.erp_v1.repository.RouteRepository;
import com.jovan.erp_v1.request.RouteRequest;
import com.jovan.erp_v1.response.RouteResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService implements IRouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Transactional
    @Override
    public RouteResponse create(RouteRequest request) {
        validateString(request.origin());
        validateString(request.destination());
        validateBigDecimal(request.distanceKm());
        Route route = routeMapper.toEntity(request);
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
    	List<Route> items = routeRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Route list is empty");
    	}
        return items.stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByOrigin(String origin) {
    	validateString(origin);
    	List<Route> items = routeRepository.findByOrigin(origin);
    	if(items.isEmpty()) {
    		String msg = String.format("No Route for origin %s is found", origin);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDestination(String destination) {
    	validateString(destination);
    	List<Route> items = routeRepository.findByDestination(destination);
    	if(items.isEmpty()) {
    		String msg = String.format("No Route for destination %s is found", destination);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByOriginAndDestination(String origin, String destination) {
    	validateString(destination);
    	validateString(origin);
    	List<Route> items = routeRepository.findByOriginAndDestination(origin, destination);
    	if(items.isEmpty()) {
    		String msg = String.format("No Route for origin %s and destination %s is found", origin,destination);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDistanceKmGreaterThan(BigDecimal distance) {
    	validateBigDecimal(distance);
    	List<Route> items = routeRepository.findByDistanceKmGreaterThan(distance);
    	if(items.isEmpty()) {
    		String msg = String.format("No Route for distance-in-km greater than %s is found", distance);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponse> findByDistanceKmLessThan(BigDecimal distance) {
    	validateBigDecimalNonNegative(distance);
    	List<Route> items = routeRepository.findByDistanceKmLessThan(distance);
    	if(items.isEmpty()) {
    		String msg = String.format("No Route for distance-in-km less than %s is found", distance);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(RouteResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("String must not be empty nor null");
    	}
    }
    
    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new ValidationException("Number must be positive");
    	}
    }
}
