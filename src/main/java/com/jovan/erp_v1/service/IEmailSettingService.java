package com.jovan.erp_v1.service;

import com.jovan.erp_v1.request.EmailSettingRequest;
import com.jovan.erp_v1.response.EmailSettingResponse;

public interface IEmailSettingService {

    EmailSettingResponse getCurrentSettings();

    EmailSettingResponse updateSettings(EmailSettingRequest request);
}
