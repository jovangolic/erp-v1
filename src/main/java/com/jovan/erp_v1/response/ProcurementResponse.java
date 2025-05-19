package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.Procurement;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcurementResponse {

	private Long id;
    private LocalDateTime date;
    private BigDecimal totalCost;
    private List<ItemSalesResponse> itemSales;  // Stavke prodaje
    private List<SupplyItemResponse> supplyItems;  // Stavke nabavke
    
    public ProcurementResponse(Procurement procurement) {
        this.id = procurement.getId();
        this.date = procurement.getDate();
        this.totalCost = procurement.getTotalCost();
        // Ako se stavke prodaje i nabavke povezuju, dodaj ih ovde
        this.itemSales = procurement.getItemSales().stream()
                                    .map(ItemSalesResponse::new)
                                    .collect(Collectors.toList());
        this.supplyItems = procurement.getSupplies().stream()
                                      .map(SupplyItemResponse::new)
                                      .collect(Collectors.toList());
    }
    
    public ProcurementResponse(Long id, LocalDateTime date, BigDecimal totalCost,
            List<ItemSalesResponse> itemSales, List<SupplyItemResponse> supplyItems) {
			this.id = id;
			this.date = date;
			this.totalCost = totalCost;
			this.itemSales = itemSales;
			this.supplyItems = supplyItems;
}
}
