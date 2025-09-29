package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.model.Trip;
import com.jovan.erp_v1.request.TripRequest;
import com.jovan.erp_v1.response.TripResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class TripMapper extends AbstractMapper<TripRequest> {

	
	public Trip toEntity(TripRequest request, Driver d) {
		Objects.requireNonNull(request, "TripRequest must not be null");
		Objects.requireNonNull(d, "Driver must not be null");
		validateIdForCreate(request, TripRequest::id);
		Trip t = new Trip();
		t.setId(request.id());
		t.setStartLocation(request.startLocation());
		t.setEndLocation(request.endLocation());
		t.setEndTime(request.endTime());
		t.setStatus(request.status());
		t.setTypeStatus(request.typeStatus());
		t.setDriver(d);
		return t;
	}
	
	public Trip toEntityUpdate(Trip t, TripRequest request, Driver d) {
		Objects.requireNonNull(t, "Trip must not be null");
		Objects.requireNonNull(request, "TripRequest must not be null");
		Objects.requireNonNull(d, "Driver must not be null");
		validateIdForUpdate(request, TripRequest::id);
		t.setStartLocation(request.startLocation());
		t.setEndLocation(request.endLocation());
		t.setEndTime(request.endTime());
		t.setStatus(request.status());
		t.setTypeStatus(request.typeStatus());
		t.setDriver(d);
		return t;
	}
	
	public TripResponse toResponse(Trip t) {
		Objects.requireNonNull(t, "Trip must not be null");
		return new TripResponse(t);
	}
	
	public List<TripResponse> toResponseList(List<Trip> t){
		if(t == null || t.isEmpty()) {
			return Collections.emptyList();
		}
		return t.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
