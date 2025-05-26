package com.jovan.erp_v1.model;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.HelpCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Help {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title; // Naslov teme pomoći, npr. "Kako kreirati korisnika"

    @Lob
    private String content; // Detaljna uputstva, može biti i HTML

    private HelpCategory category; // npr. "Korisnici", "Podešavanja", "Nabavke"

    private boolean isVisible = true; // Da li je prikazano korisnicima

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
