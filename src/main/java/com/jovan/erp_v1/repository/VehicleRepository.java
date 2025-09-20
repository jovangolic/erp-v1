package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.VehicleFuel;
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
    
    //nove metode
    List<Vehicle> findByFuel(VehicleFuel fuel);
    List<Vehicle> findByModelContainingIgnoreCaseAndFuel(String model, VehicleFuel fuel);
    
    @Query("""
    		SELECT v FROM Vehicle 
    		WHERE (:id IS NULL OR v.id = :id)
    		AND (:idFrom IS NULL OR v.id >= :idFrom)
		    AND (:idTo IS NULL OR v.id <= :idTo)
		    AND (:registrationNumber IS NULL OR LOWER(v.registrationNumber) LIKE LOWER(CONCAT('%', :registrationNumber, '%')))
		    AND (:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%')))
		    AND (:status IS NULL OR v.status = :status)
		    AND (:fuel IS NULL OR v.fuel = :fuel = :status)
    		""")
    List<Vehicle> generalSearch(
    		@Param("id") Long id,
    		@Param("idFrom") Long idFrom,
	        @Param("idTo") Long idTo,
    		@Param("registrationNumber") String registrationNumber,@Param("model") String model,
    		@Param("status") VehicleStatus status,@Param("fuel") VehicleFuel fuel);
}
