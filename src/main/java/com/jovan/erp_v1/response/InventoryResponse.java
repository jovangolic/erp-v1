package com.jovan.erp_v1.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.InventoryTypeStatus;
import com.jovan.erp_v1.model.Inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

        private Long id;
        private StorageEmployeeForUserResponse storageEmployeeForUserResponse;
        private StorageForemanForUserResponse storageForemanForUserResponse;
        private LocalDate date;
        private Boolean aligned;
        private List<InventoryItemsResponse> inventoryItems;
        private InventoryStatus status;
        private InventoryTypeStatus typeStatus;
        private Boolean confirmed;

        public InventoryResponse(Inventory inventory) {
                this.id = inventory.getId();
                this.storageEmployeeForUserResponse = inventory.getStorageEmployee() != null ? new StorageEmployeeForUserResponse(inventory.getStorageEmployee()) : null;
                this.storageForemanForUserResponse = inventory.getStorageForeman() != null ? new StorageForemanForUserResponse(inventory.getStorageForeman()) : null;
                this.date = inventory.getDate();
                this.aligned = inventory.getAligned();
                this.status = inventory.getStatus(); // ne koristi .name()
                this.inventoryItems = inventory.getInventoryItems().stream()
                                .map(InventoryItemsResponse::new)
                                .collect(Collectors.toList());
                this.typeStatus = inventory.getTypeStatus();
                this.confirmed = inventory.getConfirmed();
        }
}
