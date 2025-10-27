package com.jovan.erp_v1.statistics.inventory;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.InventoryTypeStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryForemanStatDTO {

	private Long count;
	private Long foremanId;
	private InventoryStatus status;
    private InventoryTypeStatus typeStatus;
	
	public InventoryForemanStatDTO(Long count, Long foremanId,InventoryStatus status,InventoryTypeStatus typeStatus) {
		this.count = count;
		this.foremanId = foremanId;
		this.status = status;
		this.typeStatus = typeStatus;
	}
}
