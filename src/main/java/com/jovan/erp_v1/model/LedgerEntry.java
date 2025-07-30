package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.LedgerType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime entryDate;

    @Column
    private BigDecimal amount;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    @Enumerated(EnumType.STRING)
    private LedgerType type;
    
    @Column
    private LocalDateTime modifiedAt;
    
    @PrePersist
    public void prePersist() {
        if (this.entryDate == null) {
            this.entryDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}
