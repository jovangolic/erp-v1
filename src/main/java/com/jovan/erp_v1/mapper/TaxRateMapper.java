package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.TaxRate;
import com.jovan.erp_v1.request.TaxRateRequest;
import com.jovan.erp_v1.response.TaxRateResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class TaxRateMapper extends AbstractMapper<TaxRateRequest> {

    public TaxRate toEntity(TaxRateRequest request) {
    	Objects.requireNonNull(request, "TaxeRateRequest must not be null");
    	validateIdForCreate(request, TaxRateRequest::id);
        TaxRate tax = new TaxRate();
        if (request.taxName() != null) {
            tax.setTaxName(request.taxName());
        }
        tax.setPercentage(request.percentage());
        tax.setEndDate(request.endDate());
        tax.setType(request.type());
        return tax;
    }

    public void toUpdateEntity(TaxRate tax, TaxRateRequest request) {
    	Objects.requireNonNull(tax, "TaxeRate must not be null");
    	Objects.requireNonNull(request, "TaxeRateRequest must not be null");
    	validateIdForUpdate(request, TaxRateRequest::id);
        if (request.taxName() != null) {
            tax.setTaxName(request.taxName());
        }
        tax.setPercentage(request.percentage());
        tax.setEndDate(request.endDate());
        if (request.type() != null) {
            tax.setType(request.type());
        }
    }

    public TaxRateResponse toResponse(TaxRate tax) {
    	Objects.requireNonNull(tax, "TaxeRate must not be null");
        return new TaxRateResponse(tax);
    }

    public List<TaxRateResponse> toResponseList(List<TaxRate> rates) {
    	if(rates == null || rates.isEmpty()) {
    		return Collections.emptyList();
    	}
        return rates.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
