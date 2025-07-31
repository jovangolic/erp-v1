package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.request.MaterialRequest;
import com.jovan.erp_v1.response.MaterialResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class MaterialMapper extends AbstractMapper<MaterialRequest> {

    public Material toEntity(MaterialRequest request, Storage storage) {
        Objects.requireNonNull(request, "MaterialRequest cannot be null");
        Objects.requireNonNull(storage, "Storage cannot be null");
        validateIdForCreate(request, MaterialRequest::id);
        Material m = new Material();
        m.setId(request.id());
        m.setCode(request.code());
        m.setName(request.name());
        m.setUnit(request.unit());
        m.setCurrentStock(request.currentStock());
        m.setStorage(storage);
        m.setReorderLevel(request.reorderLevel());
        return m;
    }

    public void toUpdateEntity(Material m, MaterialRequest request, Storage storage) {
        Objects.requireNonNull(m, "Material entity cannot be null");
        Objects.requireNonNull(request, "MaterialRequest cannot be null");
        Objects.requireNonNull(storage, "Storage cannot be null");
        validateIdForUpdate(request, MaterialRequest::id);
        buildMaterialFromRequest(m, request,storage);
    }

    private Material buildMaterialFromRequest(Material m, MaterialRequest request, Storage storage) {
        m.setCode(request.code());
        m.setName(request.name());
        m.setUnit(request.unit());
        m.setCurrentStock(request.currentStock());
        m.setStorage(storage);
        m.setReorderLevel(request.reorderLevel());
        return m;
    }

    public MaterialResponse toResponse(Material m) {
        return new MaterialResponse(m);
    }

    public List<MaterialResponse> toResponseList(List<Material> m) {
        return m.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
