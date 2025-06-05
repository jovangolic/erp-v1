package com.jovan.erp_v1.model;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ShipmentStatus;

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
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate shipmentDate;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @ManyToOne
    private LogisticsProvider provider;

    @OneToOne
    private OutboundDelivery outboundDelivery;

    @OneToOne(mappedBy = "shipment", cascade = CascadeType.ALL)
    private TrackingInfo trackingInfo;

    @ManyToOne
    @JoinColumn(name = "origin_storage_id")
    private Storage originStorage;
}
