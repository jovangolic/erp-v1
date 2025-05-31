package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.SecuritySetting;
import com.jovan.erp_v1.request.SecuritySettingRequest;
import com.jovan.erp_v1.response.SecuritySettingResponse;

@Component
public class SecuritySettingMapper {

    public static SecuritySettingResponse toResponse(SecuritySetting setting) {
        return new SecuritySettingResponse(
                setting.getId(),
                setting.getSettingName(),
                setting.getValue());
    }

    public static SecuritySetting fromRequest(SecuritySettingRequest request) {
        SecuritySetting setting = new SecuritySetting();
        setting.setSettingName(request.settingName());
        setting.setValue(request.value());
        return setting;
    }
}
