package com.jovan.erp_v1.service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.VehicleLocationMapper;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.model.VehicleLocation;
import com.jovan.erp_v1.repository.VehicleLocationRepository;
import com.jovan.erp_v1.repository.VehicleRepository;
import com.jovan.erp_v1.repository.specification.VehicleLocationSpecification;
import com.jovan.erp_v1.request.VehicleLocationRequest;
import com.jovan.erp_v1.response.VehicleLocationResponse;
import com.jovan.erp_v1.response.VehicleResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.VehicleLocationSaveAsRequest;
import com.jovan.erp_v1.search_request.VehicleLocationSearchRequest;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleLocationService implements IVehicleLocationService {
	
	private final VehicleLocationRepository vehicleLocationRepository;
	private final VehicleRepository vehicleRepository;
	private final VehicleLocationMapper vehicleLocationMapper;
	
	@Override
	public List<VehicleResponse> findVehiclesByLocationTimeRange(LocalDateTime from, LocalDateTime to) {
		DateValidator.validateRange(from, to);
		Specification<VehicleLocation> spec = VehicleLocationSpecification.hasRecordedAtBetween(from, to);
	    List<VehicleLocation> items = vehicleLocationRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Vehicles found for given search criteria");
		}
		return items.stream()
	            .map(VehicleLocation::getVehicle)    
	            .distinct()                          
	            .map(VehicleResponse::new)           
	            .collect(Collectors.toList());
	}
	
	@Override
	public List<VehicleLocationResponse> findLocationsByTimeRange(LocalDateTime from, LocalDateTime to) {
		DateValidator.validateRange(from, to);
		Specification<VehicleLocation> spec = VehicleLocationSpecification.hasRecordedAtBetween(from, to);
	    List<VehicleLocation> items = vehicleLocationRepository.findAll(spec);
	    if (items.isEmpty()) {
	        throw new NoDataFoundException("No Vehicle-Locations found for given criteria");
	    }
	    return items.stream()
	            .map(VehicleLocationResponse::new) 
	            .collect(Collectors.toList());
	}

	@Override
	public List<VehicleLocationResponse> findLocationsByRequest(VehicleLocationSearchRequest req) {
		validateVehicleLocationSearchRequest(req);
		Specification<VehicleLocation> spec = VehicleLocationSpecification.fromRequest(req);
		List<VehicleLocation> items = vehicleLocationRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Vehicles found for given criteria");
		}
		return items.stream().map(VehicleLocationResponse::new).collect(Collectors.toList());			
	}

	@Transactional
	@Override
	public VehicleLocationResponse create(VehicleLocationRequest request) {
		Vehicle v = validateVehicleId(request.vehicleId());
		if(request.latitude() == null) {
			throw new ValidationException("Latitude must not be null");
		}
		if(request.longitude() == null) {
			throw new ValidationException("Longitude must not be null");
		}
		VehicleLocation vl = vehicleLocationMapper.toEntity(request, v);
		VehicleLocation saved = vehicleLocationRepository.save(vl);
		return new VehicleLocationResponse(saved);
	}

	@Transactional
	@Override
	public VehicleLocationResponse update(Long id, VehicleLocationRequest request) {
		if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		VehicleLocation item = vehicleLocationRepository.findById(id).orElseThrow(() -> new ValidationException("VehicleLocation bot found with id "+id));
		Vehicle v = item.getVehicle();
		if(request.vehicleId() != null && (v.getId() == null || !request.vehicleId().equals(v.getId()))) {
			v = validateVehicleId(request.vehicleId());
		}
		if(request.latitude() == null) {
			throw new ValidationException("Latitude must not be null");
		}
		if(request.longitude() == null) {
			throw new ValidationException("Longitude must not be null");
		}
		vehicleLocationMapper.toEntityUpdate(request, item, v);
		VehicleLocation saved = vehicleLocationRepository.save(item);
		return new VehicleLocationResponse(saved);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!vehicleLocationRepository.existsById(id)) {
			throw new ValidationException("VehicleLocation bot found with id "+id);
		}
		vehicleLocationRepository.deleteById(id);
	}

	@Override
	public VehicleLocationResponse findOne(Long id) {
		VehicleLocation item = vehicleLocationRepository.findById(id).orElseThrow(() -> new ValidationException("VehicleLocation bot found with id "+id));
		return new VehicleLocationResponse(item);
	}

	@Override
	public List<VehicleLocationResponse> findAll() {
		List<VehicleLocation> items = vehicleLocationRepository.findAll();
		if(items.isEmpty()) {
			throw new ValidationException("VehicleLocation list is empty");
		}
		return items.stream().map(VehicleLocationResponse::new).collect(Collectors.toList());
	}
	
	@Override
	public List<VehicleLocationResponse> generalSearch(VehicleLocationSearchRequest req) {
		Specification<VehicleLocation> spec = VehicleLocationSpecification.fromRequest(req);
		List<VehicleLocation> items = vehicleLocationRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No VehicleLocation found for given search criteria");
		}
		return items.stream().map(vehicleLocationMapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional
	@Override
	public VehicleLocationResponse saveVehicleLocation(VehicleLocationRequest request) {
		VehicleLocation vl = VehicleLocation.builder()
				.id(request.id())
				.vehicle(validateVehicleId(request.vehicleId()))
				.latitude(request.latitude())
				.longitude(request.longitude())
				.build();
		VehicleLocation saved = vehicleLocationRepository.save(vl);
		return new VehicleLocationResponse(saved);
	}
	
	private final AbstractSaveAsService<VehicleLocation, VehicleLocationResponse> helperAsSave = new AbstractSaveAsService<VehicleLocation, VehicleLocationResponse>() {
		
		@Override
		protected VehicleLocationResponse toResponse(VehicleLocation entity) {
			return new VehicleLocationResponse(entity);
		}
		
		@Override
		protected JpaRepository<VehicleLocation, Long> getRepository() {
			return vehicleLocationRepository;
		}
		
		@Override
		protected VehicleLocation copyAndOverride(VehicleLocation source, Map<String, Object> overrides) {
			return VehicleLocation.builder()
					.vehicle(validateVehicleId(source.getVehicle().getId()))
					.latitude(source.getLatitude())
					.longitude(source.getLongitude())
					.build();
		}
	};
	
	private final AbstractSaveAllService<VehicleLocation, VehicleLocationResponse> helpSaveAll = new AbstractSaveAllService<VehicleLocation, VehicleLocationResponse>() {
		
		@Override
		protected Function<VehicleLocation, VehicleLocationResponse> toResponse() {
			return VehicleLocationResponse::new;
		}
		
		@Override
		protected JpaRepository<VehicleLocation, Long> getRepository() {
			return vehicleLocationRepository;
		}
	};

	@Transactional
	@Override
	public VehicleLocationResponse saveAs(VehicleLocationSaveAsRequest req) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		if(req.vehicleId() != null) overrides.put("Vehicle-id", req.vehicleId());
		if(req.latitude() != null) overrides.put("Latitude", req.latitude());
		if(req.longitude() != null) overrides.put("Longitude", req.longitude());
		return helperAsSave.saveAs(req.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<VehicleLocationResponse> saveAll(List<VehicleLocationRequest> requests) {
		List<VehicleLocation> items = requests.stream()
				.map(it -> VehicleLocation.builder()
						.id(it.id())
						.vehicle(validateVehicleId(it.vehicleId()))
						.latitude(it.latitude())
						.longitude(it.longitude())
						.build())
				.collect(Collectors.toList());
		return helpSaveAll.saveAll(items);
	}

	private void validateVehicleLocationSearchRequest(VehicleLocationSearchRequest req) {
		if(req == null) {
			throw new ValidationException("VehicleLocationSearchRequest req must not be null");
		}
		validateVehicleLocationId(req.id());
		validateVehicleId(req.vehicleId());
		validateIdRange(req.vehicleIdFrom(), req.vehicleIdTo());
		if(req.latitude() == null) {
			throw new ValidationException("Latitude must not be null");
		}
		if(req.longitude() == null) {
			throw new ValidationException("Longitude must not be null");
		}
		DateValidator.validateRange(req.recordedAtFrom(), req.recordedAtTo());
	}
	
	private VehicleLocation validateVehicleLocationId(Long id) {
		if(id == null) {
			throw new ValidationException("VehicleLocation ID must not be null");
		}
		return vehicleLocationRepository.findById(id).orElseThrow(() -> new ValidationException("VehicleLocation not found with id "+id));
	}
	
	private Vehicle validateVehicleId(Long vehicleId) {
		if(vehicleId == null) {
			throw new ValidationException("Vehicle ID must not be null");
		}
		return vehicleRepository.findById(vehicleId).orElseThrow(() -> new ValidationException("Vehicle not found with id "+vehicleId));
	}
	
	private void validateIdRange(Long idFrom, Long idTo) {
		if(idFrom == null || idTo == null) {
			throw new ValidationException("Both idFrom and idTo, must not be null");
		}
		if(idFrom < 0 || idTo < 0) {
			throw new ValidationException("Both idFrom and idTo, must be positive numbers");
		}
		if(idFrom > idTo) {
			throw new ValidationException("IdFrom must not be greater than idTo");
		}
	}

}
