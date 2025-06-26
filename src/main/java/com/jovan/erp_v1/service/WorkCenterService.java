package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.WorkCenterMapper;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.WorkCenterResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkCenterService implements IWorkCenterService {

    private final WorkCenterRepository workCenterRepository;
    private final WorkCenterMapper workCenterMapper;

    @Transactional
    @Override
    public WorkCenterResponse create(WorkCenterRequest request) {
        WorkCenter wc = workCenterMapper.toEntity(request);
        WorkCenter saved = workCenterRepository.save(wc);
        return workCenterMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public WorkCenterResponse update(Long id, WorkCenterRequest request) {
        WorkCenter wc = workCenterRepository.findById(id)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id: " + id));
        wc.setName(request.name());
        wc.setLocation(request.location());
        wc.setCapacity(request.capacity());
        WorkCenter updated = workCenterRepository.save(wc);
        return workCenterMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!workCenterRepository.existsById(id)) {
            throw new WorkCenterErrorException("WorkCenter not found with id: " + id);
        }
        workCenterRepository.deleteById(id);
    }

    @Override
    public WorkCenterResponse findOne(Long id) {
        WorkCenter wc = workCenterRepository.findById(id)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id: " + id));
        return new WorkCenterResponse(wc);
    }

    @Override
    public List<WorkCenterResponse> findAll() {
        return workCenterRepository.findAll().stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByName(String name) {
        return workCenterRepository.findByName(name).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacity(BigDecimal capacity) {
        return workCenterRepository.findByCapacity(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocation(String location) {
        return workCenterRepository.findByLocation(location).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByNameAndLocation(String name, String location) {
        return workCenterRepository.findByNameAndLocation(name, location).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityGreaterThan(BigDecimal capacity) {
        return workCenterRepository.findByCapacityGreaterThan(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityLessThan(BigDecimal capacity) {
        return workCenterRepository.findByCapacityLessThan(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByNameContainingIgnoreCase(String name) {
        return workCenterRepository.findByNameContainingIgnoreCase(name).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocationContainingIgnoreCase(String location) {
        return workCenterRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityBetween(BigDecimal min, BigDecimal max) {
        return workCenterRepository.findByCapacityBetween(min, max).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocationOrderByCapacityDesc(String location) {
        return workCenterRepository.findByLocationOrderByCapacityDesc(location).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

}
