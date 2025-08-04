package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jovan.erp_v1.exception.ValidationException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncomeStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate periodStart;

    @Column
    private LocalDate periodEnd;

    @Column
    private BigDecimal totalRevenue;

    @Column
    private BigDecimal totalExpenses;

    @Column
    private BigDecimal netProfit;

    @ManyToOne
    @JoinColumn(name = "fiscal_year_id")
    private FiscalYear fiscalYear;
    
    public BigDecimal calculateNetProfit() {
    	if(this.totalRevenue == null || this.totalExpenses == null) {
    		throw new ValidationException("TotalRevenu and TotalExpenses must not be null");
    	}
    	if(this.totalExpenses.compareTo(this.totalRevenue) > 0) {
    		throw new ValidationException("TotalExpenses must not be greater than TotalRevenue");
    	}
    	if(this.totalRevenue.compareTo(BigDecimal.ZERO) < 0) {
    		throw new ValidationException("TotalRevenue must not be negative number");
    	}
    	return this.totalRevenue.subtract(totalExpenses).max(BigDecimal.ZERO);
    }
    
    public boolean isProfitable() {
        return calculateNetProfit().compareTo(BigDecimal.ZERO) > 0;
    }
    
    public BigDecimal calculateTotalRevenue() {
        if (this.netProfit == null || this.totalExpenses == null) {
            throw new IllegalStateException("Cannot calculate total revenue: values are missing");
        }
        return netProfit.add(totalExpenses).max(BigDecimal.ZERO);
    }
    
    public BigDecimal calculateTotalExpenses() {
    	if(this.netProfit == null || this.totalRevenue == null) {
    		throw new ValidationException("NetProfit and TotalRevenue must not be null");
    	}
    	return this.totalRevenue.subtract(this.netProfit).max(BigDecimal.ZERO);
    }
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
}
