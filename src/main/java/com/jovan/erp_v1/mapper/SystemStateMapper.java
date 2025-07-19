package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.SystemState;
import com.jovan.erp_v1.request.SystemStateRequest;
import com.jovan.erp_v1.response.SystemStateResponse;

@Component
public class SystemStateMapper {

    public SystemStateResponse toResponse(SystemState state) {
        SystemStateResponse response = new SystemStateResponse();
        response.setId(state.getId());
        response.setMaintenanceMode(state.getMaintenanceMode());
        response.setRegistrationEnabled(state.getRegistrationEnabled());
        response.setLastRestartTime(state.getLastRestartTime());
        response.setSystemVersion(state.getSystemVersion());
        response.setStatusMessage(state.getStatusMessage());
        return response;
    }

    public void updateFromRequest(SystemState state, SystemStateRequest request) {
        state.setMaintenanceMode(request.maintenanceMode());
        state.setRegistrationEnabled(request.registrationEnabled());
        state.setStatusMessage(request.statusMessage());
    }
}
