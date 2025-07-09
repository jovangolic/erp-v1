package com.jovan.erp_v1.model;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ShiftType;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftPlanning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_center_id")
    private WorkCenter workCenter;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @Column
    private LocalDate date;

    @Column
    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @Column
    private Boolean assigned;
}
