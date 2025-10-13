package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.model.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>, JpaSpecificationExecutor<Driver> {

    List<Driver> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    Optional<Driver> findByPhone(String phone);
    
    List<Driver> findByPhoneLikeIgnoreCase(String phone);
    List<Driver> findByStatus(DriverStatus status);
    List<Driver> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndStatus(String firstName, String lastName, DriverStatus status);
    Boolean existsByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
    
    @Query("SELECT d FROM Driver d LEFT JOIN FETCH d.trips WHERE d.id = :id")
    Optional<Driver> trackDriver(@Param("id") Long id);
    
}
