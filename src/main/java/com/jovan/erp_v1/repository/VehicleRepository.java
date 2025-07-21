package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByModel(String model);
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findByModelAndStatus(String model, VehicleStatus status);
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    List<Vehicle> findByRegistrationNumberContainingIgnoreCaseOrModelContainingIgnoreCase(String keyword1,
            String keyword2);
    List<Vehicle> findByModelContainingIgnoreCase(String modelFragment);
}
