package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

	List<Trip> findByStatusAndDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(
			TripStatus status, String firstName, String lastName);

	List<Trip> findByDriverFirstNameContainingIgnoreCaseAndDriverLastNameContainingIgnoreCase(String firstName,
			String lastName);

	List<Trip> findByDriverPhoneLikeIgnoreCase(String phone);

	List<Trip> findByDriver_Status(DriverStatus status);

	@Query("SELECT t FROM Trip t WHERE t.status IN :statuses")
	List<Trip> findByStatusIn(@Param("statuses") List<TripStatus> statuses);

	@Query("SELECT t FROM Trip t WHERE t.startTime >= :startOfDay AND t.startTime <= :endOfDay")
	List<Trip> findByStartTimeOnly(@Param("startOfDay") LocalDateTime startOfDay,
			@Param("endOfDay") LocalDateTime endOfDay);

	// Metoda za vozaca, da vidi samo svoje voznje
	@Query("""
			SELECT t FROM Trip t
			WHERE t.driver.id = :driverId
			""")
	List<Trip> findByDriverIdSecure(@Param("driverId") Long driverId);

	// nove metode za praveljenje driver izvestaja
	Long countByDriver_Id(Long driverId);

	Long countByDriver_IdAndStatus(Long driverId, TripStatus status);

	@Query("""
			SELECT COUNT(t) FROM Trip t
			WHERE t.driver.id = :driverId
			AND t.startTime >= :startDate
			AND t.endTime <= :endDate
			""")
	Long countByDriver_IdAndDateBetween(@Param("driverId") Long driverId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("""
			SELECT COUNT(t) FROM Trip t
			WHERE t.driver.id = :driverId
			AND t.status IN :statuses
			AND t.startTime >= :startDate
			AND t.endTime <= :endDate
			""")
	Long countByDriver_IdAndStatusInAndDateBetween(Long driverId, List<TripStatus> statusES, LocalDate startDate,
			LocalDate endDate);

	@Query(value = """
			    SELECT COALESCE(AVG(TIMESTAMPDIFF(SECOND, t.start_time, t.end_time)) / 3600, 0)
			    FROM trip t
			    WHERE t.driver_id = :driverId
			      AND (:startDate IS NULL OR t.start_time >= :startDate)
			      AND (:endDate IS NULL OR t.end_time < DATE_ADD(:endDate, INTERVAL 1 DAY))
			      AND t.end_time IS NOT NULL
			""", nativeQuery = true)
	BigDecimal calculateAverageDuration(
			@Param("driverId") Long driverId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query(value = """
			    SELECT COALESCE(SUM(t.fare), 0)
			    FROM trip t
			    WHERE t.driver_id = :driverId
			      AND (:startDate IS NULL OR t.start_time >= :startDate)
			      AND (:endDate IS NULL OR t.end_time < DATE_ADD(:endDate, INTERVAL 1 DAY))
			""", nativeQuery = true)
	BigDecimal calculateTotalRevenue(
			@Param("driverId") Long driverId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	// prosecno trajanje u satima
	@Query(value = """
			    SELECT COALESCE(AVG(TIMESTAMPDIFF(SECOND, t.start_time, t.end_time)) / 3600, 0)
				FROM trip t
				WHERE t.driver_id = :driverId
			""", nativeQuery = true)
	BigDecimal calculateAverageDuration(@Param("driverId") Long driverId);

	// ukupan prihod
	@Query("""
			    SELECT COALESCE(SUM(t.fare), 0)
			    FROM Trip t
			    WHERE t.driver.id = :driverId
			""")
	BigDecimal calculateTotalRevenue(@Param("driverId") Long driverId);

}
