package com.jovan.erp_v1.model;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.DeliveryItemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "inboundDelivery")
    private InboundDelivery inboundDelivery;

    @ManyToOne
    @JoinColumn(name = "outboundDelivery_id")
    private OutboundDelivery outboundDelivery;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DeliveryItemStatus status = DeliveryItemStatus.NEW;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean confirmed = false;
}
