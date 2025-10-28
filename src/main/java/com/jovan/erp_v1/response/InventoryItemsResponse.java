package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.InventoryItemsStatus;
import com.jovan.erp_v1.model.InventoryItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemsResponse {

    private Long id;
    private InventoryForItemResponse inventoryForItemResponse;
    private ProductResponse productResponse;
    private BigDecimal quantity;
    private BigDecimal itemCondition;
    private BigDecimal difference;
    private Boolean confirmed;
    private InventoryItemsStatus status;

    public InventoryItemsResponse(InventoryItems items) {
        this.id = items.getId();
        this.inventoryForItemResponse = items.getInventory() != null ? new InventoryForItemResponse(items.getInventory()) : null;
        this.productResponse = items.getProduct() != null ? new ProductResponse(items.getProduct()) : null;
        this.quantity = items.getQuantity();
        this.itemCondition = items.getItemCondition();
        this.difference = items.getDifference();
        this.confirmed = items.getConfirmed();
        this.status = items.getStatus();
    }
}
