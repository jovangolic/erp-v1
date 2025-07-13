package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageBasicResponse {
    private Long id;
    private String name;
    private String location;
	private BigDecimal capacity;
	private StorageType type;
	private StorageStatus status;

    public StorageBasicResponse(Storage s) {
        this.id = s.getId();
        this.name = s.getName();
        this.location = s.getLocation();
        this.capacity = s.getCapacity();
        this.type = s.getType();
        this.status = s.getStatus();
    }
}