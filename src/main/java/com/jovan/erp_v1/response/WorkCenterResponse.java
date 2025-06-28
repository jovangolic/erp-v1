package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.WorkCenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkCenterResponse {

    private Long id;
    private String name;
    private String location;
    private BigDecimal capacity;
    private StorageBasicResponse storageBasicResponse;

    public WorkCenterResponse(WorkCenter w) {
        this.id = w.getId();
        this.name = w.getName();
        this.location = w.getLocation();
        this.capacity = w.getCapacity();
        this.storageBasicResponse = new StorageBasicResponse(w.getLocalStorage());
    }
}
