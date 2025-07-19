package com.jovan.erp_v1.model;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.SystemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean maintenanceMode; // Da li je sistem u režimu održavanja

    @Column(nullable = false)
    private Boolean registrationEnabled; // Da li je omogućena registracija korisnika

    @Column
    private LocalDateTime lastRestartTime; // Kada je poslednji put sistem restartovan

    @Column
    private String systemVersion; // Verzija aplikacije

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SystemStatus statusMessage;

    @PrePersist
    public void onCreate() {
    	if(this.maintenanceMode == null)this.maintenanceMode = false;
    	if(this.registrationEnabled == null) this.registrationEnabled = false;
        this.lastRestartTime = LocalDateTime.now();
    }
}
