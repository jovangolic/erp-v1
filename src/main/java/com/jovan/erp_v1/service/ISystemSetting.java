package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.SystemSettingCreateRequest;
import com.jovan.erp_v1.request.SystemSettingUpdateRequest;
import com.jovan.erp_v1.response.SystemSettingResponse;

public interface ISystemSetting {

    List<SystemSettingResponse> getAll();

    SystemSettingResponse getByKey(String key);

    SystemSettingResponse create(SystemSettingCreateRequest request);

    SystemSettingResponse update(SystemSettingUpdateRequest request);

    void delete(Long id);
}
