package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialRequirement;
import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.request.MaterialRequirementRequest;
import com.jovan.erp_v1.response.MaterialRequirementResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class MaterialRequirementMapper extends AbstractMapper<MaterialRequirementRequest> {

    public MaterialRequirement toEntity(MaterialRequirementRequest request,ProductionOrder productionOrder,Material material) {
        Objects.requireNonNull(request, "MaterialRequirementRequest must not be null");
        Objects.requireNonNull(productionOrder, "ProductionOrder must not be null");
        Objects.requireNonNull(material, "Material must not be null");
        validateIdForCreate(request, MaterialRequirementRequest::id);
        MaterialRequirement mr = new MaterialRequirement();
        mr.setId(request.id());
        mr.setProductionOrder(productionOrder);
        mr.setMaterial(material);
        mr.setRequiredQuantity(request.requiredQuantity());
        mr.setAvailableQuantity(request.availableQuantity());
        mr.setStatus(request.status());
        mr.setShortage(request.shortage());
        return mr;
    }

    public MaterialRequirement toUpdateEntity(MaterialRequirement mr, MaterialRequirementRequest request, ProductionOrder productionOrder,Material material) {
        Objects.requireNonNull(request, "MaterialRequirementRequest must not be null");
        Objects.requireNonNull(mr, "MaterialRequirement must not be null");
        Objects.requireNonNull(productionOrder, "ProductionOrder must not be null");
        Objects.requireNonNull(material, "Material must not be null");
        validateIdForUpdate(request, MaterialRequirementRequest::id);
        return buildMaterialRequirementFromRequest(mr, request,productionOrder,material);
    }

    private MaterialRequirement buildMaterialRequirementFromRequest(MaterialRequirement mr,
            MaterialRequirementRequest request, ProductionOrder productionOrder,Material material) {
        mr.setMaterial(material);
        mr.setProductionOrder(productionOrder);
        mr.setRequiredQuantity(request.requiredQuantity());
        mr.setAvailableQuantity(request.availableQuantity());
        if (request.requirementDate() != null) {
            mr.setRequirementDate(request.requirementDate());
        }
        mr.setStatus(request.status());
        mr.setShortage(request.shortage());
        return mr;
    }

    public MaterialRequirementResponse toResponse(MaterialRequirement req) {
        Objects.requireNonNull(req, "MaterialRequirement must not be null");
        return new MaterialRequirementResponse(req);
    }

    public List<MaterialRequirementResponse> toResponseList(List<MaterialRequirement> m) {
        if (m == null || m.isEmpty()) {
            return Collections.emptyList();
        }
        return m.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
