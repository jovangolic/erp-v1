package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jovan.erp_v1.enumeration.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String accountNumber;

    @Column(nullable = false, length = 100)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @OneToMany(mappedBy = "sourceAccount")
    private List<Transaction> sourceTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "targetAccount")
    private List<Transaction> targetTransactions = new ArrayList<>();
}
