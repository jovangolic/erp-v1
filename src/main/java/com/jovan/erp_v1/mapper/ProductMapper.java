package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.ProductRequest;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.response.ProductResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final StorageRepository storageRepository;

    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setId(request.id()); // ako je potreban za update
        product.setCurrentQuantity(request.currentQuantity());
        //product.setBarCode(request.barCode());
        product.setName(request.name());
        product.setUnitMeasure(request.unitMeasure());
        product.setSupplierType(request.supplierType());
        product.setStorageType(request.storageType());
        product.setGoodsType(request.goodsType());
        
        Storage storage = storageRepository.findById(request.storageId())
                .orElseThrow(() -> new StorageNotFoundException("Storage not found"));
        product.setStorage(storage);

        return product;
    }

    // ✅ Mapiranje Product -> GoodsResponse
    public GoodsResponse toGoodsResponse(Product product) {
    	Objects.requireNonNull(product, "Goods must not be null");
        return new GoodsResponse(product); // koristi konstruktor koji već postoji
    }

    public List<GoodsResponse> toGoodsResponseList(List<Product> products) {
    	if(products == null || products.isEmpty()) {
    		return Collections.emptyList();
    	}
        return products.stream()
                .map(this::toGoodsResponse)
                .collect(Collectors.toList());
    }

    // ✅ Mapiranje Product -> ProductResponse
    public ProductResponse toProductResponse(Product product) {
    	Objects.requireNonNull(product, "Product must not be null");
        return new ProductResponse(product);
    }

    public List<ProductResponse> toProductResponseList(List<Product> products) {
    	if(products == null || products.isEmpty()) {
    		return Collections.emptyList();
    	}
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }
}
