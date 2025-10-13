package com.jovan.erp_v1.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jovan.erp_v1.enumeration.ConfirmationDocumentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmationDocument {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;
    
    @ManyToOne
    @JoinColumn(name="shift_id")
    private Shift shift;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ConfirmationDocumentStatus status = ConfirmationDocumentStatus.NEW;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean confirmed = false;
    
    @CreatedDate
    @Column(name = "created_at_date", updatable = false)
    private LocalDateTime createdAtDate;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @CreatedBy
    @Column(name = "created_by_foreman", updatable = false)
    private String createdByForeman;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
}
