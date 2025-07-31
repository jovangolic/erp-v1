package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialTransaction;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.response.MaterialTransactionResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class MaterialTransactionMapper extends AbstractMapper<MaterialTransactionRequest> {

    public MaterialTransaction toEntity(MaterialTransactionRequest request, Material material,Vendor vendor,User createdByUser) {
        Objects.requireNonNull(request, "MaterialTransactionRequest must not be null");
        Objects.requireNonNull(material, "Material must not be null");
        Objects.requireNonNull(vendor, "Vendor must not be null");
        Objects.requireNonNull(createdByUser, "User must not be null");
        validateIdForCreate(request, MaterialTransactionRequest::id);
        MaterialTransaction mt = new MaterialTransaction();
        mt.setMaterial(material);
        mt.setQuantity(request.quantity());
        mt.setType(request.type());
        mt.setVendor(vendor);
        mt.setDocumentReference(request.documentReference());
        mt.setNotes(request.notes());
        mt.setStatus(request.status());
        mt.setCreatedByUser(createdByUser);
        return mt;
    }

    public MaterialTransaction toUpdateEntity(MaterialTransaction mt, MaterialTransactionRequest request, Material material,Vendor vendor,User createdByUser) {
        Objects.requireNonNull(request, "MaterialTransactionRequest must not be null");
        Objects.requireNonNull(mt, "MaterialTransaction must not be null");
        Objects.requireNonNull(material, "Material must not be null");
        Objects.requireNonNull(vendor, "Vendor must not be null");
        Objects.requireNonNull(createdByUser, "User must not be null");
        validateIdForUpdate(request, MaterialTransactionRequest::id);
        return buildMaterialTransactionFromRequest(mt, request,material,vendor,createdByUser);
    }

    private MaterialTransaction buildMaterialTransactionFromRequest(MaterialTransaction mt,
            MaterialTransactionRequest request, Material material,Vendor vendor,User createdByUser) {
        mt.setMaterial(material);
        mt.setQuantity(request.quantity());
        mt.setType(request.type());
        mt.setVendor(vendor);
        mt.setDocumentReference(request.documentReference());
        mt.setNotes(request.notes());
        mt.setStatus(request.status());
        mt.setCreatedByUser(createdByUser);
        return mt;
    }

    public MaterialTransactionResponse toResponse(MaterialTransaction mt) {
        Objects.requireNonNull(mt, "MaterialTransaction must not be null");
        return new MaterialTransactionResponse(mt);
    }

    public List<MaterialTransactionResponse> toResponseList(List<MaterialTransaction> mt) {
        if (mt == null || mt.isEmpty()) {
            return Collections.emptyList();
        }
        return mt.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
