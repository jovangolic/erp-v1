package com.jovan.erp_v1.model;

import com.jovan.erp_v1.enumeration.PermissionActionType;
import com.jovan.erp_v1.enumeration.PermissionResourceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionResourceType resourceType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionActionType actionType;
}
