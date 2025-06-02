package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.EmailSetting;
import com.jovan.erp_v1.request.EmailSettingRequest;
import com.jovan.erp_v1.response.EmailSettingResponse;

@Component
public class EmailSettingMapper {

    public EmailSetting toEntity(EmailSettingRequest request) {
        EmailSetting email = new EmailSetting();
        email.setSmtpServer(request.smtpServer());
        email.setSmtpPort(request.smtpPort());
        email.setFromEmail(request.fromEmail());
        email.setFromName(request.fromName());
        email.setEmailPassword(request.emailPassword());
        return email;
    }

    public EmailSettingResponse toResponse(EmailSetting email) {
        return new EmailSettingResponse(
                email.getId(),
                email.getSmtpServer(),
                email.getSmtpPort(),
                email.getFromEmail(),
                email.getFromName());
    }

    public List<EmailSettingResponse> toResponseList(List<EmailSetting> emails) {
        return emails.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
