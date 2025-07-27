package com.jovan.erp_v1.response;

import java.time.LocalDate;
import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.model.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryForItemResponse {

	private Long id;
    private StorageEmployeeForUserResponse storageEmployeeForUserResponse;
    private StorageForemanForUserResponse storageForemanForUserResponse;
    private LocalDate date;
    private Boolean aligned;
    private InventoryStatus status;
    
    public InventoryForItemResponse(Inventory items) {
    	this.id = items.getId();
    	this.storageEmployeeForUserResponse = items.getStorageEmployee() != null
    			? new StorageEmployeeForUserResponse(items.getStorageEmployee()) : null;
    	this.storageForemanForUserResponse = items.getStorageForeman() != null 
    			? new StorageForemanForUserResponse(items.getStorageForeman()) : null;
    	this.date = items.getDate();
    	this.aligned = items.getAligned();
    	this.status = items.getStatus();
    }
}
