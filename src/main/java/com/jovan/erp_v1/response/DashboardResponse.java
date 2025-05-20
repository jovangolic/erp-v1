package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalUsers;
    private Long totalInventories;
    private Long totalSalesOrders;
    private Long totalVendors;
    private Long totalProducts;
    private BigDecimal totalRevenue;
    private BigDecimal totalProcurementCost;
    private Integer activeShifts;
    private Integer pendingOrders;
    private Map<LocalDate, BigDecimal> dailySalesLast7Days;

}
