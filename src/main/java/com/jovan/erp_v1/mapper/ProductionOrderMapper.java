package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.ProductionOrderRequest;
import com.jovan.erp_v1.response.ProductionOrderResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductionOrderMapper {

    private final ProductRepository productRepository;
    private final WorkCenterRepository workCenterRepository;

    public ProductionOrder toEntity(ProductionOrderRequest request) {
        Objects.requireNonNull(request, "ProductionOrder cannot be null");
        return buildProductionOrderFromRequest(new ProductionOrder(), request);
    }

    public void toUpdateEntity(ProductionOrder order, ProductionOrderRequest request) {
        Objects.requireNonNull(order, "ProductionOrder cannot be null");
        Objects.requireNonNull(request, "ProductionOrderRequest cannot be null");
        buildProductionOrderFromRequest(order, request);
    }

    private ProductionOrder buildProductionOrderFromRequest(ProductionOrder p, ProductionOrderRequest request) {
        p.setOrderNumber(request.orderNumber());
        p.setProduct(fetchProduct(request.productId()));
        p.setQuantityPlanned(request.quantityPlanned());
        p.setQuantityProduced(request.quantityProduced());
        p.setStartDate(request.startDate());
        p.setEndDate(request.endDate());
        p.setStatus(request.status());
        p.setWorkCenter(fetchWorkCenter(request.workCenterId()));
        return p;
    }

    public ProductionOrderResponse toResponse(ProductionOrder o) {
        return new ProductionOrderResponse(o);
    }

    public List<ProductionOrderResponse> toResponseList(List<ProductionOrder> o) {
        return o.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Product fetchProduct(Long productId) {
        if (productId == null) {
            throw new NoSuchProductException("Product ID can not be null: ");
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchProductException("Product is not found with id: " + productId));
    }

    private WorkCenter fetchWorkCenter(Long workCenterId) {
        if (workCenterId == null) {
            throw new WorkCenterErrorException("WorkCenter can't be null");
        }
        return workCenterRepository.findById(workCenterId)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id " + workCenterId));
    }
}
