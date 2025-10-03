package com.jovan.erp_v1.model;

import java.util.ArrayList;
import java.util.List;

import com.jovan.erp_v1.enumeration.VehicleFuel;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.enumeration.VehicleTypeStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    @Column
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleFuel fuel;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VehicleTypeStatus typeStatus = VehicleTypeStatus.NEW;
    
    @Column(nullable = false)
	@Builder.Default
	private Boolean confirmed = false;
    
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VehicleLocation> vehicleLocations = new ArrayList<VehicleLocation>();
}
