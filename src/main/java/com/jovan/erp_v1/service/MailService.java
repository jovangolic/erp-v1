package com.jovan.erp_v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class MailService {

	@Autowired
    private JavaMailSender mailSender;

    public void sendConfirmation(String to, byte[] pdf, String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Potvrda o otpremi robe");
        helper.setText("U prilogu se nalazi potvrda.");

        ByteArrayDataSource source = new ByteArrayDataSource(pdf, "application/pdf");
        helper.addAttachment(fileName, source);

        mailSender.send(message);
        
    }
	
}
