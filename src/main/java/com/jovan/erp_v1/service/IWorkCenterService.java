package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.WorkCenterResponse;

public interface IWorkCenterService {

    WorkCenterResponse create(WorkCenterRequest request);

    WorkCenterResponse update(Long id, WorkCenterRequest request);

    void delete(Long id);

    WorkCenterResponse findOne(Long id);

    List<WorkCenterResponse> findAll();

    List<WorkCenterResponse> findByName(String name);

    List<WorkCenterResponse> findByCapacity(BigDecimal capacity);

    List<WorkCenterResponse> findByLocation(String location);

    List<WorkCenterResponse> findByNameAndLocation(String name, String location);

    List<WorkCenterResponse> findByCapacityGreaterThan(BigDecimal capacity);

    List<WorkCenterResponse> findByCapacityLessThan(BigDecimal capacity);

    List<WorkCenterResponse> findByNameContainingIgnoreCase(String name);

    List<WorkCenterResponse> findByLocationContainingIgnoreCase(String location);

    List<WorkCenterResponse> findByCapacityBetween(BigDecimal min, BigDecimal max);

    List<WorkCenterResponse> findByLocationOrderByCapacityDesc(String location);
}
