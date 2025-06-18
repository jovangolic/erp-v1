package com.jovan.erp_v1.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.SystemStatus;
import com.jovan.erp_v1.exception.SystemStateErrorException;
import com.jovan.erp_v1.mapper.SystemStateMapper;
import com.jovan.erp_v1.model.SystemState;
import com.jovan.erp_v1.repository.SystemStateRepository;
import com.jovan.erp_v1.request.SystemStateRequest;
import com.jovan.erp_v1.response.SystemStateResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemStateService implements ISystemStateService {

    private final SystemStateRepository systemStateRepository;
    private final SystemStateMapper mapper;

    @Override
    public SystemStateResponse getCurrentState() {
        SystemState state = systemStateRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new SystemStateErrorException("System-state not found"));
        return mapper.toResponse(state);
    }

    @Override
    public void updateState(SystemStateRequest request) {
        SystemState currentState = systemStateRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new SystemStateErrorException("No system state found."));
        currentState.setMaintenanceMode(request.maintenanceMode());
        currentState.setRegistrationEnabled(request.registrationEnabled());
        currentState.setSystemVersion(request.systemVersion());
        currentState.setStatusMessage(request.statusMessage());

        systemStateRepository.save(currentState);
    }

    @Override
    @Transactional
    public void updateRestartTime() {
        SystemState state = systemStateRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new SystemStateErrorException("System state not initialized"));
        state.setLastRestartTime(LocalDateTime.now());
        state.setStatusMessage(SystemStatus.RESTARTING); // ako koristiš enum
        systemStateRepository.save(state);
    }

    @Override
    public void setMaintenanceMode(boolean enabled) {
        SystemState state = systemStateRepository.findTopByOrderByIdDesc().orElseThrow();
        state.setMaintenanceMode(enabled);
        systemStateRepository.save(state);
    }

    @Override
    public void setRegistrationEnabled(boolean enabled) {
        SystemState state = systemStateRepository.findTopByOrderByIdDesc().orElseThrow();
        state.setRegistrationEnabled(enabled);
        systemStateRepository.save(state);
    }
}
