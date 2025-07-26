package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.EmailSetting;
import com.jovan.erp_v1.request.EmailSettingRequest;
import com.jovan.erp_v1.response.EmailSettingResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class EmailSettingMapper extends AbstractMapper<EmailSettingRequest> {

    public EmailSetting toEntity(EmailSettingRequest request) {
    	Objects.requireNonNull(request, "EmailSettingRequest ne sme biti null");
    	validateIdForCreate(request, EmailSettingRequest::id);
        EmailSetting email = new EmailSetting();
        email.setSmtpServer(request.smtpServer());
        email.setSmtpPort(request.smtpPort());
        email.setFromEmail(request.fromEmail());
        email.setFromName(request.fromName());
        email.setEmailPassword(request.emailPassword());
        return email;
    }
    
    public EmailSetting toEntityUpdate(EmailSetting email, EmailSettingRequest request) {
    	Objects.requireNonNull(email, "EmailSetting ne sme biti null");
    	Objects.requireNonNull(request, "EmailSettingRequest ne sme biti null");
    	validateIdForUpdate(request, EmailSettingRequest::id);
    	email.setSmtpServer(request.smtpServer());
    	email.setSmtpPort(request.smtpPort());
    	email.setFromEmail(request.fromEmail());
    	email.setFromName(request.fromName());
    	email.setFromName(request.emailPassword());
    	return email;
    }

    public EmailSettingResponse toResponse(EmailSetting email) {
    	Objects.requireNonNull(email, "EmailSetting ne sme biti null");
        return new EmailSettingResponse(email.getId(),
                email.getSmtpServer(),
                email.getSmtpPort(),
                email.getFromEmail(),
                email.getFromName(),
                email.getCreatedAt(),
                email.getCreatedBy());
    }

    public List<EmailSettingResponse> toResponseList(List<EmailSetting> emails) {
    	if(emails == null || emails.isEmpty()) {
    		return Collections.emptyList();
    	}
        return emails.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
