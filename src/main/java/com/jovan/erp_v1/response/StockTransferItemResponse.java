package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.StockTransferItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferItemResponse {

    private Long id;
    private ProductResponse productResponse;
    private StockTransferBasicResponse stockTransfer;
    private BigDecimal quantity;

    public StockTransferItemResponse(StockTransferItem item) {
        this.id = item.getId();
        this.productResponse = item.getStockTransfer() != null ? new ProductResponse(item.getProduct()) : null;
        this.quantity = item.getQuantity();
        this.stockTransfer = item.getStockTransfer() != null ? new StockTransferBasicResponse(item.getStockTransfer()) : null;
    }
}
