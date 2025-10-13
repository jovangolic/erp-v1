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

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;
import com.jovan.erp_v1.save_as.DriverSaveAsRequest;
import com.jovan.erp_v1.search_request.DriverSearchRequest;
import com.jovan.erp_v1.service.IDriverService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
@PreAuthorize(RoleGroups.DRIVER_READ_ACCESS)
public class DriverController {

    private final IDriverService driverService;

    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/create/new-driver")
    public ResponseEntity<DriverResponse> create(@Valid @RequestBody DriverRequest request) {
        DriverResponse response = driverService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<DriverResponse> update(@PathVariable Long id, @Valid @RequestBody DriverRequest request) {
        DriverResponse response = driverService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.DRIVER_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<DriverResponse> findOneById(@PathVariable Long id) {
        DriverResponse response = driverService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.DRIVER_READ_ACCESS)
    @GetMapping("find-all")
    public ResponseEntity<List<DriverResponse>> findAllDrivers() {
        List<DriverResponse> responses = driverService.findAllDrivers();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.DRIVER_READ_ACCESS)
    @GetMapping("/by-phone")
    public ResponseEntity<DriverResponse> findByPhone(@RequestParam String phone) {
        DriverResponse response = driverService.findByPhone(phone);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_READ_ACCESS)
    @GetMapping("/first-name-and-last-name")
    public ResponseEntity<List<DriverResponse>> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(@RequestParam("firstName") String firstName,
    		@RequestParam("lastName") String lastName){
    	List<DriverResponse> items = driverService.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_READ_ACCESS)
    @GetMapping("/phone-fragment")
    public ResponseEntity<List<DriverResponse>> findByPhoneLikeIgnoreCase(@RequestParam("phone") String phone){
    	List<DriverResponse> items = driverService.findByPhoneLikeIgnoreCase(phone);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/general-search")
    public ResponseEntity<List<DriverResponse>> generalSearch(@RequestBody DriverSearchRequest request){
    	List<DriverResponse> items = driverService.generalSearch(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_READ_ACCESS)
    @GetMapping("/exists/first-name-and-last-name")
    public ResponseEntity<Boolean> existsByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(@RequestParam("firstName") String firstName,
    		@RequestParam("lastName") String lastName){
    	Boolean items = driverService.existsByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/{id}/confirm")
    public ResponseEntity<DriverResponse> confirmDriver(@PathVariable Long id){
    	DriverResponse items = driverService.confirmDriver(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/{id}/close")
    public ResponseEntity<DriverResponse> closeDriver(@PathVariable Long id){
    	DriverResponse items = driverService.closeDriver(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<DriverResponse> cancelDriver(@PathVariable Long id){
    	DriverResponse items = driverService.cancelDriver(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<DriverResponse> changeStatus(@PathVariable Long id,@PathVariable  DriverStatus newStatus){
    	return ResponseEntity.ok(driverService.changeStatus(id, newStatus));
    }
    
    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/save")
    public ResponseEntity<DriverResponse> saveDriver(@Valid @RequestBody DriverRequest request){
    	DriverResponse items = driverService.saveDriver(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/save-as")
    public ResponseEntity<DriverResponse> saveAs(@Valid @RequestBody DriverSaveAsRequest request){
    	DriverResponse items = driverService.saveAs(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_FULL_ACCESS)
    @PostMapping("/save-all")
    public ResponseEntity<List<DriverResponse>> saveAll(@RequestBody List<DriverRequest> requests){
    	List<DriverResponse> items = driverService.saveAll(requests);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.DRIVER_READ_ACCESS)
    @GetMapping("/track/{id}")
    public ResponseEntity<DriverResponse> trackDriver(@PathVariable Long id){
    	DriverResponse items = driverService.trackDriver(id);
    	return ResponseEntity.ok(items);
    }
}
