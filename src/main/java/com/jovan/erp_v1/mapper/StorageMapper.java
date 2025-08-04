package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class StorageMapper extends AbstractMapper<StorageRequest> {

    public Storage toEntity(StorageRequest request) {
    	Objects.requireNonNull(request, "StorageRequest must not be null");
    	validateIdForCreate(request, StorageRequest::id);
        Storage storage = new Storage();
        storage.setId(request.id());
        storage.setName(request.name());
        storage.setLocation(request.location());
        storage.setCapacity(request.capacity());
        storage.setType(request.type());
        storage.setShelves(new ArrayList<>());
        storage.setStatus(request.status());
        return storage;
    }
    
    public Storage toEntityUpdate(Storage st, StorageRequest request) {
    	Objects.requireNonNull(st, "Storage must not be null");
    	Objects.requireNonNull(request, "StorageRequest must not be null");
    	validateIdForUpdate(request, StorageRequest::id);
    	st.setName(request.name());
    	st.setLocation(request.location());
    	st.setCapacity(request.capacity());
    	st.setType(request.type());
    	st.setStatus(request.status());
    	return st;
    }

    public StorageResponse toResponse(Storage storage) {
    	Objects.requireNonNull(storage, "Storage must not be null");
        return new StorageResponse(storage);
    }

    public List<StorageResponse> toResponseList(List<Storage> storages) {
    	if(storages == null || storages.isEmpty()) {
    		return Collections.emptyList();
    	}
        return storages.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
