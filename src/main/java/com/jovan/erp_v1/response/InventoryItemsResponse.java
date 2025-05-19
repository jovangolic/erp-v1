package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.InventoryItems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemsResponse {

	
	private Long id;             
    private Long inventoryId;
    private Long productId;
    private String productName;
    private Double quantity; 
    private Integer itemCondition;
    private Double difference;
    private String unitMeasure;
    
    public InventoryItemsResponse(InventoryItems items) {
    	this.id = items.getId();
    	this.inventoryId = items.getInventory().getId();
    	this.productId = items.getProduct().getId();
    	this.productName = items.getProduct().getName();
    	this.quantity = items.getQuantity();
    	this.itemCondition = items.getItemCondition();
    	this.difference = items.getDifference();
    	this.unitMeasure = items.getProduct().getUnitMeasure();
    }
}
