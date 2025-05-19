package com.jovan.erp_v1.service;

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
}
