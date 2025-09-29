package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.model.Trip;


@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {

	List<Trip> findByStartLocationContainingIgnoreCase(String startLocation);
	List<Trip> findByEndLocationContainingIgnoreCase(String endLocation);
	
	List<Trip> findByStartTime(LocalDateTime startTime);
	List<Trip> findByStartTimeBefore(LocalDateTime startTime);
	List<Trip> findByStartTimeAfter(LocalDateTime startTime);
	List<Trip> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
	
	List<Trip> findByEndTime(LocalDateTime endTime);
	List<Trip> findByEndTimeBefore(LocalDateTime endTime);
	List<Trip> findByEndTimeAfter(LocalDateTime endTime);
	List<Trip> findByEndTimeBetween(LocalDateTime start, LocalDateTime end);
	
	@Query("SELECT t FROM Trip t WHERE t.startTime >= :start AND t.endTime <= :end")
	List<Trip> findTripsWithinPeriod(@Param("start") LocalDateTime start, 
	                                 @Param("end") LocalDateTime end);
	
	List<Trip> findByStatus(TripStatus status);
	List<Trip> findByDriverId(Long driverId);
	List<Trip> findByStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(TripStatus status, String firstName, String lastName);
	List<Trip> findByDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(String firstName, String lastName);
	List<Trip> findByDriverPhoneLikeIgnoreCase(String phone);
	List<Trip> findByDriver_Status(DriverStatus status);
	
	@Query("SELECT t FROM Trip t WHERE t.status IN :statuses")
	List<Trip> findByStatusIn(@Param("statuses") List<TripStatus> statuses);
	
	
	@Query("SELECT t FROM Trip t WHERE t.startTime >= :startOfDay AND t.startTime <= :endOfDay")
	List<Trip> findByStartTimeOnly(@Param("startOfDay") LocalDateTime startOfDay,@Param("endOfDay") LocalDateTime endOfDay);
	
	//Metoda za vozaca, da vidi samo svoje voznje
	@Query("""
		    SELECT t FROM Trip t
		    WHERE t.driver.id = :driverId
		    """)
	List<Trip> findByDriverIdSecure(@Param("driverId") Long driverId);
}
