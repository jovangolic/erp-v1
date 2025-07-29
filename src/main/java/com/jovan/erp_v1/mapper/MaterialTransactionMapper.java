package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.MaterialNotFoundException;
import com.jovan.erp_v1.exception.SupplierNotFoundException;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.model.Material;
import com.jovan.erp_v1.model.MaterialTransaction;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.repository.MaterialRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.response.MaterialTransactionResponse;
import com.jovan.erp_v1.util.AbstractMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MaterialTransactionMapper extends AbstractMapper<MaterialTransactionRequest> {

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    public MaterialTransaction toEntity(MaterialTransactionRequest request) {
        Objects.requireNonNull(request, "MaterialTransactionRequest must not be null");
        validateIdForCreate(request, MaterialTransactionRequest::id);
        return buildMaterialTransactionFromRequest(new MaterialTransaction(), request);
    }

    public MaterialTransaction toUpdateEntity(MaterialTransaction mt, MaterialTransactionRequest request) {
        Objects.requireNonNull(request, "MaterialTransactionRequest must not be null");
        Objects.requireNonNull(mt, "MaterialTransaction must not be null");
        validateIdForUpdate(request, MaterialTransactionRequest::id);
        return buildMaterialTransactionFromRequest(mt, request);
    }

    private MaterialTransaction buildMaterialTransactionFromRequest(MaterialTransaction mt,
            MaterialTransactionRequest request) {
        mt.setMaterial(fetchMaterial(request.materialId()));
        mt.setQuantity(request.quantity());
        mt.setType(request.type());
        mt.setTransactionDate(request.transactionDate());
        mt.setVendor(fetchVendor(request.vendorId()));
        mt.setDocumentReference(request.documentReference());
        mt.setNotes(request.notes());
        mt.setStatus(request.status());
        mt.setCreatedByUser(fetchUser(request.createdByUserId()));
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

    private Material fetchMaterial(Long materialId) {
        if (materialId == null) {
            throw new MaterialNotFoundException("Material id must not be null");
        }
        return materialRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Material not found with id: " + materialId));
    }

    private User fetchUser(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException("User id must not be null");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found with id: " + userId));
    }

    private Vendor fetchVendor(Long vendorId) {
        if (vendorId == null) {
            throw new SupplierNotFoundException("Vendor id must not be null");
        }
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new SupplierNotFoundException("Vendor not found with id: " + vendorId));
    }
}
