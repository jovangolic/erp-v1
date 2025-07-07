package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.RouteRequest;
import com.jovan.erp_v1.response.RouteResponse;
import com.jovan.erp_v1.service.IRouteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
public class RouteController {

    private final IRouteService routeService;

    @PostMapping("/create/new-route")
    public ResponseEntity<RouteResponse> create(@Valid @RequestBody RouteRequest request) {
        RouteResponse response = routeService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RouteResponse> update(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
        RouteResponse response = routeService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<RouteResponse> findOne(@PathVariable Long id) {
        RouteResponse response = routeService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<RouteResponse>> findAll() {
        List<RouteResponse> responses = routeService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/origin")
    public ResponseEntity<List<RouteResponse>> findByOrigin(@RequestParam("origin") String origin) {
        List<RouteResponse> responses = routeService.findByOrigin(origin);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/destination")
    public ResponseEntity<List<RouteResponse>> findByDestination(@RequestParam("destination") String destination) {
        List<RouteResponse> responses = routeService.findByDestination(destination);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/origin-destination")
    public ResponseEntity<List<RouteResponse>> findByOriginAndDestination(@RequestParam("origin") String origin,
            @RequestParam("destination") String destination) {
        List<RouteResponse> responses = routeService.findByOriginAndDestination(origin, destination);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/distance-greater")
    public ResponseEntity<List<RouteResponse>> findByDistanceKmGreaterThan(@RequestParam("distance") BigDecimal distance) {
        List<RouteResponse> responses = routeService.findByDistanceKmGreaterThan(distance);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/distance-less")
    public ResponseEntity<List<RouteResponse>> findByDistanceKmLessThan(@RequestParam("distance") BigDecimal distance) {
        List<RouteResponse> responses = routeService.findByDistanceKmLessThan(distance);
        return ResponseEntity.ok(responses);
    }
}
