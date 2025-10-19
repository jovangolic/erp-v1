package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.SecuritySettingErrorException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.SecuritySettingMapper;
import com.jovan.erp_v1.model.SecuritySetting;
import com.jovan.erp_v1.repository.SecuritySettingRepository;
import com.jovan.erp_v1.request.SecuritySettingRequest;
import com.jovan.erp_v1.response.SecuritySettingResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecuritySettingService implements ISecuritySettingService {

    private final SecuritySettingRepository settingRepository;

    @Override
    public SecuritySettingResponse getByName(String name) {
        SecuritySetting setting = settingRepository.findBySettingName(name)
                .orElseThrow(() -> new SecuritySettingErrorException("Setting not found with name: " + name));
        return SecuritySettingMapper.toResponse(setting);
    }

    @Override
    public SecuritySettingResponse updateSetting(Long id, SecuritySettingRequest request) {
    	if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
        SecuritySetting setting = settingRepository.findBySettingName(request.settingName())
                .orElseThrow(() -> new SecuritySettingErrorException(
                        "Setting not found with name: " + request.settingName()));
        validateString(request.settingName());
        validateString(request.value());
        setting.setValue(request.value());
        SecuritySetting updated = settingRepository.save(setting);
        return SecuritySettingMapper.toResponse(updated);
    }

    @Override
    public List<SecuritySettingResponse> getAllSettings() {
        List<SecuritySetting> settings = settingRepository.findAll();
        List<SecuritySetting> items = settingRepository.findAll();
        if(items.isEmpty()) {
        	throw new NoDataFoundException("SecuritySetting list is empty");
        }
        return settings.stream()
                .map(SecuritySettingMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @Override
	public SecuritySettingResponse saveSecuritySettings(SecuritySettingRequest request) {
		SecuritySetting sec = SecuritySetting.builder()
				.id(request.id())
				.settingName(request.settingName())
				.value(request.value())
				.build();
		SecuritySetting saved = settingRepository.save(sec);
		return new SecuritySettingResponse(saved.getId(), saved.getSettingName(), saved.getValue());
	}
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new IllegalArgumentException("String must not be null nor empty");
    	}
    }

}
