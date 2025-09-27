package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class DriverMapper extends AbstractMapper<DriverRequest> {
	
	
	public Driver toEntity(DriverRequest request) {
		Objects.requireNonNull(request, "DriverRequest must not be null");
		validateIdForCreate(request, DriverRequest::id);
		Driver d = new Driver();
		d.setId(request.id());
		d.setFirstName(request.firstName());
		d.setLastName(request.lastName());
		d.setPhone(request.phone());
		d.setStatus(request.status());
		d.setConfirmed(request.confirmed());
		return d;
	}
	
	public Driver toEntityUpdate(Driver d, DriverRequest request) {
		Objects.requireNonNull(d, "Driver must not be null");
		Objects.requireNonNull(request, "DriverRequest must not be null");
		validateIdForUpdate(request, DriverRequest::id);
		d.setFirstName(request.firstName());
		d.setLastName(request.lastName());
		d.setPhone(request.phone());
		d.setStatus(request.status());
		d.setConfirmed(request.confirmed());
		return d;
	}
	
	public DriverResponse toRespone(Driver d) {
		Objects.requireNonNull(d, "Driver must not be null");
		return new DriverResponse(d);
	}
	
	public List<DriverResponse> toResponseList(List<Driver> d){
		if(d == null ||  d.isEmpty()) {
			return Collections.emptyList();
		}
		return d.stream().map(this::toRespone).collect(Collectors.toList());
	}

}
