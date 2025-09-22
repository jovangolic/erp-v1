package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.SecuritySettingRequest;
import com.jovan.erp_v1.response.SecuritySettingResponse;

public interface ISecuritySettingService {

    SecuritySettingResponse getByName(String name);

    SecuritySettingResponse updateSetting(SecuritySettingRequest request);

    List<SecuritySettingResponse> getAllSettings();
    
    SecuritySettingResponse saveSecuritySettings(SecuritySettingRequest request);
}
