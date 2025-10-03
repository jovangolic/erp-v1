package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.VehicleLocationRequest;
import com.jovan.erp_v1.response.VehicleLocationResponse;
import com.jovan.erp_v1.response.VehicleResponse;
import com.jovan.erp_v1.save_as.VehicleLocationSaveAsRequest;
import com.jovan.erp_v1.search_request.VehicleLocationSearchRequest;
import com.jovan.erp_v1.service.IVehicleLocationService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicle-locations")
@PreAuthorize(RoleGroups.VEHICLE_lOCATION_READ_ACCESS)
public class VehicleLocationController {

	private final IVehicleLocationService vehicleLocationService;
	
	@PreAuthorize(RoleGroups.VEHICLE_LOCATION_FULL_ACCESS)
	@PostMapping("/create/new-vehicle-location")
	public ResponseEntity<VehicleLocationResponse> create(@Valid @RequestBody VehicleLocationRequest request){
		VehicleLocationResponse items = vehicleLocationService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_LOCATION_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<VehicleLocationResponse> update(@PathVariable Long id, @Valid @RequestBody VehicleLocationRequest request){
		VehicleLocationResponse items = vehicleLocationService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_LOCATION_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		vehicleLocationService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_lOCATION_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<VehicleLocationResponse> findOne(@PathVariable Long id){
		VehicleLocationResponse items = vehicleLocationService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_lOCATION_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<VehicleLocationResponse>> findAll(){
		List<VehicleLocationResponse> items = vehicleLocationService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_lOCATION_READ_ACCESS)
	@GetMapping("/by-time")
	public ResponseEntity<List<VehicleLocationResponse>> findLocationsByTimeRange(
			@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
		List<VehicleLocationResponse> items = vehicleLocationService.findLocationsByTimeRange(from, to);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_lOCATION_READ_ACCESS)
	@GetMapping("/vehicles-by-time")
	public ResponseEntity<List<VehicleResponse>> findVehiclesByLocationTimeRange(
			@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
		List<VehicleResponse> items = vehicleLocationService.findVehiclesByLocationTimeRange(from, to);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_LOCATION_FULL_ACCESS)
	@PostMapping("/search")
	public ResponseEntity<List<VehicleLocationResponse>> findLocationsByRequest(@Valid @RequestBody VehicleLocationSearchRequest req){
		List<VehicleLocationResponse> items = vehicleLocationService.findLocationsByRequest(req);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_LOCATION_FULL_ACCESS)
	@PostMapping("/general-search")
	public ResponseEntity<List<VehicleLocationResponse>> generalSearch(@Valid @RequestBody VehicleLocationSearchRequest req){
		List<VehicleLocationResponse> items = vehicleLocationService.generalSearch(req);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_LOCATION_FULL_ACCESS)
	@PostMapping("/save")
	public ResponseEntity<VehicleLocationResponse> saveVehicleLocation(@Valid @RequestBody VehicleLocationRequest request){
		VehicleLocationResponse items = vehicleLocationService.saveVehicleLocation(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_LOCATION_FULL_ACCESS)
	@PostMapping("/save-as")
	public ResponseEntity<VehicleLocationResponse> saveAs(@Valid @RequestBody VehicleLocationSaveAsRequest req){
		VehicleLocationResponse items = vehicleLocationService.saveAs(req);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.VEHICLE_LOCATION_FULL_ACCESS)
	@PostMapping("/save-all")
	public ResponseEntity<List<VehicleLocationResponse>> saveAll(@Valid @RequestBody List<VehicleLocationRequest> requests){
		List<VehicleLocationResponse> items = vehicleLocationService.saveAll(requests);
		return ResponseEntity.ok(items);
	}
}

