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
        SystemState state = systemStateRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new SystemStateErrorException("System state not found."));
        mapper.updateFromRequest(state, request);
        systemStateRepository.save(state);
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
}
