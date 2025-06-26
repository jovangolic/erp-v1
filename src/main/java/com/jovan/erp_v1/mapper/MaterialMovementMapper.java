package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.MaterialNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialMovement;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.MaterialMovementRequest;
import com.jovan.erp_v1.response.MaterialMovementResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MaterialMovementMapper {

    private final MaterialRepository materialRepository;
    private final StorageRepository storageRepository;

    public MaterialMovement toEntity(MaterialMovementRequest request) {
        Objects.requireNonNull(request, "MaterialMovementRequest must not be null");
        return buildMaterialMovementFromRequest(new MaterialMovement(), request);
    }

    public MaterialMovement toUpdateEntity(MaterialMovement m, MaterialMovementRequest request) {
        Objects.requireNonNull(request, "MaterialMovementRequest must not be null");
        Objects.requireNonNull(m, "MaterialMovement must not be null");
        return buildMaterialMovementFromRequest(m, request);
    }

    private MaterialMovement buildMaterialMovementFromRequest(MaterialMovement m, MaterialMovementRequest request) {
        m.setMaterial(fetchMaterial(request.materialId()));
        m.setMovementDate(request.movementDate());
        m.setType(request.type());
        m.setQuantity(request.quantity());
        m.setFromStorage(fetchFromStorage(request.fromStorageId()));
        m.setToStorage(fetchToStorage(request.toStorageId()));
        return m;
    }

    public MaterialMovementResponse toResponse(MaterialMovement m) {
        Objects.requireNonNull(m, "MaterialMovement must not be null");
        return new MaterialMovementResponse(m);
    }

    public List<MaterialMovementResponse> toResponseList(List<MaterialMovement> m) {
        if (m == null)
            return Collections.emptyList();
        return m.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Material fetchMaterial(Long materialId) {
        if (materialId == null) {
            throw new MaterialNotFoundException("Material id must not be null");
        }
        return materialRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Material not found with id: " + materialId));
    }

    private Storage fetchFromStorage(Long fromStorageId) {
        if (fromStorageId == null) {
            throw new StorageNotFoundException("FromStorage ID must not be null");
        }
        return storageRepository.findById(fromStorageId)
                .orElseThrow(() -> new StorageNotFoundException("FromStorage not found with id " + fromStorageId));
    }

    private Storage fetchToStorage(Long toStorageId) {
        if (toStorageId == null) {
            throw new StorageNotFoundException("ToStorage ID must not be null");
        }
        return storageRepository.findById(toStorageId)
                .orElseThrow(() -> new StorageNotFoundException("ToStorage not found with id: " + toStorageId));
    }
}
