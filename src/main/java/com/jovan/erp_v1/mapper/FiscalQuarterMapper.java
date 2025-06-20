package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FiscalQuarterMapper {

    public FiscalQuarter toEntity(FiscalQuarterRequest request) {
        FiscalQuarter q = new FiscalQuarter();
        q.setStartDate(request.startDate());
        q.setEndDate(request.endDate());
        q.setQuarterStatus(request.quarterStatus());
        return q;
    }

    public void updateEntity(FiscalQuarter q, FiscalQuarterRequest request) {

        q.setQuarterStatus(request.quarterStatus());
        q.setStartDate(request.startDate());
        q.setEndDate(request.endDate());

    }

    public FiscalQuarterResponse toResponse(FiscalQuarter quarter) {
        return new FiscalQuarterResponse(quarter);
    }

    public List<FiscalQuarterResponse> toResponseList(List<FiscalQuarter> quarters) {
        return quarters.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
