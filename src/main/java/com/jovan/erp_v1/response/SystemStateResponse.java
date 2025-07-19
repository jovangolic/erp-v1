package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.SystemStatus;
import com.jovan.erp_v1.model.SystemState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemStateResponse {

    private Long id;
    private Boolean maintenanceMode;
    private Boolean registrationEnabled;
    private LocalDateTime lastRestartTime;
    private String systemVersion;
    private SystemStatus statusMessage;

    public SystemStateResponse(SystemState systemState) {
        this.id = systemState.getId();
        this.maintenanceMode = systemState.getMaintenanceMode();
        this.registrationEnabled = systemState.getRegistrationEnabled();
        this.lastRestartTime = systemState.getLastRestartTime();
        this.systemVersion = systemState.getSystemVersion();
        this.statusMessage = systemState.getStatusMessage();
    }
}
