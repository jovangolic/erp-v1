package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.WorkCenterResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class WorkCenterMapper extends AbstractMapper<WorkCenterRequest> {

    public WorkCenter toEntity(WorkCenterRequest request,Storage storage) {
        Objects.requireNonNull(request, "WorkCenterRequest must not be null");
        Objects.requireNonNull(storage, "Storage must not be null");
        validateIdForCreate(request, WorkCenterRequest::id);
        return buildWorkCenterFromRequest(new WorkCenter(), request,storage);
    }

    public WorkCenter toUpdateEntity(WorkCenter wc, WorkCenterRequest request,Storage storage) {
        Objects.requireNonNull(request, "WorkCenterRequest must not be null");
        Objects.requireNonNull(wc, "WorkCenter must not be null");
        Objects.requireNonNull(storage, "Storage must not be null");
        validateIdForUpdate(request, WorkCenterRequest::id);
        return buildWorkCenterFromRequest(wc, request,storage);
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

    private WorkCenter buildWorkCenterFromRequest(WorkCenter wc, WorkCenterRequest request, Storage storage) {
        wc.setName(request.name());
        wc.setLocation(request.location());
        wc.setCapacity(request.capacity());
        wc.setLocalStorage(storage);
        return wc;
    }
}
