package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.model.ShiftReport;
import com.jovan.erp_v1.request.ShiftReportRequest;
import com.jovan.erp_v1.response.ShiftReportResponse;
public interface IShiftReportService {

	ShiftReport save(ShiftReportRequest request);
	ShiftReport update(Long id, ShiftReportRequest request);
    List<ShiftReportResponse> getAll();
    ShiftReportResponse getById(Long id);
    List<ShiftReportResponse> getByShiftId(Long shiftId);
    void delete(Long id);
    //nove metode
    List<ShiftReportResponse> findByDescription(String description);
    List<ShiftReportResponse> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<ShiftReportResponse> findByCreatedAtAfterOrEqual( LocalDateTime date);
    //createdBy
    List<ShiftReportResponse> findByCreatedBy_EmailLikeIgnoreCase( String email);
    List<ShiftReportResponse> findByCreatedBy_PhoneNumberLikeIgnoreCase(String phoneNumber);
    List<ShiftReportResponse> findByCreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(String firstName,String lastName);
    //shift
    List<ShiftReportResponse> findByRelatedShift_EndTimeBefore(LocalDateTime time);
    List<ShiftReportResponse> findByRelatedShift_EndTimeAfter(LocalDateTime time);
    List<ShiftReportResponse> findByRelatedShift_StartTimeAfter(LocalDateTime time);
    List<ShiftReportResponse> findByRelatedShift_StartTimeBefore(LocalDateTime time);
    List<ShiftReportResponse> findByRelatedShift_EndTimeBetween(LocalDateTime start, LocalDateTime end);
    List<ShiftReportResponse> findRelatedShift_ActiveShifts();
    List<ShiftReportResponse> findByRelatedShift_EndTimeIsNull();
    List<ShiftReportResponse> findByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(Long supervisorId, LocalDateTime start, LocalDateTime end);
    List<ShiftReportResponse> findByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(Long supervisorId);
}
