package com.jovan.erp_v1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.SystemStatus;
import com.jovan.erp_v1.model.SystemState;

@Repository
public interface SystemStateRepository extends JpaRepository<SystemState, Long> {

    Optional<SystemState> findTopByOrderByIdDesc();
    
    //nove metode
    List<SystemState> findByStatusMessage(SystemStatus statusMessage);
    @Query("SELECT s FROM SystemState s WHERE s.statusMessage = 'RUNNING' ")
    List<SystemState> findRunning();
    @Query("SELECT s FROM SystemState s WHERE s.statusMessage = 'MAINTENANCE' ")
    List<SystemState> findMaintenance();
    @Query("SELECT s FROM SystemState s WHERE s.statusMessage = 'OFFLINE' ")
    List<SystemState> findOffline();
    @Query("SELECT s FROM SystemState s WHERE s.statusMessage = 'RESTARTING' ")
    List<SystemState> findRestarting();
    Boolean existsByStatusMessage(SystemStatus statusMessage);
    @Query("SELECT COUNT(ss) FROM SystemState ss WHERE ss.statusMessage = :status")
    Long countByStatusMessage(@Param("status") SystemStatus statusMessage);
    List<SystemState> findBySystemVersion(String systemVersion);
    List<SystemState> findByLastRestartTime(LocalDateTime lastRestartTime);
    Boolean existsByMaintenanceModeTrue();
    Boolean existsByRegistrationEnabledTrue();
    List<SystemState> findByStatusMessageAndSystemVersion(SystemStatus statusMessage, String systemVersion);
    @Query("SELECT s FROM SystemState s WHERE s.registrationEnabled  IS TRUE AND s.maintenanceMode IS FALSE")
    List<SystemState> findByRegistrationEnabledTrueAndMaintenanceModeFalse();
    Optional<SystemState> findTopByOrderByLastRestartTimeDesc();
    Long countByMaintenanceModeTrue();
    Long countByRegistrationEnabledTrue();
    Boolean existsByStatusMessageAndSystemVersion(SystemStatus statusMessage, String systemVersion);
    List<SystemState> findByLastRestartTimeAfter(LocalDateTime time);
    List<SystemState> findByLastRestartTimeBetween(LocalDateTime start, LocalDateTime end);
}
