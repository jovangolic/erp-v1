package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.ResourceNotFoundException;
import com.jovan.erp_v1.exception.SystemSettingErrorNotFoundException;
import com.jovan.erp_v1.model.SystemSetting;
import com.jovan.erp_v1.repository.SystemSettingRepository;
import com.jovan.erp_v1.request.SystemSettingCreateRequest;
import com.jovan.erp_v1.request.SystemSettingRequest;
import com.jovan.erp_v1.request.SystemSettingUpdateRequest;
import com.jovan.erp_v1.response.SystemSettingResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemSettingService implements ISystemSetting {

    private final SystemSettingRepository settingRepository;

    @Override
    public List<SystemSettingResponse> getAll() {
        return settingRepository.findAll().stream()
                .map(SystemSettingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public SystemSettingResponse getByKey(String key) {
        SystemSetting setting = settingRepository.findByKey(key)
                .orElseThrow(() -> new SystemSettingErrorNotFoundException("System setting not found: " + key));
        return new SystemSettingResponse(setting);
    }

    @Override
    public SystemSettingResponse create(SystemSettingCreateRequest request) {
        if (settingRepository.existsByKey(request.key())) {
            throw new IllegalArgumentException("Setting with the given key already exists.");
        }
        SystemSetting setting = new SystemSetting();
        setting.setKey(request.key());
        setting.setValue(request.value());
        setting.setDescription(""); // ili null ako nije obavezno
        return new SystemSettingResponse(settingRepository.save(setting));
    }

    @Override
    public SystemSettingResponse update(SystemSettingUpdateRequest request) {
        SystemSetting setting = settingRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Setting not found with id: " + request.id()));

        setting.setValue(request.value());

        return new SystemSettingResponse(settingRepository.save(setting));
    }

    @Override
    public void delete(Long id) {
        if (!settingRepository.existsById(id)) {
            throw new SystemSettingErrorNotFoundException("System-setting not found with id: " + id);
        }
        settingRepository.deleteById(id);
    }
}
