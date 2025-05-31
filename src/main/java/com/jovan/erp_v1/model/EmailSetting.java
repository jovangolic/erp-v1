package com.jovan.erp_v1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String smtpServer;

    @Column(nullable = false)
    private String smtpPort;

    @Column(nullable = false)
    private String fromEmail;

    @Column(nullable = false)
    private String fromName;

    @Column(nullable = false)
    private String emailPassword;
}
