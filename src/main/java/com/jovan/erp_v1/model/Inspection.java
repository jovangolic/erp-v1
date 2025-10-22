package com.jovan.erp_v1.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionStatus;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.exception.ValidationException;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *Predstavlja konkretnu proveru kvaliteta
 */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Inspection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String code;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private InspectionType type;
	
	@Column(nullable = false)
	private LocalDateTime inspectionDate;
	
	@ManyToOne
	@JoinColumn(name="batch_id")
	private Batch batch;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="inspector_id")
	private User inspector;
	
	@Column(nullable = false)
	private Integer quantityInspected;
	
	@Column(nullable = false)
    private Integer quantityAccepted;
	
	@Column(nullable = false)
    private Integer quantityRejected;
	
	@Column
    private String notes;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InspectionResult result;
	
	@ManyToOne
	@JoinColumn(name = "quality_check_id")
	private QualityCheck qualityCheck;
	
	@Column(nullable = false)
	@Builder.Default
	private Boolean confirmed = false;
	
	@Column(nullable = false)
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private InspectionStatus status = InspectionStatus.NEW;
	
	@OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<InspectionDefect> defects = new ArrayList<>();
	
	@OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<TestMeasurement> measurements = new ArrayList<>();
	
	@CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
    
    @PrePersist
    @PreUpdate
    private void validateQuantities() {
        if (this.quantityAccepted == null || this.quantityRejected == null || this.quantityInspected == null) {
            throw new ValidationException("Quantities must not be null.");
        }
        if (this.quantityAccepted < 0 || this.quantityRejected < 0 || this.quantityInspected <= 0) {
            throw new ValidationException("Quantities must be positive (inspected > 0, accepted/rejected ≥ 0).");
        }
        if (!this.quantityInspected.equals(this.quantityAccepted + this.quantityRejected)) {
            throw new ValidationException("Quantity inspected must equal accepted + rejected.");
        }
    }

    public Integer calculateQuantityInspected() {
        if (this.quantityAccepted == null || this.quantityRejected == null) {
            return null;
        }
        return this.quantityAccepted + this.quantityRejected;
    }
    
    public Integer calculateQuantityAccepted() {
        if (this.quantityInspected == null || this.quantityRejected == null) {
            return null;
        }
        return this.quantityInspected - this.quantityRejected;
    }

    public Integer calculateQuantityRejected() {
        if (this.quantityInspected == null || this.quantityAccepted == null) {
            return null;
        }
        return this.quantityInspected - this.quantityAccepted;
    }

    public boolean isConsistent() {
        return this.quantityInspected != null
                && this.quantityAccepted != null
                && this.quantityRejected != null
                && this.quantityInspected.equals(this.quantityAccepted + this.quantityRejected);
    }
}
