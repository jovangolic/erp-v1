package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.TripSearchRequest;
import com.jovan.erp_v1.response.TripResponse;
import com.jovan.erp_v1.service.ITripService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips")
public class TripController {

	private final ITripService tripService;
	
	
	@PostMapping("/general-search")
    public ResponseEntity<List<TripResponse>> searchTrips(@RequestBody TripSearchRequest request) {
        return ResponseEntity.ok(tripService.generalSearch(request));
    }
}
