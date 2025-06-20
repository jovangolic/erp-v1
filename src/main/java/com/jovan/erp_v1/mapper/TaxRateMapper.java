package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.TaxRate;
import com.jovan.erp_v1.request.TaxRateRequest;
import com.jovan.erp_v1.response.TaxRateResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaxRateMapper {

    public TaxRate toEntity(TaxRateRequest request) {
        TaxRate tax = new TaxRate();
        if (request.taxName() != null) {
            tax.setTaxName(request.taxName());
        }
        tax.setPercentage(request.percentage());
        tax.setStartDate(request.startDate());
        tax.setEndDate(request.endDate());
        tax.setType(request.type());
        return tax;
    }

    public void toUpdateEntity(TaxRate tax, TaxRateRequest request) {
        if (request.taxName() != null) {
            tax.setTaxName(request.taxName());
        }
        tax.setPercentage(request.percentage());
        tax.setStartDate(request.startDate());
        tax.setEndDate(request.endDate());
        if (request.type() != null) {
            tax.setType(request.type());
        }
    }

    public TaxRateResponse toResponse(TaxRate tax) {
        return new TaxRateResponse(tax);
    }

    public List<TaxRateResponse> toResponseList(List<TaxRate> rates) {
        return rates.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
