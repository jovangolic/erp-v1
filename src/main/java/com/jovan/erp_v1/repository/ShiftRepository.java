package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByShiftSupervisorId(Long supervisorId);

    List<Shift> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Shift s WHERE s.endTime IS NULL OR s.endTime > CURRENT_TIMESTAMP")
    Integer countActiveShifts();
    
    //nove metode
    List<Shift> findByEndTimeBefore(LocalDateTime time);
    List<Shift> findByEndTimeAfter(LocalDateTime time);
    List<Shift> findByStartTimeAfter(LocalDateTime time);
    List<Shift> findByStartTimeBefore(LocalDateTime time);
    List<Shift> findByEndTimeBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT s FROM Shift s WHERE s.endTime IS NULL OR s.endTime > CURRENT_TIMESTAMP")
    List<Shift> findActiveShifts();
    List<Shift> findByEndTimeIsNull();
    List<Shift> findByShiftSupervisorIdAndStartTimeBetween(Long supervisorId, LocalDateTime start, LocalDateTime end);
    List<Shift> findByShiftSupervisorIdAndEndTimeIsNull(Long supervisorId);
    //brojanje po datumu
    @Query("SELECT COUNT(s) FROM Shift s WHERE DATE(s.startTime) = :date")
    Integer countShiftsByStartDate(@Param("date") LocalDate date);
    @Query("SELECT COUNT(s) FROM Shift s WHERE DATE(s.endTime) = :date")
    Integer countShiftsByEndDate(@Param("date") LocalDate date);
    //Smena koja je trenutno u toku
    @Query("SELECT s FROM Shift s WHERE s.shiftSupervisor.id = :supervisorId AND s.startTime <= CURRENT_TIMESTAMP AND (s.endTime IS NULL OR s.endTime > CURRENT_TIMESTAMP)")
    List<Shift> findCurrentShiftBySupervisor(@Param("supervisorId") Long supervisorId);
    //smene koje traju duze sati
    @Query("SELECT s FROM Shift s WHERE FUNCTION('TIMESTAMPDIFF', HOUR, s.startTime, s.endTime) > :hours")
    List<Shift> findShiftsLongerThan(@Param("hours") Integer hours);
    //preklapanje smena sa odredjenim datumom
    @Query("SELECT s FROM Shift s WHERE s.startTime < :end AND (s.endTime IS NULL OR s.endTime > :start)")
    List<Shift> findShiftsOverlappingPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    //nocna smena
    @Query("SELECT s FROM Shift s WHERE " +
    	       "(HOUR(s.startTime) >= 22 OR HOUR(s.endTime) <= 6)")
    List<Shift> findNightShifts();
    //sledece smene
    List<Shift> findFutureShifts(LocalDateTime now);
    @Query("SELECT s FROM Shift s WHERE s.startTime > CURRENT_TIMESTAMP")
    List<Shift> findAllFutureShifts();
    //supervisor
    @Query("SELECT sh FROM Shift sh WHERE LOWER(sh.shiftSupervisor.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Shift> findByShiftSupervisor_EmailLikeIgnoreCase(@Param("email") String email);
    @Query("SELECT s FROM Shift s WHERE LOWER(s.shiftSupervisor.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
    List<Shift> findByShiftSupervisorPhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
    @Query("SELECT sh FROM Shift sh WHERE LOWER(sh.shiftSupervisor.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(so.shiftSupervisor.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Shift> findByShiftSupervisor_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);
    
    //boolean
    boolean existsByShiftSupervisorIdAndEndTimeIsNull(Long supervisorId);
    boolean existsByEndTimeIsNull();
    boolean existsByStartTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    boolean existsByEndTimeAfter(LocalDateTime now);
}
