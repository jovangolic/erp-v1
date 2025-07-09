package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.ShiftReport;

@Repository
public interface ShiftReportRepository extends JpaRepository<ShiftReport, Long> {

	List<ShiftReport> findByCreatedById(Long userId);

    List<ShiftReport> findByRelatedShiftId(Long shiftId);

    List<ShiftReport> findByCreatedAtAfter(LocalDateTime date);
    //nove metode
    List<ShiftReport> findByDescription(String description);
    List<ShiftReport> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT sr FROM ShiftReport sr WHERE sr.createdAt >= :date")
    List<ShiftReport> findByCreatedAtAfterOrEqual(@Param("date") LocalDateTime date);
    //createdBy
    @Query("SELECT sr FROM ShiftReport sr WHERE LOWER(sr.createdBy.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<ShiftReport> findByCreatedBy_EmailLikeIgnoreCase(@Param("email") String email);
    @Query("SELECT sr FROM ShiftReport sr WHERE LOWER(sr.createdBy.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
    List<ShiftReport> findByCreatedBy_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
    @Query("SELECT sr FROM ShiftReport sr WHERE LOWER(sr.createdBy.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(sr.createdBy.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<ShiftReport> findByCreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);
    //shift
    List<ShiftReport> findByRelatedShift_EndTimeBefore(LocalDateTime time);
    List<ShiftReport> findByRelatedShift_EndTimeAfter(LocalDateTime time);
    List<ShiftReport> findByRelatedShift_StartTimeAfter(LocalDateTime time);
    List<ShiftReport> findByRelatedShift_StartTimeBefore(LocalDateTime time);
    List<ShiftReport> findByRelatedShift_EndTimeBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT sr FROM ShiftReport sr WHERE sr.relatedShift.endTime IS NULL OR sr.relatedShift.endTime > CURRENT_TIMESTAMP")
    List<ShiftReport> findRelatedShift_ActiveShifts();
    List<ShiftReport> findByRelatedShift_EndTimeIsNull();
    @Query("SELECT sr FROM ShiftReport sr WHERE sr.relatedShift.shiftSupervisor.id = :supervisorId")
    List<ShiftReport> findByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(Long supervisorId, LocalDateTime start, LocalDateTime end);
    @Query("SELECT sr FROM ShiftReport sr WHERE sr.relatedShift.shiftSupervisor.id = :supervisorId")
    List<ShiftReport> findByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(Long supervisorId);
    
  //boolean
    @Query("SELECT COUNT(sr) > 0 FROM ShiftReport sr WHERE sr.relatedShift.shiftSupervisor.id = :supervisorId AND sr.relatedShift.endTime IS NULL")
    boolean existsByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(Long supervisorId);
    
    @Query("SELECT CASE WHEN COUNT(sr) > 0 THEN true ELSE false END FROM ShiftReport sr WHERE sr.relatedShift.endTime IS NULL")
    boolean existsByRelatedShift_EndTimeIsNull();
   
    boolean existsByRelatedShift_StartTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    boolean existsByRelatedShift_EndTimeAfter(LocalDateTime now);
    boolean existsByDescription(String description);
    @Query("SELECT COUNT(sr) > 0 FROM ShiftReport sr WHERE sr.relatedShift.endTime > CURRENT_TIMESTAMP")
    boolean existsByRelatedShift_EndTimeInFuture();
    @Query("SELECT COUNT(sr) > 0 FROM ShiftReport sr WHERE sr.relatedShift.shiftSupervisor.id = :supervisorId AND sr.relatedShift.startTime BETWEEN :start AND :end")
    boolean existsByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(@Param("supervisorId") Long supervisorId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
