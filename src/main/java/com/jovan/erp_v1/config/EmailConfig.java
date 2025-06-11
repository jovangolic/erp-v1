package com.jovan.erp_v1.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.jovan.erp_v1.model.EmailSetting;
import com.jovan.erp_v1.repository.EmailSettingRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    private final EmailSettingRepository emailSettingRepository;

    @Bean
    public JavaMailSender javaMailSender() {
        EmailSetting setting = emailSettingRepository.findTopByOrderByIdDesc()
            .orElseThrow(() -> new RuntimeException("Email settings not configured"));

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
