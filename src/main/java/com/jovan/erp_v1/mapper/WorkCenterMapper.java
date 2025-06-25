package com.jovan.erp_v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.WorkCenterResponse;

@Mapper(componentModel = "Spring")
public interface WorkCenterMapper {

    public WorkCenter toEntity(WorkCenterRequest request);

    public WorkCenterResponse toResponse(WorkCenter wc);

    public List<WorkCenterResponse> toResponseList(List<WorkCenter> works);
}
