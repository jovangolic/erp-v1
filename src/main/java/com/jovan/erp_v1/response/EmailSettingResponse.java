package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.model.EmailSetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSettingResponse {

    private Long id;
    private String smtpServer;
    private String smtpPort;
    private String fromEmail;
    private String fromName;
    private LocalDateTime createdAt;
    private String createdBy;

    public EmailSettingResponse(EmailSetting email) {
        this.id = email.getId();
        this.smtpServer = email.getSmtpServer();
        this.smtpPort = email.getSmtpPort();
        this.fromEmail = email.getFromEmail();
        this.fromName = email.getFromName();
        this.createdAt = email.getCreatedAt();
        this.createdBy = email.getCreatedBy();
    }
}
