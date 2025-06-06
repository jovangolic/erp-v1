package com.jovan.erp_v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jovan.erp_v1.model.LogisticsProvider;
import com.jovan.erp_v1.request.LogisticsProviderRequest;
import com.jovan.erp_v1.response.LogisticsProviderResponse;

@Mapper(componentModel = "Spring")
public interface LogisticsProviderMapper {

    public LogisticsProvider toEntity(LogisticsProviderRequest request);

    public LogisticsProviderResponse toResponse(LogisticsProvider provider);

    public List<LogisticsProviderResponse> toResponseList(List<LogisticsProvider> providers);
}
