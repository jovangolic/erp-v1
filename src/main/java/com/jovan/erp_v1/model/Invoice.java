package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.InvoiceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    private LocalDateTime issueDate;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name="buyer_id")
    private Buyer buyer;

    @OneToOne
    private Sales relatedSales;

    @OneToOne
    private Payment payment;

    private String note;
    
    @OneToOne(mappedBy = "invoice")
    private SalesOrder salesOrder;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User createdBy;
}