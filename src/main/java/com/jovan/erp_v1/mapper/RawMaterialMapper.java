package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.request.RawMaterialRequest;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.response.RawMaterialResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RawMaterialMapper {

	private final StorageRepository storageRepository;
	private final UserRepository userRepository;

	public RawMaterial toEntity(RawMaterialRequest request) {
	    RawMaterial rawMaterial = new RawMaterial();
	    if (request.barCodes() != null) {
	        for (BarCodeRequest req : request.barCodes()) {
	            User scannedBy = null;
	            if (req.scannedById() != null) {
	                scannedBy = userRepository.findById(req.scannedById())
	                        .orElseThrow(() -> new IllegalArgumentException("Korisnik sa ID " + req.scannedById() + " nije pronađen."));
	            }

	            BarCode barCode = BarCode.builder()
	                    .code(req.code())
	                    .scannedAt(req.scannedAt())
	                    .scannedBy(scannedBy)
	                    .goods(rawMaterial) // ako koristiš bidirekcionu vezu
	                    .build();
	            rawMaterial.addBarCode(barCode);
	        }
	    }
	    rawMaterial.setId(request.id()); // za update
	    rawMaterial.setName(request.name());
	    rawMaterial.setUnitMeasure(request.unitMeasure());
	    rawMaterial.setSupplierType(request.supplierType());
	    rawMaterial.setStorageType(request.storageType());
	    rawMaterial.setGoodsType(request.goodsType());
	    Storage storage = storageRepository.findById(request.storageId())
	            .orElseThrow(() -> new StorageNotFoundException("Storage not found"));
	    rawMaterial.setStorage(storage);
	    return rawMaterial;
	}

    public RawMaterialResponse toResponse(RawMaterial rawMaterial) {
        return new RawMaterialResponse(rawMaterial);
    }

    public List<RawMaterialResponse> toResponseList(List<RawMaterial> materials) {
        return materials.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GoodsResponse toGoodsResponse(RawMaterial rawMaterial) {
        return new GoodsResponse(rawMaterial); // jer RawMaterial nasleđuje Goods
    }

    public List<GoodsResponse> toGoodsResponseList(List<RawMaterial> materials) {
        return materials.stream()
                .map(this::toGoodsResponse)
                .collect(Collectors.toList());
    }
	
}
