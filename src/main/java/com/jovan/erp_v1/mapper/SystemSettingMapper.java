package com.jovan.erp_v1.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.model.SystemSetting;
import com.jovan.erp_v1.request.SystemSettingCreateRequest;
import com.jovan.erp_v1.request.SystemSettingRequest;
import com.jovan.erp_v1.request.SystemSettingUpdateRequest;
import com.jovan.erp_v1.response.SystemSettingResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class SystemSettingMapper extends AbstractMapper<SystemSettingRequest> {

    public SystemSettingResponse toResponse(SystemSetting setting) {
    	Objects.requireNonNull(setting, "SystemSetting must not be null");
        return new SystemSettingResponse(
                setting.getId(),
                setting.getSettingKey(),
                setting.getValue(),
                setting.getDescription(),
                setting.getCategory(),
                setting.getDataType(),
                setting.getEditable(),
                setting.getIsVisible(),
                setting.getDefaultValue(),
                setting.getCreatedAt(),
                setting.getUpdatedAt());
    }

    public SystemSetting toEntity(SystemSettingRequest request) {
    	Objects.requireNonNull(request, "SystemSettingRequest must not be null");
    	validateIdForCreate(request, SystemSettingRequest::id); //provera id
        SystemSetting setting = new SystemSetting();
        setting.setSettingKey(request.settingKey());
        setting.setValue(request.value());
        setting.setDescription(request.description());
        setting.setCategory(request.category());
        setting.setDataType(request.dataType());
        setting.setEditable(request.editable() != null ? request.editable() : true);
        setting.setIsVisible(request.isVisible() != null ? request.isVisible() : true);
        setting.setDefaultValue(request.defaultValue());
        return setting;
    }
    
    public SystemSetting toEntity(SystemSettingCreateRequest request) {
    	Objects.requireNonNull(request, "SystemSettingRequest must not be null");
    	if(request.id() != null) {
    		throw new ValidationException("Id must be null while being created");
    	}
        SystemSetting setting = new SystemSetting();
        setting.setSettingKey(request.settingKey());
        setting.setValue(request.value());
        setting.setDescription(request.description());
        setting.setCategory(request.category());
        setting.setDataType(request.dataType());
        setting.setEditable(request.editable() != null ? request.editable() : true);
        setting.setIsVisible(request.isVisible() != null ? request.isVisible() : true);
        setting.setDefaultValue(request.defaultValue());
        return setting;
    }
    
    public SystemSetting toEntityUpdate(SystemSetting setting, SystemSettingRequest request) {
    	Objects.requireNonNull(setting, "SystemSetting must not be null");
    	Objects.requireNonNull(request, "SystemSettingRequest must not be null");
    	validateIdForUpdate(request, SystemSettingRequest::id);
    	setting.setSettingKey(request.settingKey());
        setting.setValue(request.value());
        setting.setDescription(request.description());
        setting.setCategory(request.category());
        setting.setDataType(request.dataType());
        setting.setEditable(request.editable() != null ? request.editable() : true);
        setting.setIsVisible(request.isVisible() != null ? request.isVisible() : true);
        setting.setDefaultValue(request.defaultValue());
        return setting;
    }
    
    public SystemSetting toEntityUpdate(SystemSetting setting, SystemSettingUpdateRequest request) {
    	Objects.requireNonNull(setting, "SystemSetting must not be null");
    	Objects.requireNonNull(request, "SystemSettingRequest must not be null");
    	if(request.id() == null) {
    		throw new ValidationException("Id must not be null while being updated");
    	}
        setting.setValue(request.value());
        setting.setDescription(request.description());
        setting.setCategory(request.category());
        setting.setDataType(request.dataType());
        setting.setEditable(request.editable() != null ? request.editable() : true);
        setting.setIsVisible(request.isVisible() != null ? request.isVisible() : true);
        setting.setDefaultValue(request.defaultValue());
        return setting;
    }
}
