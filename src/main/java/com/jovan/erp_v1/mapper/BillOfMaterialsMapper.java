package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.BillOfMaterials;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.request.BillOfMaterialsRequest;
import com.jovan.erp_v1.response.BillOfMaterialsResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class BillOfMaterialsMapper extends AbstractMapper<BillOfMaterialsRequest>{

    public BillOfMaterials toEntity(BillOfMaterialsRequest request, Product parentProduct, Product component) {
        Objects.requireNonNull(request, "BillOfMaterialsRequest must not be null");
        Objects.requireNonNull(parentProduct, "ParentProduct must not be null");
        Objects.requireNonNull(component, "Component must not be null");
        validateIdForCreate(request, BillOfMaterialsRequest::id);
        BillOfMaterials bom = new BillOfMaterials();
        bom.setId(request.id());
        bom.setParentProduct(parentProduct);
        bom.setComponent(component);
        bom.setQuantity(request.quantity());
        return bom;
    }

    public BillOfMaterials toUpdateEntity(BillOfMaterials bom, BillOfMaterialsRequest request, Product parentProduct, Product component) {
        Objects.requireNonNull(request, "BillOfMaterialsRequest must not be null");
        Objects.requireNonNull(bom, "BillOfMaterial must not be null");
        Objects.requireNonNull(parentProduct, "ParentProduct must not be null");
        Objects.requireNonNull(component, "Component must not be null");
        validateIdForUpdate(request, BillOfMaterialsRequest::id);
        return buildBillOfMaterialsFromRequest(bom, request,parentProduct,component);
    }

    private BillOfMaterials buildBillOfMaterialsFromRequest(BillOfMaterials bom, BillOfMaterialsRequest request, Product parentProduct, Product component) {
        bom.setParentProduct(parentProduct);
        bom.setComponent(component);
        bom.setQuantity(request.quantity());
        return bom;
    }

    public BillOfMaterialsResponse toResponse(BillOfMaterials bom) {
        Objects.requireNonNull(bom, "BillOfMaterials must not be null");
        return new BillOfMaterialsResponse(bom);
    }

    public List<BillOfMaterialsResponse> toResponseList(List<BillOfMaterials> bom) {
    	if(bom == null || bom.isEmpty()) {
    		return Collections.emptyList();
    	}
        return bom.stream().map(this::toResponse).collect(Collectors.toList());
    }
}