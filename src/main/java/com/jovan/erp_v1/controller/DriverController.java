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

import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;
import com.jovan.erp_v1.service.IDriverService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
public class DriverController {

    private final IDriverService driverService;

    @PostMapping("/create/new-driver")
    public ResponseEntity<DriverResponse> create(@Valid @RequestBody DriverRequest request) {
        DriverResponse response = driverService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DriverResponse> update(@PathVariable Long id, @Valid @RequestBody DriverRequest request) {
        DriverResponse response = driverService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<DriverResponse> findOneById(@PathVariable Long id) {
        DriverResponse response = driverService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("find-all")
    public ResponseEntity<List<DriverResponse>> findAllDrivers() {
        List<DriverResponse> responses = driverService.findAllDrivers();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<DriverResponse>> findByName(@RequestParam("name") String name) {
        List<DriverResponse> responses = driverService.findByName(name);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-phone")
    public ResponseEntity<DriverResponse> findByPhone(@RequestParam String phone) {
        DriverResponse response = driverService.findByPhone(phone);
        return ResponseEntity.ok(response);
    }
}
