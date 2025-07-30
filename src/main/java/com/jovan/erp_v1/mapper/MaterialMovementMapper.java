package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialMovement;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.request.MaterialMovementRequest;
import com.jovan.erp_v1.response.MaterialMovementResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class MaterialMovementMapper extends AbstractMapper<MaterialMovementRequest> {

    public MaterialMovement toEntity(MaterialMovementRequest request, Material material,Storage fromStorage,Storage toStorage) {
        Objects.requireNonNull(request, "MaterialMovementRequest must not be null");
        Objects.requireNonNull(material, "Material must not be null");
        Objects.requireNonNull(fromStorage, "FromStorage must not be null");
        Objects.requireNonNull(toStorage, "ToStorage must not be null");
        validateIdForCreate(request, MaterialMovementRequest::id);
        MaterialMovement mm = new MaterialMovement();
        mm.setMaterial(material);
        mm.setType(request.type());
        mm.setQuantity(request.quantity());
        mm.setFromStorage(fromStorage);
        mm.setToStorage(toStorage);
        return mm;
    }

    public MaterialMovement toUpdateEntity(MaterialMovement m, MaterialMovementRequest request, Material material,Storage fromStorage,Storage toStorage) {
        Objects.requireNonNull(request, "MaterialMovementRequest must not be null");
        Objects.requireNonNull(m, "MaterialMovement must not be null");
        Objects.requireNonNull(material, "Material must not be null");
        Objects.requireNonNull(fromStorage, "FromStorage must not be null");
        Objects.requireNonNull(toStorage, "ToStorage must not be null");
        validateIdForUpdate(request, MaterialMovementRequest::id);
        return buildMaterialMovementFromRequest(m, request,material,fromStorage,toStorage);
    }

    private MaterialMovement buildMaterialMovementFromRequest(MaterialMovement m, MaterialMovementRequest request, Material material,Storage fromStorage,Storage toStorage) {
        m.setMaterial(material);
        m.setType(request.type());
        m.setQuantity(request.quantity());
        m.setFromStorage(fromStorage);
        m.setToStorage(toStorage);
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
}
