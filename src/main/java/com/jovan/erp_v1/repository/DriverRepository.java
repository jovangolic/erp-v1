package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.model.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    Optional<Driver> findByPhone(String phone);
    
    List<Driver> findByPhoneLikeIgnoreCase(String phone);
    List<Driver> findByStatus(DriverStatus status);
    List<Driver> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndStatus(String firstName, String lastName, DriverStatus status);
    Boolean existsByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
    
    @Query("SELECT d FROM Driver d LEFT JOIN FETCH d.trips WHERE d.id = :id")
    Optional<Driver> trackDriver(@Param("id") Long id);
    @Query("""
    		SELECT d From Driver d 
    		WHERE (:id IS NULL OR d.id = :id)
    		AND (:idFrom IS NULL OR d.id >= :idFrom)
		    AND (:idTo IS NULL OR d.id <= :idTo)
		    AND (:firstName IS NULL OR LOWER(d.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
		    AND (:lastName IS NULL OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
		    AND (:phone IS NULL OR LOWER(d.phone) LIKE LOWER(CONCAT('%', :phone, '%')))
		    AND (:status IS NULL OR d.status = :status)
		    AND (:confirmed IS NULL OR d.confirmed = :confirmed)
    		""")
    List<Driver> generalSearch(@Param("id") Long id, @Param("idFrom") Long idFrom, @Param("idTo") Long idTo,
    		@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("phone") String phone,
    		@Param("status") DriverStatus status, @Param("confirmed") Boolean confirmed);
}
