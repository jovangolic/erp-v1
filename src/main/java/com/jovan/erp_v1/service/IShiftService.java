package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;

public interface IShiftService {

	Shift save(ShiftRequest request);
	Shift update(Long id, ShiftRequest request);
    List<ShiftResponse> getAll();
    ShiftResponse getById(Long id);
    void delete(Long id);
    //nove metode
    List<ShiftResponse> findByEndTimeBefore(LocalDateTime time);
    List<ShiftResponse> findByEndTimeAfter(LocalDateTime time);
    List<ShiftResponse> findByStartTimeAfter(LocalDateTime time);
    List<ShiftResponse> findByStartTimeBefore(LocalDateTime time);
    List<ShiftResponse> findByEndTimeBetween(LocalDateTime start, LocalDateTime end);
    List<ShiftResponse> findActiveShifts();
    List<ShiftResponse> findByEndTimeIsNull();
    List<ShiftResponse> findByShiftSupervisorIdAndStartTimeBetween(Long supervisorId, LocalDateTime start, LocalDateTime end);
    List<ShiftResponse> findByShiftSupervisorIdAndEndTimeIsNull(Long supervisorId);
    //brojanje po datumu
    Integer countShiftsByStartDate(LocalDate date);
    Integer countShiftsByEndDate(LocalDate date);
    //Smena koja je trenutno u toku
    List<ShiftResponse> findCurrentShiftBySupervisor(Long supervisorId);
    //smene koje traju duze sati
    List<ShiftResponse> findShiftsLongerThan(Integer hours);
    //preklapanje smena sa odredjenim datumom
    List<ShiftResponse> findShiftsOverlappingPeriod(LocalDateTime start, LocalDateTime end);
    //nocna smena
    List<ShiftResponse> findNightShifts();
    //sledece smene
    List<ShiftResponse> findFutureShifts(LocalDateTime now);
    List<ShiftResponse> findAllFutureShifts();
    //supervisor
    List<ShiftResponse> findByShiftSupervisor_EmailLikeIgnoreCase( String email);
    List<ShiftResponse> findByShiftSupervisorPhoneNumberLikeIgnoreCase( String phoneNumber);
    List<ShiftResponse> findByShiftSupervisor_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(String firstName,  String lastName);
    //boolean
    boolean hasActiveShift(Long supervisorId);

}
