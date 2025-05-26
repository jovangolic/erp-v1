package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.SystemSetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingResponse {

    private Long id;
    private String key;
    private String value;
    private String description;

    public SystemSettingResponse(SystemSetting setting) {
        this.id = setting.getId();
        this.key = setting.getKey();
        this.value = setting.getValue();
        this.description = setting.getDescription();
    }
}
