package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.SystemSetting;
import com.jovan.erp_v1.request.SystemSettingRequest;
import com.jovan.erp_v1.response.SystemSettingResponse;

@Component
public class SystemSettingMapper {

    public SystemSettingResponse toResponse(SystemSetting setting) {
        return new SystemSettingResponse(
                setting.getId(),
                setting.getKey(),
                setting.getValue(),
                setting.getDescription());
    }

    public SystemSetting toEntity(SystemSettingRequest request) {
        SystemSetting setting = new SystemSetting();
        setting.setKey(request.key());
        setting.setValue(request.value());
        return setting;
    }
}
