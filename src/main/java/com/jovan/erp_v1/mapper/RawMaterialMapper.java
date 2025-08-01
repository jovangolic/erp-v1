package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.request.RawMaterialRequest;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.response.RawMaterialResponse;
import com.jovan.erp_v1.util.AbstractMapper;



@Component
public class RawMaterialMapper extends AbstractMapper<RawMaterialRequest> {
	public RawMaterial toEntity(RawMaterialRequest request,Product product,Storage storage, Supply supply, Shelf shelf) {
		Objects.requireNonNull(request, "RawMaterialRequest must ot be null");
		Objects.requireNonNull(product, "Product must ot be null");
		Objects.requireNonNull(supply, "Supply must ot be null");
		Objects.requireNonNull(shelf, "Shelf must ot be null");
		validateIdForCreate(request, RawMaterialRequest::id);
	    RawMaterial rawMaterial = new RawMaterial();
	    rawMaterial.setId(request.id()); // za update
	    rawMaterial.setName(request.name());
	    rawMaterial.setBarCodes(new ArrayList<>());
	    rawMaterial.setUnitMeasure(request.unitMeasure());
	    rawMaterial.setSupplierType(request.supplierType());
	    rawMaterial.setStorageType(request.storageType());
	    rawMaterial.setGoodsType(request.goodsType());
	    rawMaterial.setStorage(storage);
	    rawMaterial.setProduct(product);
	    rawMaterial.setSupply(supply);
	    rawMaterial.setShelf(shelf);
	    return rawMaterial;
	}
	
	public RawMaterial toEntityUpdate(RawMaterial raw,RawMaterialRequest request,Product product,Storage storage, Supply supply, Shelf shelf) {
		Objects.requireNonNull(raw, "RawMaterial must ot be null");
		Objects.requireNonNull(request, "RawMaterialRequest must ot be null");
		Objects.requireNonNull(product, "Product must ot be null");
		Objects.requireNonNull(supply, "Supply must ot be null");
		Objects.requireNonNull(shelf, "Shelf must ot be null");
		validateIdForUpdate(request, RawMaterialRequest::id);
		return buildRawMaterialForRequest(raw,request,product,storage,supply,shelf);
	}

    private RawMaterial buildRawMaterialForRequest(RawMaterial raw, RawMaterialRequest request, Product product,
			Storage storage, Supply supply, Shelf shelf) {
		raw.setName(request.name());
		raw.setBarCodes(new ArrayList<>());
		raw.setUnitMeasure(request.unitMeasure());
		raw.setSupplierType(request.supplierType());
		raw.setStorageType(request.storageType());
		raw.setGoodsType(request.goodsType());
		raw.setStorage(storage);
		raw.setProduct(product);
		raw.setSupply(supply);
		raw.setShelf(shelf);
		return raw;
	}

	public RawMaterialResponse toResponse(RawMaterial rawMaterial) {
    	Objects.requireNonNull(rawMaterial, "RawMaterial must not be null");
        return new RawMaterialResponse(rawMaterial);
    }

    public List<RawMaterialResponse> toResponseList(List<RawMaterial> materials) {
    	if(materials == null || materials.isEmpty()) {
    		return Collections.emptyList();
    	}
        return materials.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GoodsResponse toGoodsResponse(RawMaterial rawMaterial) {
    	Objects.requireNonNull(rawMaterial, "RawMaterial must not be null");
        return new GoodsResponse(rawMaterial); // jer RawMaterial nasleđuje Goods
    }

    public List<GoodsResponse> toGoodsResponseList(List<RawMaterial> materials) {
    	if(materials == null || materials.isEmpty()) {
    		return Collections.emptyList();
    	}
        return materials.stream()
                .map(this::toGoodsResponse)
                .collect(Collectors.toList());
    }
	
}
