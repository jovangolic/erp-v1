package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.SettingDataType;
import com.jovan.erp_v1.enumeration.SystemSettingCategory;
import com.jovan.erp_v1.model.SystemSetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingResponse {

    private Long id;
    private String settingKey;
    private String value;
    private String description;
    private SystemSettingCategory category;
    private SettingDataType dataType;
    private Boolean editable;
    private Boolean isVisible;
    private String defaultValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SystemSettingResponse(SystemSetting setting) {
        this.id = setting.getId();
        this.settingKey = setting.getSettingKey();
        this.value = setting.getValue();
        this.description = setting.getDescription();
        this.category = setting.getCategory();
        this.dataType = setting.getDataType();
        this.editable = setting.getEditable();
        this.isVisible = setting.getIsVisible();
        this.defaultValue = setting.getDefaultValue();
        this.createdAt = setting.getCreatedAt();
        this.updatedAt = setting.getUpdatedAt();
    }
}
