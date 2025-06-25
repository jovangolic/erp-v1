package com.jovan.erp_v1.response;

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

    public StorageBasicResponse(Storage s) {
        this.id = s.getId();
        this.name = s.getName();
    }
}