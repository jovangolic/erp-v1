package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.enumeration.TripTypeStatus;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TripMapper;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.model.Trip;
import com.jovan.erp_v1.repository.DriverRepository;
import com.jovan.erp_v1.repository.TripRepository;
import com.jovan.erp_v1.repository.specification.TripSpecification;
import com.jovan.erp_v1.request.TripRequest;
import com.jovan.erp_v1.request.TripSearchRequest;
import com.jovan.erp_v1.response.TripResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.TripSaveAsRequest;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService implements ITripService {

	private final TripRepository tripRepository;
	private final TripMapper tripMapper;
	private final DriverRepository driverRepository;
	
	@Transactional
	@Override
	public TripResponse create(TripRequest request) {
		validateString(request.startLocation());
		validateString(request.endLocation());
		DateValidator.validateNotNull(request.endTime(), "End-time");
		validateTripStatus(request.status());
		validateTripTypeStatus(request.typeStatus());
		Driver d = validateDriverId(request.driverId());
		Trip t = tripMapper.toEntity(request, d);
		Trip saved = tripRepository.save(t);
		return new TripResponse(saved);
	}
	
	@Transactional
	@Override
	public TripResponse update(Long id, TripRequest request) {
		if(!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		Trip t = tripRepository.findById(id).orElseThrow(() -> new ValidationException("Trip not found with id "+id));
		validateStringsNotEmpty("start-location, end-location", request.startLocation(), request.endLocation());
		DateValidator.validateNotNull(request.endTime(), "End-time");
		validateTripStatus(request.status());
		validateTripTypeStatus(request.typeStatus());
		Driver d = t.getDriver();
		if(request.driverId() != null && (d == null || !request.driverId().equals(d.getId()))) {
			d = validateDriverId(request.driverId());
		}
		tripMapper.toEntityUpdate(t, request, d);
		Trip saved = tripRepository.save(t);
		return new TripResponse(saved);
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		if(!tripRepository.existsById(id)) {
			throw new ValidationException("Trip bot found with id "+id);
		}
		tripRepository.deleteById(id);
	}
	
	@Override
	public TripResponse findOne(Long id) {
		Trip t = tripRepository.findById(id).orElseThrow(() -> new ValidationException("Trip not found with id "+id));
		return new TripResponse(t);
	}
	
	@Override
	public List<TripResponse> findAll() {
		List<Trip> items = tripRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Trip list is empty");
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<TripResponse> findMyTrips(Long driverId) {
		if(driverId == null) {
			throw new ValidationException("Driver-ID must not be null");
		}
		List<Trip> trips = tripRepository.findByDriverIdSecure(driverId);
        return trips.stream()
                .map(tripMapper::toResponse)
                .toList();
	}
	
	@Override
	public List<TripResponse> findByStartLocationContainingIgnoreCase(String startLocation) {
		validateString(startLocation);
		List<Trip> items = tripRepository.findByStartLocationContainingIgnoreCase(startLocation);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for start-location %s is found", startLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByEndLocationContainingIgnoreCase(String endLocation) {
		validateString(endLocation);
		List<Trip> items = tripRepository.findByEndLocationContainingIgnoreCase(endLocation);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for end-location %s is found", endLocation);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByStartTime(LocalDateTime startTime) {
		DateValidator.validateNotNull(startTime, "Start-time");
		List<Trip> items = tripRepository.findByStartTime(startTime);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip for start-time %s is found", startTime.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByStartTimeBefore(LocalDateTime startTime) {
		DateValidator.validateNotInFuture(startTime, "Start-time before");
		List<Trip> items = tripRepository.findByStartTimeBefore(startTime);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip for start-time before %s, is found", startTime.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByStartTimeAfter(LocalDateTime startTime) {
		DateValidator.validateNotInPast(startTime, "Start-time after");
		List<Trip> items = tripRepository.findByStartTimeAfter(startTime);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip for start-time after %s, is found", startTime.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByStartTimeBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<Trip> items = tripRepository.findByStartTimeBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip for start-time between %s and %s, is found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByEndTime(LocalDateTime endTime) {
		DateValidator.validateNotNull(endTime, "End-time");
		List<Trip> items = tripRepository.findByEndTime(endTime);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip for end-time %s, is found", endTime.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByEndTimeBefore(LocalDateTime endTime) {
		DateValidator.validateNotInFuture(endTime, "End-time before");
		List<Trip> items = tripRepository.findByEndTimeBefore(endTime);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip for end-time before %s, is found", endTime.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByEndTimeAfter(LocalDateTime endTime) {
		DateValidator.validateNotInPast(endTime, "End-time after");
		List<Trip> items = tripRepository.findByEndTimeAfter(endTime);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip for end-time after %s, is found", endTime.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByEndTimeBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<Trip> items = tripRepository.findByEndTimeBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip for end time between %s and %s, is found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findTripsWithinPeriod(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<Trip> items = tripRepository.findTripsWithinPeriod(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Trip within period between %s and  %s, is found", 
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByStatus(TripStatus status) {
		validateTripStatus(status);
		List<Trip> items = tripRepository.findByStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for trip-status %s is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByDriverId(Long driverId) {
		validateDriverId(driverId);
		List<Trip> items = tripRepository.findByDriverId(driverId);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for driver's id %d, is found", driverId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(
			TripStatus status, String firstName, String lastName) {
		validateTripStatus(status);
		validateStringsNotEmpty("First-name, Last-name", firstName,lastName);
		List<Trip> items = tripRepository.findByStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(status, firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for driver's first-name %s, last-name %s and trip-status %s is found", 
					firstName,lastName,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(
			String firstName, String lastName) {
		validateStringsNotEmpty("First-name, Last-name", firstName,lastName);
		List<Trip> items = tripRepository.findByDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for driver's first-name %s and last-name %s is found", firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByDriverPhoneLikeIgnoreCase(String phone) {
		validateString(phone);
		List<Trip> items = tripRepository.findByDriverPhoneLikeIgnoreCase(phone);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for driver's phone %s, is found", phone);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByDriver_Status(DriverStatus status) {
		validateDriverStatus(status);
		List<Trip> items = tripRepository.findByDriver_Status(status);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for driver's status %s, is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> findByStatusIn(List<TripStatus> statuses) {
		validateTripStatusList(statuses);
		List<Trip> items = tripRepository.findByStatusIn(statuses);
		if(items.isEmpty()) {
			String msg = String.format("No Trip for %statuses, is found", statuses);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> searchByDateOnly(LocalDate dateOnly) {
		if (dateOnly == null) {
	        throw new ValidationException("Date must not be null");
	    }
	    LocalDateTime startOfDay = dateOnly.atStartOfDay();
	    LocalDateTime endOfDay = dateOnly.atTime(LocalTime.MAX);
		DateValidator.validateRange(startOfDay, endOfDay);
		List<Trip> items = tripRepository.findByStartTimeOnly(startOfDay, endOfDay);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Trip found for date " + dateOnly);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> generalSearch(TripSearchRequest req) {
		Specification<Trip> spec = TripSpecification.fromRequest(req);
        List<Trip> trips = tripRepository.findAll(spec);
        if (trips.isEmpty()) {
            throw new NoDataFoundException("No trips found for given search criteria");
        }
        return trips.stream()
                    .map(tripMapper::toResponse)
                    .toList();
	}
	
	@Transactional
	@Override
	public TripResponse saveTrip(TripRequest request) {
		Trip t = Trip.builder()
				.id(request.id())
				.startLocation(request.startLocation())
				.endLocation(request.endLocation())
				.endTime(request.endTime())
				.status(request.status())
				.driver(validateDriverId(request.driverId()))
				.confirmed(request.confirmed())
				.build();
		Trip saved = tripRepository.save(t);
		return new TripResponse(saved);
	}
	
	private final AbstractSaveAsService<Trip, TripResponse> helperSaveAs = new AbstractSaveAsService<Trip, TripResponse>() {
		
		@Override
		protected TripResponse toResponse(Trip entity) {
			return new TripResponse(entity);
		}
		
		@Override
		protected JpaRepository<Trip, Long> getRepository() {
			return tripRepository;
		}
		
		@Override
		protected Trip copyAndOverride(Trip source, Map<String, Object> overrides) {
			return Trip.builder()
					.startLocation((String) overrides.getOrDefault("start-location", source.getStartLocation()))
					.endLocation((String) overrides.getOrDefault("end-location", source.getEndLocation()))
					.endTime(source.getEndTime())
					.status(source.getStatus())
					.driver(validateDriverId(source.getDriver().getId()))
					.confirmed(source.getConfirmed())
					.build();
		}
	};
	
	private final AbstractSaveAllService<Trip, TripResponse> helpSaveAll = new AbstractSaveAllService<Trip, TripResponse>() {
		
		@Override
		protected Function<Trip, TripResponse> toResponse() {
			return TripResponse::new;
		}
		
		@Override
		protected JpaRepository<Trip, Long> getRepository() {
			return tripRepository;
		}
	};
	
	@Transactional
	@Override
	public TripResponse saveAs(TripSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		if(request.startLocation() != null) overrides.put("Start-location", request.startLocation());
		if(request.endLocation() != null) overrides.put("End-location", request.endLocation());
		if(request.endTime() != null) overrides.put("End-time", request.endTime());
		if(request.driverId() != null) overrides.put("Driver-id", validateDriverId(request.driverId()));
		return helperSaveAs.saveAs(request.sourceId(), overrides);
	}
	
	@Transactional
	@Override
	public List<TripResponse> saveAll(List<TripRequest> requests) {
		List<Trip> items = requests.stream()
				.map(req -> Trip.builder()
						.id(req.id())
						.startLocation(req.startLocation())
						.endLocation(req.endLocation())
						.endTime(req.endTime())
						.status(req.status())
						.driver(validateDriverId(req.driverId()))
						.confirmed(req.confirmed())
						.build())
				.collect(Collectors.toList());	
		return helpSaveAll.saveAll(items);
	}
	
	@Transactional
	@Override
	public TripResponse cancelTrip(Long id) {
		Trip t = tripRepository.findById(id).orElseThrow(() -> new ValidationException("Trip not found with id "+id));
		if(t.getTypeStatus() != TripTypeStatus.NEW && t.getTypeStatus() != TripTypeStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED defects can be cancelled");
		}
		t.setTypeStatus(TripTypeStatus.CANCELLED);
		return new TripResponse(tripRepository.save(t));
	}

	@Transactional
	@Override
	public TripResponse closeTrip(Long id) {
		Trip t = tripRepository.findById(id).orElseThrow(() -> new ValidationException("Trip not found with id "+id));
		if(t.getTypeStatus() != TripTypeStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED defects can be closed");
		}
		t.setTypeStatus(TripTypeStatus.CLOSED);
		return new TripResponse(tripRepository.save(t));
	}

	@Transactional
	@Override
	public TripResponse confirmTrip(Long id) {
		Trip t = tripRepository.findById(id).orElseThrow(() -> new ValidationException("Trip not found with id "+id));
		t.setConfirmed(true);
		t.setTypeStatus(TripTypeStatus.CONFIRMED);
		tripRepository.save(t);
		return new TripResponse(t);
	}
	
	@Transactional
	@Override
	public TripResponse changeStatus(Long id, TripTypeStatus newStatus) {
		validateTripTypeStatus(newStatus);
		Trip t = tripRepository.findById(id).orElseThrow(() -> new ValidationException("Trip not found with id "+id));
		if(t.getTypeStatus() == TripTypeStatus.CLOSED) {
			throw new ValidationException("Closed trips cannot change status");
		}
		if(newStatus == TripTypeStatus.CONFIRMED) {
			if(t.getTypeStatus() != TripTypeStatus.NEW) {
				throw new ValidationException("Only NEW trips can be confirmed");
			}
			t.setConfirmed(true);
		}
		t.setTypeStatus(newStatus);
		return new TripResponse(tripRepository.save(t));
	}

	private void validateTripTypeStatus(TripTypeStatus newStatus) {
		Optional.ofNullable(newStatus)
			.orElseThrow(() -> new ValidationException("TripTypeStatus newStatus must not be null"));
	}
	
	private void validateTripStatusList(List<TripStatus> statuses) {
		if(statuses == null || statuses.isEmpty()) {
			throw new ValidationException("TripStatus list must not be null nor empty");
		}
		//lambda funkcija koja vrsi iteraciju, kao for-petlja
		if (statuses.stream().anyMatch(Objects::isNull)) {
	        throw new ValidationException("TripStatus list must not contain null items");
	    }
	}
	
	private void validateDriverStatus(DriverStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("DriverStatus status must not be null"));
	}
	
	private void validateStringsNotEmpty(String fieldName, String... values) {
	    for (String val : values) {
	        if (val == null || val.trim().isEmpty()) {
	            throw new ValidationException(fieldName + " must not be null or empty");
	        }
	    }
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private Driver validateDriverId(Long driverId) {
		if(driverId == null) {
			throw new ValidationException("Driver ID must not be null");
		}
		return driverRepository.findById(driverId).orElseThrow(() -> new ValidationException("Driver not found with id "+driverId));
	}
	
	private void validateTripStatus(TripStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("TripStatus status must not be null"));
	}
	
}
