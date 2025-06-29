package com.jovan.erp_v1.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.MaterialNotFoundException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialRequest;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.MaterialRequestDTO;
import com.jovan.erp_v1.response.MaterialRequestResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MaterialRequestMapper extends AbstractMapper<MaterialRequestDTO> {

    private final MaterialRepository materialRepository;
    private final WorkCenterRepository workCenterRepository;

    public MaterialRequest toEntity(MaterialRequestDTO dto) {
        Objects.requireNonNull(dto, "MaterialRequestDTO must not be null");
        validateIdForCreate(dto, MaterialRequestDTO::id);
        return buildMaterialRequestFromRequest(new MaterialRequest(), dto);
    }

    public MaterialRequest toUpdateEntity(MaterialRequest req, MaterialRequestDTO dto) {
        Objects.requireNonNull(dto, "MaterialRequestdDTO must not be null");
        Objects.requireNonNull(req, "MaterialRequestObject must not be null");
        validateIdForUpdate(dto, MaterialRequestDTO::id);
        return buildMaterialRequestFromRequest(req, dto);
    }

    private MaterialRequest buildMaterialRequestFromRequest(MaterialRequest req, MaterialRequestDTO dto) {
        req.setMaterial(fetchMaterial(dto.materialId()));
        req.setRequestingWorkCenter(fetchWorkCenter(dto.requestingWorkCenterId()));
        req.setQuantity(dto.quantity());
        req.setRequestDate(dto.requestDate());
        req.setNeededBy(dto.neededBy());
        req.setStatus(dto.status());
        return req;
    }

    public MaterialRequestResponse toResponse(MaterialRequest req) {
        Objects.requireNonNull(req, "MaterialRequest must not be null");
        return new MaterialRequestResponse(req);
    }

    public List<MaterialRequestResponse> toResponseList(List<MaterialRequest> req) {
        if (req == null || req.isEmpty()) {
            return Collections.emptyList();
        }
        return req.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Material fetchMaterial(Long materialId) {
        if (materialId == null) {
            throw new MaterialNotFoundException("Material id must not be null");
        }
        return materialRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Material not found with id: " + materialId));
    }

    private WorkCenter fetchWorkCenter(Long workCenterId) {
        if (workCenterId == null) {
            throw new MaterialNotFoundException("WorkCenter id must not be null");
        }
        return workCenterRepository.findById(workCenterId)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id: " + workCenterId));
    }
}
