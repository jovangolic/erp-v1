package com.jovan.erp_v1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.EmailSettingNotFoundException;
import com.jovan.erp_v1.mapper.EmailSettingMapper;
import com.jovan.erp_v1.model.EmailSetting;
import com.jovan.erp_v1.repository.EmailSettingRepository;
import com.jovan.erp_v1.request.EmailSettingRequest;
import com.jovan.erp_v1.response.EmailSettingResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSettingService implements IEmailSettingService {

    private final EmailSettingRepository emailSettingRepository;
    private final EmailSettingMapper emailSettingMapper;

    @Override
    public EmailSettingResponse getCurrentSettings() {
        EmailSetting email = emailSettingRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new EmailSettingNotFoundException("Email not found"));
        return emailSettingMapper.toResponse(email);
    }

    @Transactional
    @Override
    public EmailSettingResponse updateSettings(EmailSettingRequest request) {
        EmailSetting email = emailSettingMapper.toEntity(request);
        return emailSettingMapper.toResponse(emailSettingRepository.save(email));
    }
}
