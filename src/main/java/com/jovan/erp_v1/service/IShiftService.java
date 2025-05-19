package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;

public interface IShiftService {

	Shift save(ShiftRequest request);
	Shift update(Long id, ShiftRequest request);
    List<ShiftResponse> getAll();
    ShiftResponse getById(Long id);
    void delete(Long id);
}
