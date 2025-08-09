package com.jovan.erp_v1.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class ShiftReport {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column
    private String description;

	@Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "related_shift")
    private Shift relatedShift;

    @Column
    private String filePath; // za PDF putanju ako se izvestaj stampa
    
    @CreatedDate
    @Column(name = "created_at_date", updatable = false)
    private LocalDateTime createdAtDate;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @CreatedBy
    @Column(name = "created_by_user", updatable = false)
    private String createdBy_user;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
}
