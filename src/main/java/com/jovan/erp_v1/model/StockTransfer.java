package com.jovan.erp_v1.model;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.TransferStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate transferDate;

    @ManyToOne
    @JoinColumn(name = "from_storage_id")
    private Storage fromStorage;

    @ManyToOne
    @JoinColumn(name = "to_storage_id")
    private Storage toStorage;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @OneToMany(mappedBy = "stockTransfer", cascade = CascadeType.ALL)
    private List<StockTransferItem> items;
}
