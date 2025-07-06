package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.request.ProcurementRequest;
import com.jovan.erp_v1.response.ProcurementResponse;

public interface IProcurementService {

	ProcurementResponse createProcurement(ProcurementRequest request);
	ProcurementResponse updateProcurement(Long id, ProcurementRequest request);
	void deleteProcurement(Long id);
	ProcurementResponse getByProcurementId(Long id);
	List<ProcurementResponse> getAllProcurement();
	List<ProcurementResponse> getByTotalCost(BigDecimal totalCost);
	List<ProcurementResponse> getByDateBetween(LocalDateTime start, LocalDateTime end);
	List<ProcurementResponse> getByTotalCostBetween(BigDecimal min, BigDecimal max);
	List<ProcurementResponse> getByTotalCostGreaterThan(BigDecimal totalCost);
	List<ProcurementResponse> getByTotalCostLessThan(BigDecimal totalCost);
}
