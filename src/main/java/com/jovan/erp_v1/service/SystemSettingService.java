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

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemSettingService implements ISystemSetting {

    private final SystemSettingRepository settingRepository;

    @Override
    public List<SystemSettingResponse> getAll() {
        return settingRepository.findAll()
                .stream()
                .map(SystemSettingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemSettingResponse> getByCategory(String category) {
        return settingRepository.findAllByCategory(category)
                .stream()
                .map(SystemSettingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public SystemSettingResponse getByKey(String key) {
        SystemSetting setting = settingRepository.findByKey(key)
                .orElseThrow(() -> new SystemSettingErrorNotFoundException("Setting not found for key: " + key));
        return new SystemSettingResponse(setting);
    }

    @Transactional
    @Override
    public SystemSettingResponse create(SystemSettingCreateRequest request) {
        if (settingRepository.existsByKey(request.key())) {
            throw new IllegalArgumentException("Setting with this key already exists.");
        }

        SystemSetting setting = new SystemSetting();
        setting.setKey(request.key());
        setting.setValue(request.value());
        setting.setDescription(request.description());
        setting.setCategory(request.category());
        setting.setDataType(request.dataType());
        setting.setEditable(request.editable() != null ? request.editable() : true);
        setting.setIsVisible(request.isVisible() != null ? request.isVisible() : true);
        setting.setDefaultValue(request.defaultValue());

        return new SystemSettingResponse(settingRepository.save(setting));
    }

    @Transactional
    @Override
    public SystemSettingResponse update(SystemSettingUpdateRequest request) {
        SystemSetting setting = settingRepository.findById(request.id())
                .orElseThrow(() -> new SystemSettingErrorNotFoundException("Setting not found"));

        setting.setValue(request.value());
        setting.setDescription(request.description());
        setting.setCategory(request.category());
        setting.setDataType(request.dataType());
        setting.setEditable(request.editable());
        setting.setIsVisible(request.isVisible());
        setting.setDefaultValue(request.defaultValue());

        return new SystemSettingResponse(settingRepository.save(setting));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!settingRepository.existsById(id)) {
            throw new SystemSettingErrorNotFoundException("Setting not found with id: " + id);
        }
        settingRepository.deleteById(id);
    }
}
