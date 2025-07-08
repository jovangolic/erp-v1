package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.mapper.WorkCenterMapper;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.WorkCenterResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkCenterService implements IWorkCenterService {

    private final WorkCenterRepository workCenterRepository;
    private final WorkCenterMapper workCenterMapper;
    private final StorageRepository storageRepository;

    @Transactional
    @Override
    public WorkCenterResponse create(WorkCenterRequest request) {
        validateString(request.name());
        validateString(request.location());
        validateBigDecimal(request.capacity());
        validateStorageId(request.localStorageId());
        WorkCenter wc = workCenterMapper.toEntity(request);
        WorkCenter saved = workCenterRepository.save(wc);
        return workCenterMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public WorkCenterResponse update(Long id, WorkCenterRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        WorkCenter wc = workCenterRepository.findById(id)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id: " + id));
        validateString(request.name());
        validateString(request.location());
        validateBigDecimal(request.capacity());
        validateStorageId(request.localStorageId());
        Storage st = storageRepository.findById(request.localStorageId()).orElseThrow(
                () -> new StorageNotFoundException("Storage not found with id: " + request.localStorageId()));
        wc.setName(request.name());
        wc.setLocation(request.location());
        wc.setCapacity(request.capacity());
        wc.setLocalStorage(st);
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
        validateString(name);
        return workCenterRepository.findByName(name).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacity(BigDecimal capacity) {
        validateBigDecimal(capacity);
        return workCenterRepository.findByCapacity(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocation(String location) {
        validateString(location);
        return workCenterRepository.findByLocation(location).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByNameAndLocation(String name, String location) {
        validateDoubleString(name, location);
        return workCenterRepository.findByNameAndLocation(name, location).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityGreaterThan(BigDecimal capacity) {
        validateBigDecimal(capacity);
        return workCenterRepository.findByCapacityGreaterThan(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityLessThan(BigDecimal capacity) {
        validateBigDecimal(capacity);
        return workCenterRepository.findByCapacityLessThan(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByNameContainingIgnoreCase(String name) {
        validateString(name);
        return workCenterRepository.findByNameContainingIgnoreCase(name).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocationContainingIgnoreCase(String location) {
        validateString(location);
        return workCenterRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByCapacityBetween(BigDecimal min, BigDecimal max) {
        validateMinAndMax(min, max);
        return workCenterRepository.findByCapacityBetween(min, max).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocationOrderByCapacityDesc(String location) {
        validateString(location);
        return workCenterRepository.findByLocationOrderByCapacityDesc(location).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_Id(Long localStorageId) {
        validateStorageId(localStorageId);
        return workCenterRepository.findByLocalStorage_Id(localStorageId).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_NameContainingIgnoreCase(String localStorageName) {
        validateString(localStorageName);
        return workCenterRepository.findByLocalStorage_NameContainingIgnoreCase(localStorageName).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_LocationContainingIgnoreCase(String localStorageLocation) {
        validateString(localStorageLocation);
        return workCenterRepository.findByLocalStorage_LocationContainingIgnoreCase(localStorageLocation).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_Capacity(BigDecimal capacity) {
        validateBigDecimal(capacity);
        return workCenterRepository.findByLocalStorage_Capacity(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_CapacityLessThan(BigDecimal capacity) {
        validateBigDecimal(capacity);
        return workCenterRepository.findByLocalStorage_CapacityLessThan(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_CapacityGreaterThan(BigDecimal capacity) {
        validateBigDecimal(capacity);
        return workCenterRepository.findByLocalStorage_CapacityGreaterThan(capacity).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkCenterResponse> findByLocalStorage_Type(StorageType localStorageType) {
        validateStorageType(localStorageType);
        return workCenterRepository.findByLocalStorage_Type(localStorageType).stream()
                .map(WorkCenterResponse::new)
                .collect(Collectors.toList());
    }

    private void validateWorkCenter(Long workCenterId) {
        if (workCenterId == null) {
            throw new IllegalArgumentException("ID za workCenter ne sme biti null");
        }
    }

    private void validateStorageId(Long storageId) {
        if (storageId == null) {
            throw new IllegalArgumentException("ID za skladiste ne sme biti null");
        }
    }

    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

    private void validateStorageType(StorageType type) {
        if (type == null) {
            throw new IllegalArgumentException("Unit za StorageType ne sme biti null");
        }
    }

    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }

    private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Min i Max ne smeju biti null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Min mora biti >= 0, a Max mora biti > 0");
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Min ne može biti veći od Max");
        }
    }

    private void validateDoubleString(String name, String loc) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("String name ne sme biti null ili prazan");
        }
        if (loc == null || loc.trim().isEmpty()) {
            throw new IllegalArgumentException("String lokacija ne sme biti null ili prazan");
        }
    }

}
