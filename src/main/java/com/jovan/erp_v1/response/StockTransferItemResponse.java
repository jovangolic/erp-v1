package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.StockTransferItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Double quantity;

    public StockTransferItemResponse(StockTransferItem item) {
        this.id = item.getId();
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.quantity = item.getQuantity();
        ;
    }
}
