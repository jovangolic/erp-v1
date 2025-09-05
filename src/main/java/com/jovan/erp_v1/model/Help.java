package com.jovan.erp_v1.model;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.HelpCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Column(nullable = false)
    private String title; // Naslov teme pomoci, npr. "Kako kreirati korisnika"

    @Lob
    @Column(nullable = false)
    private String content; // Detaljna uputstva, moze biti i HTML

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelpCategory category; // npr. "Korisnici", "Podesavanja", "Nabavke"

    @Column(nullable = false)
    private boolean isVisible = true; // Da li je prikazano korisnicima

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
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
