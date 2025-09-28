package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.model.Trip;
import com.jovan.erp_v1.request.TripSearchRequest;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

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
	List<Trip> findbyStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(TripStatus status, String firstName, String lastName);
	List<Trip> findByDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(String firstName, String lastName);
	List<Trip> findByDriverPhoneLikeIgnoreCase(String phone);
	List<Trip> findByDriver_Status(DriverStatus status);
	
	@Query("SELECT t FROM Trip t WHERE t.status IN :statuses")
	List<Trip> findByStatusIn(@Param("statuses") List<TripStatus> statuses);
	
	
	default List<Trip> generalSearch(TripSearchRequest req) {
	    return generalSearchJpa(
	            req.id(),
	            req.idFrom(),
	            req.idTo(),
	            req.startLocation(),
	            req.endLocation(),
	            req.startTime(),
	            req.endTime(),
	            req.startTimeAfter(),
	            req.startTimeBefore(),
	            req.endTimeAfter(),
	            req.endTimeBefore(),
	            req.tripStatus(),
	            req.driverId(),
	            req.driverIdFrom(),
	            req.driverIdTo(),
	            req.firstName(),
	            req.lastName(),
	            req.phone(),
	            req.driverStatus(),
	            req.confirmed()
	    );
	}

	@Query(""" 
	    SELECT t FROM Trip t
	    WHERE (:id IS NULL OR t.id = :id)
	    AND (:idFrom IS NULL OR t.id >= :idFrom)
	    AND (:idTo IS NULL OR t.id <= :idTo)
	    AND (:startLocation IS NULL OR LOWER(t.startLocation) LIKE LOWER(CONCAT('%', :startLocation, '%')))
	    AND (:endLocation IS NULL OR LOWER(t.endLocation) LIKE LOWER(CONCAT('%', :endLocation, '%')))
	    AND (:startTime IS NULL OR t.startTime = :startTime)
	    AND (:endTime IS NULL OR t.endTime = :endTime)
	    AND (:startTimeAfter IS NULL OR t.startTime >= :startTimeAfter)
	    AND (:startTimeBefore IS NULL OR t.startTime <= :startTimeBefore)
	    AND (:endTimeAfter IS NULL OR t.endTime >= :endTimeAfter)
	    AND (:endTimeBefore IS NULL OR t.endTime <= :endTimeBefore)
	    AND (:tripStatus IS NULL OR t.status = :tripStatus)
	    AND (:driverId IS NULL OR t.driver.id = :driverId)
	    AND (:driverIdFrom IS NULL OR t.driver.id >= :driverIdFrom)
	    AND (:driverIdTo IS NULL OR t.driver.id <= :driverIdTo)
	    AND (:firstName IS NULL OR LOWER(t.driver.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
	    AND (:lastName IS NULL OR LOWER(t.driver.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
	    AND (:phone IS NULL OR LOWER(t.driver.phone) LIKE LOWER(CONCAT('%', :phone, '%')))
	    AND (:driverStatus IS NULL OR t.driver.status = :driverStatus)
	    AND (:confirmed IS NULL OR t.driver.confirmed = :confirmed)
	    """)
	List<Trip> generalSearchJpa(
	        @Param("id") Long id, 
	        @Param("idFrom") Long idFrom, 
	        @Param("idTo") Long idTo,
	        @Param("startLocation") String startLocation, 
	        @Param("endLocation") String endLocation,
	        @Param("startTime") LocalDateTime startTime, 
	        @Param("endTime") LocalDateTime endTime,
	        @Param("startTimeAfter") LocalDateTime startTimeAfter, 
	        @Param("startTimeBefore") LocalDateTime startTimeBefore,
	        @Param("endTimeAfter") LocalDateTime endTimeAfter, 
	        @Param("endTimeBefore") LocalDateTime endTimeBefore,
	        @Param("tripStatus") TripStatus tripStatus, 
	        @Param("driverId") Long driverId,
	        @Param("driverIdFrom") Long driverIdFrom, 
	        @Param("driverIdTo") Long driverIdTo,
	        @Param("firstName") String firstName, 
	        @Param("lastName") String lastName,
	        @Param("phone") String phone, 
	        @Param("driverStatus") DriverStatus driverStatus,
	        @Param("confirmed") Boolean confirmed
	);
	
	@Query("SELECT t FROM Trip t WHERE t.startTime >= :startOfDay AND t.startTime <= :endOfDay")
	List<Trip> findByStartTimeOnly(@Param("startOfDay") LocalDateTime startOfDay,@Param("endOfDay") LocalDateTime endOfDay);
}
