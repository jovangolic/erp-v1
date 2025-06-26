package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.model.BillOfMaterials;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.request.BillOfMaterialsRequest;
import com.jovan.erp_v1.response.BillOfMaterialsResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BillOfMaterialsMapper {

    private final ProductRepository productRepository;

    public BillOfMaterials toEntity(BillOfMaterialsRequest request) {
        Objects.requireNonNull(request, "BillOfMaterialsRequest must not be null");
        return buildBillOfMaterialsFromRequest(new BillOfMaterials(), request);
    }

    public BillOfMaterials toUpdateEntity(BillOfMaterials bom, BillOfMaterialsRequest request) {
        Objects.requireNonNull(request, "BillOfMaterialsRequest must not be null");
        Objects.requireNonNull(bom, "BillOfMaterial must not be null");
        return buildBillOfMaterialsFromRequest(bom, request);
    }

    private BillOfMaterials buildBillOfMaterialsFromRequest(BillOfMaterials bom, BillOfMaterialsRequest request) {
        bom.setParentProduct(fetchProduct(request.parentProductId()));
        bom.setComponent(fetchComponent(request.componentId()));
        bom.setQuantity(request.quantity());
        return bom;
    }

    public BillOfMaterialsResponse toResponse(BillOfMaterials bom) {
        Objects.requireNonNull(bom, "BillOfMaterials must not be null");
        return new BillOfMaterialsResponse(bom);
    }

    public List<BillOfMaterialsResponse> toResponseList(List<BillOfMaterials> bom) {
        return bom.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Product fetchProduct(Long productId) {
        if (productId == null) {
            throw new NoSuchProductException("Product must not be null");
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchProductException("Product not found with id: " + productId));
    }

    private Product fetchComponent(Long componentId) {
        if (componentId == null) {
            throw new NoSuchProductException("Component ID must not be null");
        }
        return productRepository.findById(componentId)
                .orElseThrow(() -> new NoSuchProductException("Component not found with id: " + componentId));
    }
}