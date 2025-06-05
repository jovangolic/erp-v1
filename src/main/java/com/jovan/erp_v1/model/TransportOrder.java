package com.jovan.erp_v1.model;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.TransportStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate scheduledDate;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private Driver drivers; // misli se na jedninu da se ne bi sudaralo sa kljucnom recju Driver

    @Enumerated(EnumType.STRING)
    private TransportStatus status;

    @OneToOne
    private OutboundDelivery outboundDelivery;
}
