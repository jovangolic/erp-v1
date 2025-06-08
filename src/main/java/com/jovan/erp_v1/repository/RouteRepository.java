package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByOrigin(String origin);

    List<Route> findByDestination(String destination);

    List<Route> findByOriginAndDestination(String origin, String destination);

    List<Route> findByDistanceKmGreaterThan(Double distance);

    List<Route> findByDistanceKmLessThan(Double distance);
}
