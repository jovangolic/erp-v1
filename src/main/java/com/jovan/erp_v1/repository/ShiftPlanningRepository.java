package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jovan.erp_v1.model.ShiftPlanning;
import java.time.LocalDate;
import com.jovan.erp_v1.enumeration.ShiftType;

@Repository
public interface ShiftPlanningRepository extends JpaRepository<ShiftPlanning, Long> {

    List<ShiftPlanning> findByWorkCenter_NameContainingIgnoreCase(String name);
    List<ShiftPlanning> findByWorkCenter_Capacity(BigDecimal capacity);
    List<ShiftPlanning> findByWorkCenter_LocationContainingIgnoreCase(String location);
    List<ShiftPlanning> findByEmployee_Id(Long id);
    List<ShiftPlanning> findByEmployee_Email(String email);
    List<ShiftPlanning> findByEmployee_UsernameContainingIgnoreCase(String username);
    @Query("SELECT s FROM ShiftPlanning s WHERE LOWER(s.employee.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(s.employee.lastName) LIKE LOWER(CONCAT('%', :lastName,'%'))")
    List<ShiftPlanning> findByEmployeeFirstContainingIgnoreCaseAndLastNameContainingIgnoreCase(@Param("firstName") String firstName,
            @Param("lastName") String lastName);
    List<ShiftPlanning> findByEmployee_PhoneNumber(String phoneNumber);
    List<ShiftPlanning> findByDate(LocalDate date);
    List<ShiftPlanning> findByDateBetween(LocalDate start, LocalDate end);
    List<ShiftPlanning> findByDateGreaterThanEqual(LocalDate date);
    @Query("SELECT sp FROM ShiftPlanning sp WHERE sp.date >= :date")
    List<ShiftPlanning> findOrdersWithStartDateAfterOrEqual(@Param("date") LocalDate date);
    List<ShiftPlanning> findByShiftType(ShiftType shiftType);
    List<ShiftPlanning> findByAssigned(Boolean assigned);
    List<ShiftPlanning> findByEmployee_IdAndAssignedTrue(Long employeeId);
    List<ShiftPlanning> findByEmployee_IdAndShiftType(Long employeeId, ShiftType shiftType);
    List<ShiftPlanning> findByWorkCenter_IdAndDateAfterAndAssignedFalse(Long workCenterId, LocalDate date);
    @Query("SELECT sp FROM ShiftPlanning sp WHERE sp.employee.id = :employeeId AND sp.date BETWEEN :startDate AND :endDate")
    List<ShiftPlanning> findShiftsForEmployeeBetweenDates(@Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate start,
            @Param("endDate") LocalDate end);
    boolean existsByEmployee_IdAndDateAndShiftType(Long employeeId, LocalDate date, ShiftType shiftType);
    List<ShiftPlanning> findByWorkCenter_CapacityGreaterThan(BigDecimal capacity);
    List<ShiftPlanning> findByWorkCenter_CapacityLessThan(BigDecimal capacity);
}
