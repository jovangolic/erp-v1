package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.SystemStatus;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.SystemStateErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.SystemStateMapper;
import com.jovan.erp_v1.model.SystemState;
import com.jovan.erp_v1.repository.SystemStateRepository;
import com.jovan.erp_v1.request.SystemStateRequest;
import com.jovan.erp_v1.response.SystemStateResponse;
import com.jovan.erp_v1.util.DateValidator;
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
        validateSystemStateRequest(request);
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
    
    //nove metode

	@Override
	public List<SystemStateResponse> findByStatusMessage(SystemStatus statusMessage) {
		validateSystemStatus(statusMessage);
		List<SystemState> items = systemStateRepository.findByStatusMessage(statusMessage);
		if(items.isEmpty()) {
			String msg = String.format("Status message for system-state is not found %s", statusMessage);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<SystemStateResponse> findRunning() {
		List<SystemState> items = systemStateRepository.findRunning();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-state status for 'Running' is not found");
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<SystemStateResponse> findMaintenance() {
		List<SystemState> items = systemStateRepository.findMaintenance();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-state status for 'Maintenance' is not found");
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<SystemStateResponse> findOffline() {
		List<SystemState> items = systemStateRepository.findOffline();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-state status for 'Offline' is not found");
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<SystemStateResponse> findRestarting() {
		List<SystemState> items = systemStateRepository.findRestarting();
		if(items.isEmpty()) {
			throw new NoDataFoundException("System-state status for 'Restarting' is not found");
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public Boolean existsByStatusMessage(SystemStatus statusMessage) {
		validateSystemStatus(statusMessage);
		return systemStateRepository.existsByStatusMessage(statusMessage);
	}

	@Override
	public Long countByStatusMessage(SystemStatus statusMessage) {
		validateSystemStatus(statusMessage);
		Long items = systemStateRepository.countByStatusMessage(statusMessage);
		if(items == null) {
			items = 0L;
		}
		return items;
	}

	@Override
	public List<SystemStateResponse> findBySystemVersion(String systemVersion) {
		validateString(systemVersion);
		List<SystemState> items = systemStateRepository.findBySystemVersion(systemVersion);
		if(items.isEmpty()) {
			String msg = String.format("No system version found %s", systemVersion);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<SystemStateResponse> findByLastRestartTime(LocalDateTime lastRestartTime) {
		DateValidator.validateNotNull(lastRestartTime, "Last restart time");
		List<SystemState> items = systemStateRepository.findByLastRestartTime(lastRestartTime);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("Last restart time for system-state not found %s", lastRestartTime.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public Boolean existsByMaintenanceMode() {
		Boolean result = systemStateRepository.existsByMaintenanceModeTrue();
	    return result != null && result;
	}

	@Override
	public Boolean existsByRegistrationEnabled() {
		Boolean result = systemStateRepository.existsByRegistrationEnabledTrue();
	    return result != null && result;
	}

	@Override
	public List<SystemStateResponse> findByStatusMessageAndSystemVersion(SystemStatus statusMessage,
			String systemVersion) {
		validateSystemStatus(statusMessage);
		validateString(systemVersion);
		List<SystemState> items = systemStateRepository.findByStatusMessageAndSystemVersion(statusMessage, systemVersion);
		if(items.isEmpty()) {
			String msg = String.format("System-state date for status message %s and system version %s not found",
					statusMessage,systemVersion);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<SystemStateResponse> findByRegistrationEnabledTrueAndMaintenanceModeFalse() {
		List<SystemState> items = systemStateRepository.findByRegistrationEnabledTrueAndMaintenanceModeFalse();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No data found for registrationEnabled and maintenanceMode");
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public SystemStateResponse findTopByOrderByLastRestartTimeDesc() {
		SystemState items = systemStateRepository.findTopByOrderByLastRestartTimeDesc()
				.orElseThrow(() -> new NoDataFoundException("Last restart time by descending order is not found"));
		return new SystemStateResponse(items);
	}

	@Override
	public Long countByMaintenanceModeTrue() {
		Long items = systemStateRepository.countByMaintenanceModeTrue();
		if(items == null) {
			items = 0L;
		}
		return items;
	}

	@Override
	public Long countByRegistrationEnabledTrue() {
		Long items = systemStateRepository.countByRegistrationEnabledTrue();
		if(items == null) {
			items = 0L;
		}
		return items;
	}

	@Override
	public Boolean existsByStatusMessageAndSystemVersion(SystemStatus statusMessage, String systemVersion) {
		validateSystemStatus(statusMessage);
		validateString(systemVersion);
		return systemStateRepository.existsByStatusMessageAndSystemVersion(statusMessage, systemVersion);
	}

	@Override
	public List<SystemStateResponse> findByLastRestartTimeAfter(LocalDateTime time) {
		DateValidator.validateNotNull(time, "Last restart time");
		List<SystemState> items = systemStateRepository.findByLastRestartTimeAfter(time);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No restart result found for last restart time after %s", time.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(SystemStateResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<SystemStateResponse> findByLastRestartTimeBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<SystemState> items = systemStateRepository.findByLastRestartTimeBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("Last restart time between %s and %s for system-state is not found",
					start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SystemStateResponse::new)
				.collect(Collectors.toList());
	}
	
	private void validateSystemStateRequest(SystemStateRequest request) {
		if(request == null) {
			throw new ValidationException("System state request must not be null");
		}
		validateBoolean(request.maintenanceMode());
		validateBoolean(request.registrationEnabled());
		validateString(request.systemVersion());
		validateSystemStatus(request.statusMessage());
	}
	
	private void validateSystemStatus(SystemStatus statusMessage) {
		if(statusMessage == null) {
			throw new ValidationException("SystemStatus statusMessage must not be null");
		}
	}
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private void validateBoolean(Boolean bool) {
		if(bool == null) {
			throw new ValidationException("Boolean attribute value must not be null");
		}
	}
}
