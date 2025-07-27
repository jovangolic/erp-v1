package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.exception.ValidationException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BalanceSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private BigDecimal totalAssets;

    @Column
    private BigDecimal totalLiabilities;

    @Column
    private BigDecimal totalEquity;

    @ManyToOne
    @JoinColumn(name = "fiscal_year_id")
    private FiscalYear fiscalYear;
    
    public BigDecimal calcualteTotalEquity() {
    	if(this.totalAssets == null || this.totalLiabilities == null) {
    		throw new ValidationException("TotalAssets and TotalLiabilities must not be null");
    	}
    	if(this.totalLiabilities.compareTo(this.totalAssets) > 0) {
    		throw new ValidationException("BalanceSheet is insolvent");
    	}
    	return this.totalAssets.subtract(this.totalLiabilities).max(BigDecimal.ZERO);
    }
    
    public BigDecimal calculateTotalLiabilities() {
    	if(this.totalEquity == null || this.totalAssets == null) {
    		throw new ValidationException("TotalEquity, TotalAssets must not be null");
    	}
    	return this.totalAssets.subtract(this.totalEquity).max(BigDecimal.ZERO);
    }
    
    public BigDecimal calculateTotalAssets() {
        if (this.totalLiabilities == null || this.totalEquity == null) {
            throw new ValidationException("TotalLiabilities and TotalEquity must not be null");
        }
        return this.totalLiabilities.add(this.totalEquity).max(BigDecimal.ZERO);
    }
    
    public Boolean isBalanced() {
    	if(this.totalAssets == null || this.totalEquity == null || this.totalLiabilities == null) {
    		throw new ValidationException("TotalAsstes, TotalEquity and TotalLiabilities must not bre null");
    	}
    	BigDecimal sum = this.totalLiabilities.add(this.totalEquity);
    	BigDecimal diffrence = this.totalAssets.subtract(sum).abs();
    	return diffrence.compareTo(new BigDecimal("0.01")) <= 0;
    }
    
    @PrePersist
    @PreUpdate
    private void validateBalanceSheet() {
        if (this.totalAssets == null || this.totalLiabilities == null || this.totalEquity == null) {
            throw new ValidationException("Sva polja (assets, liabilities, equity) moraju biti popunjena.");
        }
        BigDecimal expectedAssets = this.totalLiabilities.add(this.totalEquity);
        BigDecimal difference = this.totalAssets.subtract(expectedAssets).abs();
        if (difference.compareTo(new BigDecimal("0.01")) > 0) {
            throw new ValidationException("Bilans nije izbalansiran: Aktiva mora biti jednaka zbiru obaveza i kapitala.");
        }
    }
    
    @PostLoad
    private void validateAfterLoad() {
        if (!isBalanced()) {
            System.err.println("UPOZORENJE: Učitani bilans nije izbalansiran!");
        }
    }
}
