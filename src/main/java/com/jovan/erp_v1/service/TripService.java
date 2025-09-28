package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TripMapper;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.model.Trip;
import com.jovan.erp_v1.repository.DriverRepository;
import com.jovan.erp_v1.repository.TripRepository;
import com.jovan.erp_v1.request.TripRequest;
import com.jovan.erp_v1.request.TripSearchRequest;
import com.jovan.erp_v1.response.TripResponse;
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
	public List<TripResponse> findbyStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(
			TripStatus status, String firstName, String lastName) {
		validateTripStatus(status);
		validateStringsNotEmpty("First-name, Last-name", firstName,lastName);
		List<Trip> items = tripRepository.findbyStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(status, firstName, lastName);
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
	public List<TripResponse> findByStartTimeOnly(LocalDateTime startOfDay, LocalDateTime endOfDay) {
		DateValidator.validateRange(startOfDay, endOfDay);
		List<Trip> items = tripRepository.findByStartTimeOnly(startOfDay, endOfDay);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("", startOfDay.format(formatter), endOfDay.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(tripMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TripResponse> generalSearch(TripSearchRequest req) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	@Override
	public TripResponse saveTrip(TripRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	@Override
	public TripResponse saveAs() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	@Override
	public List<TripResponse> saveAll(List<TripRequest> requests) {
		// TODO Auto-generated method stub
		return null;
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
