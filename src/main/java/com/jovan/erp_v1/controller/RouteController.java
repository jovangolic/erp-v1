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
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
@PreAuthorize(RoleGroups.ROUTE_READ_ACCESS)
public class RouteController {

    private final IRouteService routeService;

    @PreAuthorize(RoleGroups.ROUTE_FULL_ACCESS)
    @PostMapping("/create/new-route")
    public ResponseEntity<RouteResponse> create(@Valid @RequestBody RouteRequest request) {
        RouteResponse response = routeService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ROUTE_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<RouteResponse> update(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
        RouteResponse response = routeService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ROUTE_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.ROUTE_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<RouteResponse> findOne(@PathVariable Long id) {
        RouteResponse response = routeService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ROUTE_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<RouteResponse>> findAll() {
        List<RouteResponse> responses = routeService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ROUTE_READ_ACCESS)
    @GetMapping("/origin")
    public ResponseEntity<List<RouteResponse>> findByOrigin(@RequestParam("origin") String origin) {
        List<RouteResponse> responses = routeService.findByOrigin(origin);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ROUTE_READ_ACCESS)
    @GetMapping("/destination")
    public ResponseEntity<List<RouteResponse>> findByDestination(@RequestParam("destination") String destination) {
        List<RouteResponse> responses = routeService.findByDestination(destination);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ROUTE_READ_ACCESS)
    @GetMapping("/origin-destination")
    public ResponseEntity<List<RouteResponse>> findByOriginAndDestination(@RequestParam("origin") String origin,
            @RequestParam("destination") String destination) {
        List<RouteResponse> responses = routeService.findByOriginAndDestination(origin, destination);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ROUTE_READ_ACCESS)
    @GetMapping("/distance-greater")
    public ResponseEntity<List<RouteResponse>> findByDistanceKmGreaterThan(@RequestParam("distance") BigDecimal distance) {
        List<RouteResponse> responses = routeService.findByDistanceKmGreaterThan(distance);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ROUTE_READ_ACCESS)
    @GetMapping("/distance-less")
    public ResponseEntity<List<RouteResponse>> findByDistanceKmLessThan(@RequestParam("distance") BigDecimal distance) {
        List<RouteResponse> responses = routeService.findByDistanceKmLessThan(distance);
        return ResponseEntity.ok(responses);
    }
}
