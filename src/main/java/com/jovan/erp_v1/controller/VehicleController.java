package com.jovan.erp_v1.controller;

import java.util.List;

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

import com.jovan.erp_v1.enumeration.VehicleFuel;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.request.VehicleRequest;
import com.jovan.erp_v1.response.VehicleResponse;
import com.jovan.erp_v1.service.IVehicleService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
@PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
public class VehicleController {

    private final IVehicleService vehicleService;

    @PreAuthorize(RoleGroups.VEHICLE_FULL_ACCESS)
    @PostMapping("/create/new-vehicle")
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.VEHICLE_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<VehicleResponse> update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.VEHICLE_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<VehicleResponse> findOneById(@PathVariable Long id) {
        VehicleResponse response = vehicleService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/find-all-vehicles")
    public ResponseEntity<List<VehicleResponse>> findAll() {
        List<VehicleResponse> responses = vehicleService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/find-by-model")
    public ResponseEntity<List<VehicleResponse>> findByModel(@RequestParam("model") String model) {
        List<VehicleResponse> response = vehicleService.findByModel(model);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/by-registration-number")
    public ResponseEntity<VehicleResponse> findByRegistrationNumber(@RequestParam String registrationNumber) {
        VehicleResponse response = vehicleService.findByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/status")
    public ResponseEntity<List<VehicleResponse>> findByStatus(@RequestParam("status") VehicleStatus status) {
        List<VehicleResponse> responses = vehicleService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/filter-by-model-and-status")
    public ResponseEntity<List<VehicleResponse>> findByModelAndStatus(@RequestParam("model") String model,
            @RequestParam("status") VehicleStatus status) {
        List<VehicleResponse> responses = vehicleService.findByModelAndStatus(model, status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/search")
    public ResponseEntity<List<VehicleResponse>> search(@RequestParam("keyword") String keyword) {
        List<VehicleResponse> responses = vehicleService.search(keyword);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/filter")
    public ResponseEntity<List<VehicleResponse>> filterVehicles(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) VehicleStatus status) {
        List<VehicleResponse> responses = vehicleService.filterVehicles(model,
                status != null ? status.name() : null);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/modelFragment")
    public ResponseEntity<List<VehicleResponse>> findByModelContainingIgnoreCase(
            @RequestParam("modelFragment") String modelFragment) {
        List<VehicleResponse> responses = vehicleService.findByModelContainingIgnoreCase(modelFragment);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/by-fuel")
    public ResponseEntity<List<VehicleResponse>> findByFuel(@RequestParam("fuel") VehicleFuel fuel){
    	List<VehicleResponse> items = vehicleService.findByFuel(fuel);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.VEHICLE_READ_ACCESS)
    @GetMapping("/model-and-fuel")
    public ResponseEntity<List<VehicleResponse>> findByModelContainingIgnoreCaseAndFuel(@RequestParam("model") String model,
    		@RequestParam("fuel") VehicleFuel fuel){
    	List<VehicleResponse> items = vehicleService.findByModelContainingIgnoreCaseAndFuel(model, fuel);
    	return ResponseEntity.ok(items);
    }
}
