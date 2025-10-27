package com.jovan.erp_v1.statistics.inventory;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.InventoryTypeStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryEmployeeStatDTO {

	private Long count;
	private Long employeeId;
	private InventoryStatus status;
    private InventoryTypeStatus typeStatus;
	
	public InventoryEmployeeStatDTO(Long count, Long employeeId,InventoryStatus status,InventoryTypeStatus typeStatus) {
		this.count = count;
		this.employeeId = employeeId;
		this.status = status;
		this.typeStatus = typeStatus;
	}
}
