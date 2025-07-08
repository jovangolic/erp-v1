package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.BillOfMaterialsErrorException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.mapper.BillOfMaterialsMapper;
import com.jovan.erp_v1.model.BillOfMaterials;
import com.jovan.erp_v1.repository.BillOfMaterialsRepository;
import com.jovan.erp_v1.request.BillOfMaterialsRequest;
import com.jovan.erp_v1.response.BillOfMaterialsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillOfMaterialsService implements IBillOfMaterialsService {

    private final BillOfMaterialsRepository billOfMaterialsRepository;
    private final BillOfMaterialsMapper billOfMaterialsMapper;

    @Transactional
    @Override
    public BillOfMaterialsResponse create(BillOfMaterialsRequest request) {
        validateParentProductId(request.parentProductId());
        validateComponentId(request.componentId());
        validateBigDecimal(request.quantity());
        BillOfMaterials bom = billOfMaterialsMapper.toEntity(request);
        BillOfMaterials saved = billOfMaterialsRepository.save(bom);
        return billOfMaterialsMapper.toResponse(saved);

    }

    @Transactional
    @Override
    public BillOfMaterialsResponse update(Long id, BillOfMaterialsRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        BillOfMaterials bom = billOfMaterialsRepository.findById(id)
                .orElseThrow(() -> new BillOfMaterialsErrorException("BillOfMaterials not found with id: " + id));
        validateParentProductId(request.parentProductId());
        validateComponentId(request.componentId());
        validateBigDecimal(request.quantity());
        billOfMaterialsMapper.toUpdateEntity(bom, request);
        return billOfMaterialsMapper.toResponse(billOfMaterialsRepository.save(bom));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!billOfMaterialsRepository.existsById(id)) {
            throw new BillOfMaterialsErrorException("BillOfMaterials not found with id: " + id);
        }
        billOfMaterialsRepository.deleteById(id);
    }

    @Override
    public BillOfMaterialsResponse findOne(Long id) {
        BillOfMaterials bom = billOfMaterialsRepository.findById(id)
                .orElseThrow(() -> new BillOfMaterialsErrorException("BillOfMaterials not found with id: " + id));
        return new BillOfMaterialsResponse(bom);
    }

    @Override
    public List<BillOfMaterialsResponse> findAll() {
        return billOfMaterialsRepository.findAll().stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByParentProductId(Long parentProductId) {
    	validateParentProductId(parentProductId);
        return billOfMaterialsRepository.findByParentProductId(parentProductId).stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByComponentId(Long componentId) {
    	validateComponentId(componentId);
        return billOfMaterialsRepository.findByComponentId(componentId).stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByQuantityGreaterThan(BigDecimal quantity) {
    	validateBigDecimal(quantity);
        return billOfMaterialsRepository.findByQuantityGreaterThan(quantity).stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByQuantity(BigDecimal quantity) {
    	validateBigDecimal(quantity);
        return billOfMaterialsRepository.findByQuantity(quantity).stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillOfMaterialsResponse> findByQuantityLessThan(BigDecimal quantity) {
    	validateBigDecimal(quantity);
        return billOfMaterialsRepository.findByQuantityLessThan(quantity).stream()
                .map(BillOfMaterialsResponse::new)
                .collect(Collectors.toList());
    }

    private void validateRequest(BillOfMaterialsRequest request) {
        if (request.componentId() == null) {
            throw new NoSuchProductException("Component must not be null");
        }
        if (request.parentProductId() == null) {
            throw new NoSuchProductException("ParentProduct must not be null");
        }
        if (request.quantity() == null || request.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number");
        }
    }
    
    private void validateComponentId(Long componentId) {
    	if (componentId == null) {
            throw new NoSuchProductException("Component must not be null");
        }
    }
    
    private void validateParentProductId(Long parentProductId) {
    	if (parentProductId == null) {
            throw new NoSuchProductException("ParentProduct must not be null");
        }
    }
    
    private void validateBigDecimal(BigDecimal quantity) {
    	if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number");
        }
    }

}
