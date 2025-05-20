package com.jovan.erp_v1.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentSalesDTO {

    private String buyerName;
    private BigDecimal amount;
    private LocalDateTime saleDate;
}
