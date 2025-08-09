package com.jovan.erp_v1.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.SystemStatus;
import com.jovan.erp_v1.request.SystemStateRequest;
import com.jovan.erp_v1.response.SystemStateResponse;
import com.jovan.erp_v1.service.ISystemStateService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system-states")
@PreAuthorize(RoleGroups.SYSTEM_STATE_FULL_ACCESS)
public class SystemStateController {

    private final ISystemStateService service;

    @GetMapping("/current-state")
    public ResponseEntity<SystemStateResponse> getCurrentState() {
        return ResponseEntity.ok(service.getCurrentState());
    }

    @PutMapping
    public ResponseEntity<Void> updateState(@Valid @RequestBody SystemStateRequest request) {
        service.updateState(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/restart")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restartSystem() {
        service.updateRestartTime();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/maintenance-mode")
    public ResponseEntity<Void> toggleMaintenance(@RequestParam boolean enabled) {
        service.setMaintenanceMode(enabled);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/registration-enabled")
    public ResponseEntity<Void> toggleRegistrationEnabled(@RequestParam boolean enabled) {
        service.setRegistrationEnabled(enabled);
        return ResponseEntity.ok().build();
    }
    
    //nove metode
    
    @GetMapping("/search/by-status-message")
    public ResponseEntity<List<SystemStateResponse>> findByStatusMessage(@RequestParam("statusMessage") SystemStatus statusMessage){
    	List<SystemStateResponse> responses = service.findByStatusMessage(statusMessage);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-status-running")
    public ResponseEntity<List<SystemStateResponse>> findRunning(){
    	List<SystemStateResponse> responses = service.findRunning();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-status-maintenance")
    public ResponseEntity<List<SystemStateResponse>> findMaintenance(){
    	List<SystemStateResponse> responses = service.findMaintenance();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-status-offline")
    public ResponseEntity<List<SystemStateResponse>> findOffline(){
    	List<SystemStateResponse> responses = service.findOffline();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-status-restarting")
    public ResponseEntity<List<SystemStateResponse>> findRestarting(){
    	List<SystemStateResponse> responses = service.findRestarting();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/exists-status-message")
    public ResponseEntity<Boolean> existsByStatusMessage(@RequestParam("statusMessage") SystemStatus statusMessage){
    	Boolean responses = service.existsByStatusMessage(statusMessage);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/count-status-message")
    public ResponseEntity<Long> countByStatusMessage(@RequestParam("statusMessage") SystemStatus statusMessage){
    	Long responses = service.countByStatusMessage(statusMessage);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/by-system-version")
    public ResponseEntity<List<SystemStateResponse>> findBySystemVersion(@RequestParam("systemVersion") String systemVersion){
    	List<SystemStateResponse> responses = service.findBySystemVersion(systemVersion);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/last-restart-time")
    public ResponseEntity<List<SystemStateResponse>> findByLastRestartTime(@RequestParam("lastRestartTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastRestartTime){
    	List<SystemStateResponse> responses = service.findByLastRestartTime(lastRestartTime);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/exists-by-maintenance-mode")
    public ResponseEntity<Boolean> existsByMaintenanceMode(){
    	Boolean responses = service.existsByMaintenanceMode();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/exists-by-registration-enabled")
    public ResponseEntity<Boolean> existsByRegistrationEnabled(){
    	Boolean responses = service.existsByRegistrationEnabled();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/status-message-and-system-version")
    public ResponseEntity<List<SystemStateResponse>> findByStatusMessageAndSystemVersion(@RequestParam("statusMessage") SystemStatus statusMessage,
    		@RequestParam("systemVersion") String systemVersion){
    	List<SystemStateResponse> responses = service.findByStatusMessageAndSystemVersion(statusMessage, systemVersion);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/registration-enabled-and-maintenance-mode")
    public ResponseEntity<List<SystemStateResponse>> findByRegistrationEnabledTrueAndMaintenanceModeFalse(){
    	List<SystemStateResponse> responses = service.findByRegistrationEnabledTrueAndMaintenanceModeFalse();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/last-restart-time-desc")
    public ResponseEntity<SystemStateResponse> findTopByOrderByLastRestartTimeDesc(){
    	SystemStateResponse responses = service.findTopByOrderByLastRestartTimeDesc();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/count/maintenance-true")
    public ResponseEntity<Long> countByMaintenanceModeTrue() {
        Long responses = service.countByMaintenanceModeTrue();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/count/registration-enabled-true")
    public ResponseEntity<Long> countByRegistrationEnabledTrue() {
        Long responses = service.countByRegistrationEnabledTrue();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/exists-status-message-and-system-version")
    public ResponseEntity<Boolean> existsByStatusMessageAndSystemVersion(@RequestParam("statusMessage") SystemStatus statusMessage,
    		@RequestParam("systemVersion") String systemVersion){
    	Boolean responses = service.existsByStatusMessageAndSystemVersion(statusMessage, systemVersion);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/last-restart-time-after")
    public ResponseEntity<List<SystemStateResponse>> findByLastRestartTimeAfter(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
    	List<SystemStateResponse> responses = service.findByLastRestartTimeAfter(time);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/last-restart-time-between")
    public ResponseEntity<List<SystemStateResponse>> findByLastRestartTimeBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
    	List<SystemStateResponse> responses = service.findByLastRestartTimeBetween(start, end);
    	return ResponseEntity.ok(responses);
    }
 }
