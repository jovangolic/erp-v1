package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.VehicleFuel;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.enumeration.VehicleTypeStatus;
import com.jovan.erp_v1.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

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
    		SELECT v FROM Vehicle v
    		WHERE (:id IS NULL OR v.id = :id)
    		AND (:idFrom IS NULL OR v.id >= :idFrom)
		    AND (:idTo IS NULL OR v.id <= :idTo)
		    AND (:registrationNumber IS NULL OR LOWER(v.registrationNumber) LIKE LOWER(CONCAT('%', :registrationNumber, '%')))
		    AND (:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%')))
		    AND (:status IS NULL OR v.status = :status)
		    AND (:fuel IS NULL OR v.fuel = :fuel)
		    AND (:typeStatus IS NULL OR v.typeStatus = :typeStatus)
		    AND (:confirmed IS NULL OR v.confirmed = :confirmed)
    		""")
    List<Vehicle> generalSearch(
    		@Param("id") Long id,
    		@Param("idFrom") Long idFrom,
	        @Param("idTo") Long idTo,
    		@Param("registrationNumber") String registrationNumber,@Param("model") String model,
    		@Param("status") VehicleStatus status,@Param("fuel") VehicleFuel fuel,
    		@Param("typeStatus") VehicleTypeStatus typeStatus,
    		@Param("confirmed") Boolean confirmed);
    
    Long countByModelContainingIgnoreCase(String model);
    
    @Query("SELECT DISTINCT v FROM Vehicle v WHERE v.id = :id")
    Optional<Vehicle> trackVehicle(@Param("id") Long id);
    
    //nove metode
    //prikaz samo akticnih vozila koji ima makar jednu zabelezenu lokaciju. Tu se u upitu koristi klucna rec INNER JOIN FETCH
    @Query("SELECT v FROM Vehicle v INNER JOIN FETCH v.locations WHERE v.id = :id")
    Optional<Vehicle> findVehicleWithExistingLocations(@Param("id") Long id);
}
