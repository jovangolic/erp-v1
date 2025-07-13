package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StorageMapper {

    private final ShelfMapper shelfMapper;

    public Storage toEntity(StorageRequest request) {
        Storage storage = new Storage();
        storage.setId(request.id());
        storage.setName(request.name());
        storage.setLocation(request.location());
        storage.setCapacity(request.capacity());
        storage.setType(request.type());
        List<Shelf> shelves = request.shelves().stream()
                .map(shelfRequest -> {
                    Shelf shelf = shelfMapper.toEntity(shelfRequest);
                    shelf.setStorage(storage); // Veza ka parent skladištu
                    return shelf;
                })
                .collect(Collectors.toList());
        storage.setShelves(shelves);
        storage.setStatus(request.status());
        return storage;
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
