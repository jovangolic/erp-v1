package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.SecuritySetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecuritySettingResponse {

    private Long id;
    private String settingName;
    private String value;

    public SecuritySettingResponse(SecuritySetting setting) {
        this.id = setting.getId();
        this.settingName = setting.getSettingName();
        this.value = setting.getValue();
    }
}
