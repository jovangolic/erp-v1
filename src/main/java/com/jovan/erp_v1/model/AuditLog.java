package com.jovan.erp_v1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @ManyToOne
    private User user; // Ko je izvršio akciju

    @Column(nullable = false)
    private String action; // Npr. "CREATE_USER", "UPDATE_INVENTORY", itd.

    @Column(nullable = false)
    private LocalDateTime timestamp; // Kada je akcija izvršena

    @Column
    private String details;
}
