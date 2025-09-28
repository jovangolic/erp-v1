package com.jovan.erp_v1.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.model.CustomUserDetails;
import com.jovan.erp_v1.request.TripRequest;
import com.jovan.erp_v1.request.TripSearchRequest;
import com.jovan.erp_v1.response.TripResponse;
import com.jovan.erp_v1.save_as.TripSaveAsRequest;
import com.jovan.erp_v1.service.ITripService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips")
@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
public class TripController {

	private final ITripService tripService;
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PostMapping("/create/new-trip")
	public ResponseEntity<TripResponse> create(@Valid @RequestBody TripRequest request){
		TripResponse items = tripService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<TripResponse> update(@PathVariable Long id, @Valid @RequestBody TripRequest request){
		TripResponse items = tripService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		tripService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<TripResponse> findOne(@PathVariable Long id){
		TripResponse items = tripService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<TripResponse>> findAll(){
		List<TripResponse> items = tripService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/my-trips")
    public List<TripResponse> getMyTrips(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return null;
    }
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/start-location")
	public ResponseEntity<List<TripResponse>> findByStartLocationContainingIgnoreCase(@RequestParam("startLocation") String startLocation){
		List<TripResponse> items = tripService.findByStartLocationContainingIgnoreCase(startLocation);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/end-location")
	public ResponseEntity<List<TripResponse>> findByEndLocationContainingIgnoreCase(@RequestParam("endLocation") String endLocation){
		List<TripResponse> items = tripService.findByEndLocationContainingIgnoreCase(endLocation);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/start-time")
	public ResponseEntity<List<TripResponse>> findByStartTime(@RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime){
		List<TripResponse> items = tripService.findByStartTime(startTime);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/start-time-before")
	public ResponseEntity<List<TripResponse>> findByStartTimeBefore(@RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime){
		List<TripResponse> items = tripService.findByStartTimeBefore(startTime);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/start-time-after")
	public ResponseEntity<List<TripResponse>> findByStartTimeAfter(@RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime){
		List<TripResponse> items = tripService.findByStartTimeAfter(startTime);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/start-time-range")
	public ResponseEntity<List<TripResponse>> findByStartTimeBetween(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<TripResponse> items = tripService.findByStartTimeBetween(start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/end-time")
	public ResponseEntity<List<TripResponse>> findByEndTime(@RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime){
		List<TripResponse> items = tripService.findByEndTime(endTime);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/end-time-before")
	public ResponseEntity<List<TripResponse>> findByEndTimeBefore(@RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime){
		List<TripResponse> items = tripService.findByEndTimeBefore(endTime);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/end-time-after")
	public ResponseEntity<List<TripResponse>> findByEndTimeAfter(
			@RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime){
		List<TripResponse> items = tripService.findByEndTimeAfter(endTime);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/end-time-range")
	public ResponseEntity<List<TripResponse>> findByEndTimeBetween(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<TripResponse> items = tripService.findByEndTimeBetween(start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/within-period-range")
	public ResponseEntity<List<TripResponse>> findTripsWithinPeriod(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<TripResponse> items = tripService.findTripsWithinPeriod(start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/trip-status")
	public ResponseEntity<List<TripResponse>> findByStatus(@RequestParam("status") TripStatus status){
		List<TripResponse> items = tripService.findByStatus(status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/driver/{driverId}")
	public ResponseEntity<List<TripResponse>> findByDriverId(@PathVariable Long driverId){
		List<TripResponse> items = tripService.findByDriverId(driverId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/trip-status-and-driver-first-last-name")
	public ResponseEntity<List<TripResponse>> findbyStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(
			@RequestParam("status") TripStatus status,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName){
		List<TripResponse> items = tripService.findbyStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(status, firstName, lastName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/driver/first-name-and-last-name")
	public ResponseEntity<List<TripResponse>> findByDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(
			@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<TripResponse> items = tripService.findByDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("search/driver-phone")
	public ResponseEntity<List<TripResponse>> findByDriverPhoneLikeIgnoreCase(@RequestParam("phone") String phone){
		List<TripResponse> items = tripService.findByDriverPhoneLikeIgnoreCase(phone);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("search/driver-status")
	public ResponseEntity<List<TripResponse>> findByDriver_Status(@RequestParam("status") DriverStatus status){
		List<TripResponse> items = tripService.findByDriver_Status(status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/trip-statuses")
	public ResponseEntity<List<TripResponse>> findByStatusIn(@RequestParam("statuses") List<TripStatus> statuses){
		List<TripResponse> items = tripService.findByStatusIn(statuses);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_READ_ACCESS)
	@GetMapping("/search/by-date")
	public ResponseEntity<List<TripResponse>> searchByDateOnly(@RequestParam("dateOnly") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOnly){
		List<TripResponse> items = tripService.searchByDateOnly(dateOnly);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PostMapping("/general-search")
    public ResponseEntity<List<TripResponse>> searchTrips(@RequestBody TripSearchRequest request) {
        return ResponseEntity.ok(tripService.generalSearch(request));
    }
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PostMapping("/{id}/cancel")
	public ResponseEntity<TripResponse> cancelTrip(@PathVariable Long id){
		TripResponse items = tripService.cancelTrip(id);
		return ResponseEntity.ok(items);	
	}
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PostMapping("/{id}/close")
	public ResponseEntity<TripResponse> closeTrip(@PathVariable Long id){
		TripResponse items = tripService.closeTrip(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PostMapping("/{id}/confirm")
	public ResponseEntity<TripResponse> confirmTrip(@PathVariable Long id){
		TripResponse items = tripService.confirmTrip(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PostMapping("/save")
	public ResponseEntity<TripResponse> saveTrip(@Valid @RequestBody TripRequest request){
		TripResponse items = tripService.saveTrip(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PostMapping("/save-as")
	public ResponseEntity<TripResponse> saveAs(@Valid @RequestBody TripSaveAsRequest request){
		TripResponse items = tripService.saveAs(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRIP_FULL_ACCESS)
	@PostMapping("/save-all")
	public ResponseEntity<List<TripResponse>> saveAll(@RequestBody List<TripRequest> requests){
		List<TripResponse> items = tripService.saveAll(requests);
		return ResponseEntity.ok(items);
	}
}
