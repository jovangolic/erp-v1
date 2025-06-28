package com.jovan.erp_v1.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.jovan.erp_v1.model.EmailSetting;
import com.jovan.erp_v1.repository.EmailSettingRepository;

import lombok.RequiredArgsConstructor;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class EmailConfig {

    private final EmailSettingRepository emailSettingRepository;

    /*
     * @Bean
     * public JavaMailSender javaMailSender() {
     * EmailSetting setting = emailSettingRepository.findTopByOrderByIdDesc()
     * .orElseThrow(() -> new RuntimeException("Email settings not configured"));
     * 
     * JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
     * mailSender.setHost(setting.getSmtpServer());
     * mailSender.setPort(Integer.parseInt(setting.getSmtpPort()));
     * mailSender.setUsername(setting.getFromEmail());
     * mailSender.setPassword(setting.getEmailPassword());
     * 
     * Properties props = mailSender.getJavaMailProperties();
     * props.put("mail.smtp.auth", "true");
     * props.put("mail.smtp.starttls.enable", "true");
     * 
     * return mailSender;
     * }
     */

    /*
     * @Bean
     * public JavaMailSender javaMailSender() {
     * EmailSetting setting = emailSettingRepository.findTopByOrderByIdDesc()
     * .orElseGet(() -> {
     * EmailSetting defaultSetting = new EmailSetting();
     * defaultSetting.setSmtpServer("smtp.gmail.com");
     * defaultSetting.setSmtpPort("587");
     * defaultSetting.setFromEmail("tvojemail@gmail.com");
     * defaultSetting.setFromName("ERP Admin");
     * defaultSetting.setEmailPassword("tvojpass");
     * defaultSetting.setCreatedBy("system");
     * return emailSettingRepository.save(defaultSetting);
     * });
     * 
     * JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
     * mailSender.setHost(setting.getSmtpServer());
     * mailSender.setPort(Integer.parseInt(setting.getSmtpPort()));
     * mailSender.setUsername(setting.getFromEmail());
     * mailSender.setPassword(setting.getEmailPassword());
     * 
     * Properties props = mailSender.getJavaMailProperties();
     * props.put("mail.smtp.auth", "true");
     * props.put("mail.smtp.starttls.enable", "true");
     * 
     * return mailSender;
     * }
     */

    @Bean
    public JavaMailSender javaMailSender() {
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
