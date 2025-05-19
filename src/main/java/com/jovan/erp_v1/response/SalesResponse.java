package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.model.Sales;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalesResponse {

	private Long id;
    private String buyerCompanyName;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private List<ItemSalesResponse> itemSales;
    private String salesDescription;

    public SalesResponse(Sales sales, List<ItemSalesResponse> itemSalesResponses) {
        this.id = sales.getId();
        this.buyerCompanyName = sales.getBuyer().getCompanyName();
        this.createdAt = sales.getCreatedAt();
        this.totalPrice = sales.getTotalPrice();
        this.itemSales = itemSalesResponses;
        this.salesDescription = sales.getSalesDescription();
    }
    public SalesResponse(Sales sales) {
        this.id = sales.getId();
        this.buyerCompanyName = sales.getBuyer().getCompanyName();
        this.createdAt = sales.getCreatedAt();
        this.totalPrice = sales.getTotalPrice();
        this.salesDescription = sales.getSalesDescription();
    }
}
