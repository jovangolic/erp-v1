package com.jovan.erp_v1.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.model.Inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

        private Long id;
        private Long storageEmployeeId;
        private Long storageForemanId;
        private String storageEmployeeName;
        private String storageForemanName;
        private LocalDate date;
        private Boolean aligned;
        private List<InventoryItemsResponse> inventoryItems;
        private InventoryStatus status;

        public InventoryResponse(Inventory inventory) {
                this.id = inventory.getId();
                this.storageEmployeeId = inventory.getStorageEmployee().getId();
                this.storageForemanId = inventory.getStorageForeman().getId();
                this.storageEmployeeName = inventory.getStorageEmployee().getFirstName() + " "
                                + inventory.getStorageEmployee().getLastName();
                this.storageForemanName = inventory.getStorageForeman().getFirstName() + " "
                                + inventory.getStorageForeman().getLastName();
                this.date = inventory.getDate();
                this.aligned = inventory.getAligned();
                this.status = inventory.getStatus(); // ne koristi .name()
                this.inventoryItems = inventory.getInventoryItems().stream()
                                .map(InventoryItemsResponse::new)
                                .collect(Collectors.toList());
        }
}
