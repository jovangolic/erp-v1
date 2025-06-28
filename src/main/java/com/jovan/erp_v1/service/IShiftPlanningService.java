package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.ShiftType;
import com.jovan.erp_v1.request.ShiftPlanningRequest;
import com.jovan.erp_v1.response.ShiftPlanningResponse;

public interface IShiftPlanningService {

    ShiftPlanningResponse create(ShiftPlanningRequest request);

    ShiftPlanningResponse update(Long id, ShiftPlanningRequest request);

    void delete(Long id);

    ShiftPlanningResponse findOne(Long id);

    List<ShiftPlanningResponse> findAll();

    List<ShiftPlanningResponse> findByWorkCenter_NameContainingIgnoreCase(String name);

    List<ShiftPlanningResponse> findByWorkCenter_Capacity(BigDecimal capacity);

    List<ShiftPlanningResponse> findByWorkCenter_LocationContainingIgnoreCase(String location);

    List<ShiftPlanningResponse> findByEmployee_Id(Long id);

    List<ShiftPlanningResponse> findByEmployee_Email(String email);

    List<ShiftPlanningResponse> findByEmployee_UsernameContainingIgnoreCase(String username);

    List<ShiftPlanningResponse> findByEmployeeFirstAndLastName(String firstName, String lastName);

    List<ShiftPlanningResponse> findByEmployee_PhoneNumber(String phoneNumber);

    List<ShiftPlanningResponse> findByDate(LocalDate date);

    List<ShiftPlanningResponse> findByDateBetween(LocalDate start, LocalDate end);

    List<ShiftPlanningResponse> findByDateGreaterThanEqual(LocalDate date);

    List<ShiftPlanningResponse> findOrdersWithStartDateAfterOrEqual(LocalDate date);

    List<ShiftPlanningResponse> findByShiftType(ShiftType shiftType);

    List<ShiftPlanningResponse> findByAssigned(boolean assigned);

    List<ShiftPlanningResponse> findByEmployee_IdAndAssignedTrue(Long employeeId);

    List<ShiftPlanningResponse> findByEmployee_IdAndShiftType(Long employeeId, ShiftType shiftType);

    List<ShiftPlanningResponse> findByWorkCenter_IdAndDateAfterAndAssignedFalse(Long workCenterId, LocalDate date);

    List<ShiftPlanningResponse> findShiftsForEmployeeBetweenDates(Long employeeId, LocalDate start, LocalDate end);

    boolean existsByEmployee_IdAndDateAndShiftType(Long employeeId, LocalDate date, ShiftType shiftType);
}
