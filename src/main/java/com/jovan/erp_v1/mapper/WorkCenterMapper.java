package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.WorkCenterResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WorkCenterMapper {

    private final StorageRepository storageRepository;

    public WorkCenter toEntity(WorkCenterRequest request) {
        Objects.requireNonNull(request, "WorkCenterRequest must not be null");
        return buildWorkCenterFromRequest(new WorkCenter(), request);
    }

    public WorkCenter toUpdateEntity(WorkCenter wc, WorkCenterRequest request) {
        Objects.requireNonNull(request, "WorkCenterRequest must not be null");
        Objects.requireNonNull(wc, "WorkCenter must not be null");
        return buildWorkCenterFromRequest(wc, request);
    }

    public WorkCenterResponse toResponse(WorkCenter wc) {
        Objects.requireNonNull(wc, "WorkCenter must not be null");
        return new WorkCenterResponse(wc);
    }

    public List<WorkCenterResponse> toResponseList(List<WorkCenter> wc) {
        if (wc == null || wc.isEmpty()) {
            return Collections.emptyList();
        }
        return wc.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private WorkCenter buildWorkCenterFromRequest(WorkCenter wc, WorkCenterRequest request) {
        wc.setName(request.name());
        wc.setLocation(request.location());
        wc.setCapacity(request.capacity());
        wc.setLocalStorage(fetchStorage(request.localStorageId()));
        return wc;
    }

    private Storage fetchStorage(Long storageId) {
        if (storageId == null) {
            throw new StorageNotFoundException("Storage ID must not be null");
        }
        return storageRepository.findById(storageId)
                .orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + storageId));
    }
}
