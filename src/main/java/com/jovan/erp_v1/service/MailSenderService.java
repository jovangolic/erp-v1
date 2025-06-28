package com.jovan.erp_v1.service;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.model.EmailSetting;
import com.jovan.erp_v1.repository.EmailSettingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final EmailSettingRepository emailSettingRepository;

    public JavaMailSender createMailSender() {
        EmailSetting setting = emailSettingRepository.findTopByOrderByIdDesc()
                .orElseGet(() -> {
                    EmailSetting defaultSetting = new EmailSetting();
                    defaultSetting.setSmtpServer("smtp.gmail.com");
                    defaultSetting.setSmtpPort("587");
                    defaultSetting.setFromEmail("torbicadraganjove910@gmail.com");
                    defaultSetting.setFromName("ERP Admin");
                    defaultSetting.setEmailPassword("zmxfytsioyzubfqw");
                    defaultSetting.setCreatedBy("system");
                    return emailSettingRepository.save(defaultSetting);
                });

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(setting.getSmtpServer());
        mailSender.setPort(Integer.parseInt(setting.getSmtpPort()));
        mailSender.setUsername(setting.getFromEmail());
        mailSender.setPassword(setting.getEmailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
