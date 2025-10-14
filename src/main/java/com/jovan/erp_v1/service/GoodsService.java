package com.jovan.erp_v1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.mapper.ProductMapper;
import com.jovan.erp_v1.mapper.RawMaterialMapper;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.RawMaterialRepository;
import com.jovan.erp_v1.repository.specification.GoodsSpecification;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.search_request.GoodsSearchRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsService implements IGoodsService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductMapper productMapper;
    private final RawMaterialMapper rawMaterialMapper;
    private final GoodsRepository goodsRepository;

    @Override
    public List<GoodsResponse> findByName(String name) {
        List<GoodsResponse> products = productMapper.toGoodsResponseList(productRepository.findByName(name));
        List<GoodsResponse> rawMaterials = rawMaterialMapper
                .toGoodsResponseList(rawMaterialRepository.findByName(name));
        return mergeResults(products, rawMaterials);
    }

    @Override
    public List<GoodsResponse> findByBarCodes(String barCode) {
        List<Goods> goodsList = goodsRepository.findAllByBarCodes(barCode);
        if (goodsList.isEmpty()) {
            throw new GoodsNotFoundException("No goods found with barcode: " + barCode);
        }
        List<GoodsResponse> responses = new ArrayList<>();
        for (Goods goods : goodsList) {
            if (goods instanceof Product product) {
                responses.add(productMapper.toGoodsResponse(product));
            } else if (goods instanceof RawMaterial rawMaterial) {
                responses.add(rawMaterialMapper.toGoodsResponse(rawMaterial));
            }
        }
        return responses;
    }

    @Override
    public List<GoodsResponse> findByUnitMeasure(UnitMeasure unitMeasure) {
        List<GoodsResponse> products = productMapper
                .toGoodsResponseList(productRepository.findByUnitMeasure(unitMeasure));
        List<GoodsResponse> rawMaterials = rawMaterialMapper
                .toGoodsResponseList(rawMaterialRepository.findByUnitMeasure(unitMeasure));
        return mergeResults(products, rawMaterials);
    }

    @Override
    public List<GoodsResponse> findBySupplierType(SupplierType type) {
        List<GoodsResponse> products = productMapper.toGoodsResponseList(productRepository.findBySupplierType(type));
        List<GoodsResponse> rawMaterials = rawMaterialMapper
                .toGoodsResponseList(rawMaterialRepository.findBysupplierType(type));
        return mergeResults(products, rawMaterials);
    }

    @Override
    public List<GoodsResponse> findByStorageType(StorageType type) {
        List<GoodsResponse> products = productMapper.toGoodsResponseList(productRepository.findByStorageType(type));
        List<GoodsResponse> rawMaterials = rawMaterialMapper
                .toGoodsResponseList(rawMaterialRepository.findByStorageType(type));
        return mergeResults(products, rawMaterials);
    }

    @Override
    public List<GoodsResponse> findByGoodsType(GoodsType type) {
        List<GoodsResponse> products = productMapper.toGoodsResponseList(productRepository.findByGoodsType(type));
        List<GoodsResponse> rawMaterials = rawMaterialMapper
                .toGoodsResponseList(rawMaterialRepository.findByGoodsType(type));
        return mergeResults(products, rawMaterials);
    }

    @Override
    public List<GoodsResponse> findByStorageName(String storageName) {
        List<GoodsResponse> products = productMapper
                .toGoodsResponseList(productRepository.findByStorageName(storageName));
        List<GoodsResponse> rawMaterials = rawMaterialMapper
                .toGoodsResponseList(rawMaterialRepository.findByStorageName(storageName));
        return mergeResults(products, rawMaterials);
    }

    @Override
    public List<GoodsResponse> findBySupplyId(Long supplyId) {
        List<GoodsResponse> products = productMapper.toGoodsResponseList(productRepository.findBySupplyId(supplyId));
        List<GoodsResponse> rawMaterials = rawMaterialMapper
                .toGoodsResponseList(rawMaterialRepository.findBySupplyId(supplyId));
        return mergeResults(products, rawMaterials);
    }

    @Override
    public List<GoodsResponse> findByBarCodeAndGoodsType(String barCode, GoodsType goodsType) {
        List<Goods> goodsList = goodsRepository.findAllByBarCodesAndGoodsType(barCode, goodsType);
        return mapGoodsListToResponse(goodsList);
    }

    @Override
    public List<GoodsResponse> findByBarCodeAndStorageId(String barCode, Long storageId) {
        List<Goods> goodsList = goodsRepository.findAllByBarCodesAndStorageId(barCode, storageId);
        return mapGoodsListToResponse(goodsList);
    }

    @Override
    public GoodsResponse findSingleByBarCode(String barCode) {
        Goods goods = goodsRepository.findGoodsByBarCode(barCode)
                .orElseThrow(() -> new GoodsNotFoundException("No goods found for barcode: " + barCode));
        return mapGoodsToResponse(goods);
    }
    
    @Override
    public List<GoodsResponse> findByStorageId(Long storageId) {
        return goodsRepository.findByStorageId(storageId).stream()
                .map(GoodsResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<GoodsResponse> generalSearch(GoodsSearchRequest request) {
		Specification<Goods> spec = GoodsSpecification.fromRequest(request);
		List<Goods> items = goodsRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Goods for given criteria found");
		}
		return items.stream().map(GoodsResponse::new).collect(Collectors.toList());
	}

    private List<GoodsResponse> mergeResults(List<GoodsResponse> list1, List<GoodsResponse> list2) {
        List<GoodsResponse> combined = new ArrayList<>();
        combined.addAll(list1);
        combined.addAll(list2);
        return combined;
    }

    private List<GoodsResponse> mapGoodsListToResponse(List<Goods> goodsList) {
        List<GoodsResponse> responseList = new ArrayList<>();
        for (Goods goods : goodsList) {
            responseList.add(mapGoodsToResponse(goods));
        }
        return responseList;
    }

    private GoodsResponse mapGoodsToResponse(Goods goods) {
        if (goods instanceof Product product) {
            return productMapper.toGoodsResponse(product);
        } else if (goods instanceof RawMaterial rawMaterial) {
            return rawMaterialMapper.toGoodsResponse(rawMaterial);
        } else {
            throw new IllegalStateException("Unknown Goods subtype: " + goods.getClass().getSimpleName());
        }
    }
}
