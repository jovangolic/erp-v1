package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.enumeration.TripTypeStatus;
import com.jovan.erp_v1.request.TripRequest;
import com.jovan.erp_v1.request.TripSearchRequest;
import com.jovan.erp_v1.response.TripResponse;
import com.jovan.erp_v1.save_as.TripSaveAsRequest;

public interface ITripService {

	TripResponse create(TripRequest request);
	TripResponse update(Long id, TripRequest request);
	void delete(Long id);
	TripResponse findOne(Long id);
	List<TripResponse> findAll();
	
	List<TripResponse> findByStartLocationContainingIgnoreCase(String startLocation);
	List<TripResponse> findByEndLocationContainingIgnoreCase(String endLocation);
	List<TripResponse> findByStartTime(LocalDateTime startTime);
	List<TripResponse> findByStartTimeBefore(LocalDateTime startTime);
	List<TripResponse> findByStartTimeAfter(LocalDateTime startTime);
	List<TripResponse> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
	List<TripResponse> findByEndTime(LocalDateTime endTime);
	List<TripResponse> findByEndTimeBefore(LocalDateTime endTime);
	List<TripResponse> findByEndTimeAfter(LocalDateTime endTime);
	List<TripResponse> findByEndTimeBetween(LocalDateTime start, LocalDateTime end);

	List<TripResponse> findTripsWithinPeriod( LocalDateTime start,  LocalDateTime end);
	
	List<TripResponse> findByStatus(TripStatus status);
	List<TripResponse> findByDriverId(Long driverId);
	List<TripResponse> findByStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(TripStatus status, String firstName, String lastName);
	List<TripResponse> findByDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(String firstName, String lastName);
	List<TripResponse> findByDriverPhoneLikeIgnoreCase(String phone);
	List<TripResponse> findByDriver_Status(DriverStatus status);
	
	List<TripResponse> findByStatusIn( List<TripStatus> statuses);
	List<TripResponse> searchByDateOnly(LocalDate dateOnly);
	List<TripResponse> findMyTrips(Long driverId);
	TripResponse changeStatus(Long id, TripTypeStatus newStatus);
	
	List<TripResponse> generalSearch(TripSearchRequest req);
	
	TripResponse cancelTrip(Long id);
	TripResponse closeTrip(Long id);
	TripResponse confirmTrip(Long id);
	
	//save, save-as, saveAll
	TripResponse saveTrip(TripRequest request);
	TripResponse saveAs(TripSaveAsRequest request);
	List<TripResponse> saveAll(List<TripRequest> requests);
}
