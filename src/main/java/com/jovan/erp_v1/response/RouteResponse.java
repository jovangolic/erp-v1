package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.Route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {

    private Long id;
    private String origin;
    private String destination;
    private Double distanceKm;

    public RouteResponse(Route r) {
        this.id = r.getId();
        this.origin = r.getOrigin();
        this.destination = r.getDestination();
        this.distanceKm = r.getDistanceKm();
    }
}
