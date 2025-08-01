package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.request.ProductRequest;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.response.ProductResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class ProductMapper extends AbstractMapper<ProductRequest> {

    public Product toEntity(ProductRequest request,Storage storage,Supply supply, Shelf shelf) {
    	Objects.requireNonNull(request, "ProductRequest must not be null");
    	Objects.requireNonNull(storage, "Storage must not be null");
    	Objects.requireNonNull(supply, "Supply must not be null");
    	Objects.requireNonNull(shelf, "Shelf must not be null");
    	validateIdForCreate(request, ProductRequest::id);
        Product product = new Product();
        product.setId(request.id()); // ako je potreban za update
        product.setCurrentQuantity(request.currentQuantity());
        product.setBarCodes(new ArrayList<>());
        product.setName(request.name());
        product.setUnitMeasure(request.unitMeasure());
        product.setSupplierType(request.supplierType());
        product.setStorageType(request.storageType());
        product.setGoodsType(request.goodsType());
        product.setStorage(storage);
        product.setSupply(supply);
        product.setShelf(shelf);
        return product;
    }
    
    public Product toEntityUpdate(Product product,ProductRequest request,Storage storage,Supply supply, Shelf shelf) {
    	Objects.requireNonNull(product, "Product must not be null");
    	Objects.requireNonNull(request, "ProductRequest must not be null");
    	Objects.requireNonNull(storage, "Storage must not be null");
    	Objects.requireNonNull(supply, "Supply must not be null");
    	Objects.requireNonNull(shelf, "Shelf must not be null");
    	validateIdForUpdate(request, ProductRequest::id);
    	return buildProductForRequest(product,request,storage,supply,shelf);
    }
    
    

    private Product buildProductForRequest(Product product, ProductRequest request, Storage storage, Supply supply,Shelf shelf) {
    	product.setName(request.name());
    	product.setUnitMeasure(request.unitMeasure());
    	product.setSupplierType(request.supplierType());
    	product.setStorageType(request.storageType());
    	product.setGoodsType(request.goodsType());
    	product.setStorage(storage);
    	product.setSupply(supply);
    	product.setShelf(shelf);
    	product.setCurrentQuantity(request.currentQuantity());
    	product.setBarCodes(new ArrayList<>());
		return product;
	}

	public GoodsResponse toGoodsResponse(Product product) {
    	Objects.requireNonNull(product, "Goods must not be null");
        return new GoodsResponse(product); 
    }

    public List<GoodsResponse> toGoodsResponseList(List<Product> products) {
    	if(products == null || products.isEmpty()) {
    		return Collections.emptyList();
    	}
        return products.stream()
                .map(this::toGoodsResponse)
                .collect(Collectors.toList());
    }

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
