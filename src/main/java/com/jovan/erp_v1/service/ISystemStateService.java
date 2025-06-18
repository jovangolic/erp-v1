package com.jovan.erp_v1.service;

import com.jovan.erp_v1.request.SystemStateRequest;
import com.jovan.erp_v1.response.SystemStateResponse;

public interface ISystemStateService {

    SystemStateResponse getCurrentState();

    void updateState(SystemStateRequest request);

    void updateRestartTime();

    void setMaintenanceMode(boolean enabled);

    void setRegistrationEnabled(boolean enabled);
}
