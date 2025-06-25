package com.jovan.erp_v1.model;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.AuditActionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user; // Ko je izvršio akciju

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditActionType action; // Enum za dozvoljene akcije (bez free text!)

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 1000)
    private String details; // Opcioni opis, npr. "Sent email to john@example.com"

    @Column
    private String ipAddress; // IP adresa korisnika, možeš uzeti iz request-a

    @Column
    private String userAgent; // Browser info (opcionalno, ali korisno)
}
