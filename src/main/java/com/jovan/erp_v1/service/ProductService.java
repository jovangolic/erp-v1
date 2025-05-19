package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.mapper.ProductMapper;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.request.ProductRequest;
import com.jovan.erp_v1.response.ProductResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Product saved = productRepository.save(product);
        return productMapper.toProductResponse(saved);
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
    	Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        product.setCurrentQuantity(request.currentQuantity());
        product.setName(request.name());
        product.setUnitMeasure(request.unitMeasure());
        product.setSupplierType(request.supplierType());
        product.setStorageType(request.storageType());
        product.setGoodsType(request.goodsType());

        Storage storage = storageRepository.findById(request.storageId())
                .orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
        product.setStorage(storage);
        // **Update barCode liste**
        updateBarCodes(product, request.barCodes());
        Product updated = productRepository.save(product);
        return productMapper.toProductResponse(updated);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
        	throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> findAll() {
        return productMapper.toProductResponseList(productRepository.findAll());
    }

    @Override
    public ProductResponse findByBarCode(String code) {
        Product product = productRepository.findByBarCodes(code).orElseThrow(() -> new ProductNotFoundException("Product not found with barCode: "+code));
        if (product == null) {
            throw new ProductNotFoundException("Product not found with bar code: " + code);
        }
        return productMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> findByCurrentQuantityLessThan(Double quantity) {
        List<Product> products = productRepository.findByCurrentQuantityLessThan(quantity);
        return productMapper.toProductResponseList(products);
    }

    @Override
    public List<ProductResponse> findByName(String name) {
        return productMapper.toProductResponseList(productRepository.findByName(name));
    }

    @Override
    public List<ProductResponse> findByStorageId(Long storageId) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + storageId));

        List<Product> products = productRepository.findByStorage(storage);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for storage id: " + storageId);
        }

        return productMapper.toProductResponseList(products);
    }

    @Override
    public List<ProductResponse> findBySupplierType(SupplierType supplierType) {
        return productMapper.toProductResponseList(productRepository.findBySupplierType(supplierType));
    }

    @Override
    public List<ProductResponse> findByStorageType(StorageType storageType) {
        return productMapper.toProductResponseList(productRepository.findByStorageType(storageType));
    }

    @Override
    public List<ProductResponse> findByGoodsType(GoodsType goodsType) {
        return productMapper.toProductResponseList(productRepository.findByGoodsType(goodsType));
    }
    
    private void updateBarCodes(Product product, List<BarCodeRequest> barCodeRequests) {
        // Kreiraj mapu postojećih BarCode entiteta po ID (ako imaju ID)
        Map<Long, BarCode> existingBarCodesById = product.getBarCodes().stream()
                .filter(bc -> bc.getId() != null)
                .collect(Collectors.toMap(BarCode::getId, Function.identity()));
        List<BarCode> updatedBarCodes = new ArrayList<>();
        for (BarCodeRequest bcRequest : barCodeRequests) {
            if (bcRequest.id() != null && existingBarCodesById.containsKey(bcRequest.id())) {
                // Postojeći barCode - ažuriraj polja
                BarCode existing = existingBarCodesById.get(bcRequest.id());
                existing.setCode(bcRequest.code());
                existing.setScannedAt(bcRequest.scannedAt());
                existing.setScannedBy(bcRequest.scannedBy());
                updatedBarCodes.add(existing);
                // Ukloni iz mape da bi znao šta je obrađeno
                existingBarCodesById.remove(bcRequest.id());
            } else {
                // Novi barCode - napravi novi objekat i poveži sa proizvodom
                BarCode newBarCode = BarCode.builder()
                        .code(bcRequest.code())
                        .scannedAt(bcRequest.scannedAt())
                        .scannedBy(bcRequest.scannedBy())
                        .goods(product) // poveži sa proizvodom
                        .build();
                updatedBarCodes.add(newBarCode);
            }
        }
        // BarCode entiteti koji su ostali u existingBarCodesById mapi su obrisani u request-u,
        // pa ih treba ukloniti iz proizvoda (orphanRemoval = true će ih obrisati iz baze)
        product.getBarCodes().clear();
        product.getBarCodes().addAll(updatedBarCodes);
    }
}
