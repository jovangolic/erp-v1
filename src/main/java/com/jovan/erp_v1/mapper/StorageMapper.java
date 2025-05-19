package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StorageMapper {

	
	public Storage toEntity(StorageRequest request) {
        Storage storage = new Storage();
        storage.setId(request.id());
        storage.setName(request.name());
        storage.setLocation(request.location());
        storage.setCapacity(request.capacity());
        storage.setType(request.type());
        return storage;
    }

    public StorageResponse toResponse(Storage storage) {
        return new StorageResponse(storage);
    }

    public List<StorageResponse> toResponseList(List<Storage> storages) {
        return storages.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
