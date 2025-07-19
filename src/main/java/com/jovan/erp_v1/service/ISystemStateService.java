package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.SystemStatus;
import com.jovan.erp_v1.request.SystemStateRequest;
import com.jovan.erp_v1.response.SystemStateResponse;

public interface ISystemStateService {

    SystemStateResponse getCurrentState();
    void updateState(SystemStateRequest request);
    void updateRestartTime();
    void setMaintenanceMode(boolean enabled);
    void setRegistrationEnabled(boolean enabled);
    
    //nove metode
    List<SystemStateResponse> findByStatusMessage(SystemStatus statusMessage);
    List<SystemStateResponse> findRunning();
    List<SystemStateResponse> findMaintenance();
    List<SystemStateResponse> findOffline();
    List<SystemStateResponse> findRestarting();
    Boolean existsByStatusMessage(SystemStatus statusMessage);
    Long countByStatusMessage( SystemStatus statusMessage);
    List<SystemStateResponse> findBySystemVersion(String systemVersion);
    List<SystemStateResponse> findByLastRestartTime(LocalDateTime lastRestartTime);
    Boolean existsByMaintenanceMode();
    Boolean existsByRegistrationEnabled();
    List<SystemStateResponse> findByStatusMessageAndSystemVersion(SystemStatus statusMessage, String systemVersion);
    List<SystemStateResponse> findByRegistrationEnabledTrueAndMaintenanceModeFalse();
    SystemStateResponse findTopByOrderByLastRestartTimeDesc();
    Long countByMaintenanceModeTrue();
    Long countByRegistrationEnabledTrue();
    Boolean existsByStatusMessageAndSystemVersion(SystemStatus statusMessage, String systemVersion);
    List<SystemStateResponse> findByLastRestartTimeAfter(LocalDateTime time);
    List<SystemStateResponse> findByLastRestartTimeBetween(LocalDateTime start, LocalDateTime end);
}
