package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialRequest;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.request.MaterialRequestDTO;
import com.jovan.erp_v1.response.MaterialRequestResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class MaterialRequestMapper extends AbstractMapper<MaterialRequestDTO> {

    public MaterialRequest toEntity(MaterialRequestDTO dto, WorkCenter wc, Material material) {
        Objects.requireNonNull(dto, "WorkCenter must not be null");
        Objects.requireNonNull(wc, "MaterialRequestDTO must not be null");
        Objects.requireNonNull(material, "Material must not be null");
        validateIdForCreate(dto, MaterialRequestDTO::id);
        MaterialRequest mr = new MaterialRequest();
        mr.setId(dto.id());
        mr.setMaterial(material);
        mr.setRequestingWorkCenter(wc);
        mr.setQuantity(dto.quantity());
        mr.setNeededBy(dto.neededBy());
        mr.setStatus(dto.status());
        return mr;
    }

    public MaterialRequest toUpdateEntity(MaterialRequest req, MaterialRequestDTO dto, WorkCenter wc, Material material) {
        Objects.requireNonNull(dto, "MaterialRequestdDTO must not be null");
        Objects.requireNonNull(req, "MaterialRequestObject must not be null");
        Objects.requireNonNull(wc, "MaterialRequestDTO must not be null");
        Objects.requireNonNull(material, "Material must not be null");
        validateIdForUpdate(dto, MaterialRequestDTO::id);
        return buildMaterialRequestFromRequest(req, dto,wc,material);
    }

    private MaterialRequest buildMaterialRequestFromRequest(MaterialRequest req, MaterialRequestDTO dto, WorkCenter wc, Material material) {
        req.setMaterial(material);
        req.setRequestingWorkCenter(wc);
        req.setQuantity(dto.quantity());
        if (req.getNeededBy().isBefore(req.getRequestDate())) {
            throw new ValidationException("Datum potrebnosti ne može biti pre datuma zahteva.");
        }
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
}
