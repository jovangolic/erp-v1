package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.TaxType;
import com.jovan.erp_v1.model.TaxRate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxRateResponse {

    private Long id;
    private String taxName;
    private BigDecimal percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private TaxType type;

    public TaxRateResponse(TaxRate tax) {
        this.id = tax.getId();
        this.taxName = tax.getTaxName();
        this.percentage = tax.getPercentage();
        this.startDate = tax.getStartDate();
        this.endDate = tax.getEndDate();
        this.type = tax.getType();
    }
}
