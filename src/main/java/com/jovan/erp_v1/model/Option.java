package com.jovan.erp_v1.model;

import com.jovan.erp_v1.enumeration.OptionCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String label; // Šta se prikazuje korisniku
    @Column
    private String value; // Šta se šalje/koristi u sistemu

    @Column
    @Enumerated(EnumType.STRING)
    private OptionCategory category; // npr: "GENDER", "ROLE", "STATUS"

    @Column
    @Builder.Default
    private boolean active = true;
}
