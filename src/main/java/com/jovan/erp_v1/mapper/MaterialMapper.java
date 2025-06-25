package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.MaterialRequest;
import com.jovan.erp_v1.response.MaterialResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MaterialMapper {

    private final StorageRepository storageRepository;

    public Material toEntity(MaterialRequest request) {
        Objects.requireNonNull(request, "MaterialRequest cannot be null");
        return buildMaterialFromRequest(new Material(), request);
    }

    public void toUpdateEntity(Material m, MaterialRequest request) {
        Objects.requireNonNull(m, "Material entity cannot be null");
        Objects.requireNonNull(request, "MaterialRequest cannot be null");
        buildMaterialFromRequest(m, request);
    }

    private Material buildMaterialFromRequest(Material m, MaterialRequest request) {
        m.setCode(request.code());
        m.setName(request.name());
        m.setUnit(request.unit());
        m.setCurrentStock(request.currentStock());
        m.setStorage(fetchStorage(request.storageId()));
        m.setReorderLevel(request.reorderLevel());
        return m;
    }

    public MaterialResponse toResponse(Material m) {
        return new MaterialResponse(m);
    }

    public List<MaterialResponse> toResponseList(List<Material> m) {
        return m.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Storage fetchStorage(Long storageId) {
        if (storageId == null) {
            throw new StorageNotFoundException("Storage ID cannot be null");
        }
        return storageRepository.findById(storageId)
                .orElseThrow(() -> new StorageNotFoundException("Storage not found "));
    }
}
