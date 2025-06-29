package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.MaterialNotFoundException;
import com.jovan.erp_v1.exception.ProductionOrderErrorException;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialRequirement;
import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.ProductionOrderRepository;
import com.jovan.erp_v1.request.MaterialRequirementRequest;
import com.jovan.erp_v1.response.MaterialRequirementResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MaterialRequirementMapper extends AbstractMapper<MaterialRequirementRequest> {

    private final MaterialRepository materialRepository;
    private final ProductionOrderRepository productionOrderRepository;

    public MaterialRequirement toEntity(MaterialRequirementRequest request) {
        Objects.requireNonNull(request, "MaterialRequirementRequest must not be null");
        validateIdForCreate(request, MaterialRequirementRequest::id);
        return buildMaterialRequirementFromRequest(new MaterialRequirement(), request);
    }

    public MaterialRequirement toUpdateEntity(MaterialRequirement mr, MaterialRequirementRequest request) {
        Objects.requireNonNull(request, "MaterialRequirementRequest must not be null");
        Objects.requireNonNull(mr, "MaterialRequirement must not be null");
        validateIdForUpdate(request, MaterialRequirementRequest::id);
        return buildMaterialRequirementFromRequest(mr, request);
    }

    private MaterialRequirement buildMaterialRequirementFromRequest(MaterialRequirement mr,
            MaterialRequirementRequest request) {
        mr.setMaterial(fetchMaterial(request.materialId()));
        mr.setProductionOrder(fetchProductionOrder(request.productionOrderId()));
        mr.setRequiredQuantity(request.requiredQuantity());
        mr.setAvailableQuantity(request.availableQuantity());
        mr.setRequirementDate(request.requirementDate());
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

    private Material fetchMaterial(Long materialId) {
        if (materialId == null) {
            throw new MaterialNotFoundException("Material id must not be null");
        }
        return materialRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Material not found with id: " + materialId));
    }

    private ProductionOrder fetchProductionOrder(Long productionOrderId) {
        if (productionOrderId == null) {
            throw new ProductionOrderErrorException("ProductionOrder id must not be null");
        }
        return productionOrderRepository.findById(productionOrderId)
                .orElseThrow(() -> new ProductionOrderErrorException(
                        "ProductionOrder not found with id: " + productionOrderId));
    }
}
