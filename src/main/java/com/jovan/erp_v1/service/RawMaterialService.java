package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.RawMaterialNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.mapper.RawMaterialMapper;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.RawMaterialRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.RawMaterialRequest;
import com.jovan.erp_v1.response.RawMaterialResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RawMaterialService implements IRawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialMapper rawMaterialMapper;
    private final StorageRepository storageRepository;
    private final SupplyRepository supplyRepository;
    private final ProductRepository productRepository;

    @Override
    public List<RawMaterialResponse> findAll() {
        return rawMaterialMapper.toResponseList(rawMaterialRepository.findAll());
    }

    @Override
    public RawMaterialResponse findById(Long id) {
        RawMaterial material = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RawMaterialNotFoundException("RawMaterial not found"));
        return rawMaterialMapper.toResponse(material);
    }

    @Transactional
    @Override
    public RawMaterialResponse save(RawMaterialRequest request) {
        RawMaterial material = rawMaterialMapper.toEntity(request);
        return rawMaterialMapper.toResponse(rawMaterialRepository.save(material));
    }

    @Transactional
    @Override
    public RawMaterialResponse update(Long id, RawMaterialRequest request) {
        RawMaterial existing = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RawMaterialNotFoundException("RawMaterial not found with id: " + id));
        // Ažuriranje osnovnih podataka
        existing.setName(request.name());
        existing.setUnitMeasure(request.unitMeasure());
        existing.setSupplierType(request.supplierType());
        existing.setStorageType(request.storageType());
        existing.setGoodsType(request.goodsType());
        existing.setCurrentQuantity(request.currentQuantity());
        // Ažuriranje skladišta
        Storage storage = storageRepository.findById(request.storageId())
                .orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + request.storageId()));
        existing.setStorage(storage);
        // Ažuriranje supply-a
        if (request.supplyId() != null) {
            Supply supply = supplyRepository.findById(request.supplyId())
                    .orElseThrow(() -> new SupplyNotFoundException("Supply not found with id: " + request.supplyId()));
            existing.setSupply(supply);
        } else {
            existing.setSupply(null);
        }
        // Ažuriranje povezanog proizvoda
        if (request.productId() != null) {
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(
                            () -> new ProductNotFoundException("Product not found with id: " + request.productId()));
            existing.setProduct(product);
        } else {
            existing.setProduct(null);
        }
        // Ažuriranje bar kodova
        existing.getBarCodes().clear(); // zbog `orphanRemoval = true`
        if (request.barCodes() != null && !request.barCodes().isEmpty()) {
            List<BarCode> barCodes = request.barCodes().stream()
                    .map(barCodeReq -> BarCode.builder()
                            .code(barCodeReq.code())
                            .scannedAt(barCodeReq.scannedAt() != null ? barCodeReq.scannedAt() : LocalDateTime.now())
                            .scannedBy(barCodeReq.scannedBy() != null ? barCodeReq.scannedBy() : "system")
                            .goods(existing)
                            .build())
                    .toList();
            existing.getBarCodes().addAll(barCodes);
        }
        RawMaterial updated = rawMaterialRepository.save(existing);
        return rawMaterialMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!rawMaterialRepository.existsById(id)) {
            throw new RawMaterialNotFoundException("RawMaterial not found");
        }
        rawMaterialRepository.deleteById(id);
    }

    @Override
    public List<RawMaterialResponse> findByName(String name) {
        return rawMaterialMapper.toResponseList(
                rawMaterialRepository.findByNameContainingIgnoreCase(name));
    }
}
