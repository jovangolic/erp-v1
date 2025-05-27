package com.jovan.erp_v1.model;

import com.jovan.erp_v1.enumeration.EditOptType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditOpt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String value;

    @Column
    @Enumerated(EnumType.STRING)
    private EditOptType type; // npr: "NOTIFICATION_METHOD", "REPORT_FORMAT", itd.

    @Column
    private boolean editable = true;

    @Column
    private boolean visible = true;
}
