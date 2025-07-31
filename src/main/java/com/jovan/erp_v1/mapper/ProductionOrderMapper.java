package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.request.ProductionOrderRequest;
import com.jovan.erp_v1.response.ProductionOrderResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class ProductionOrderMapper extends AbstractMapper<ProductionOrderRequest> {

    public ProductionOrder toEntity(ProductionOrderRequest request,Product product, WorkCenter wc) {
        Objects.requireNonNull(request, "ProductionOrder cannot be null");
        Objects.requireNonNull(product, "Product cannot be null");
        Objects.requireNonNull(wc, "WorkCenter cannot be null");
        validateIdForCreate(request, ProductionOrderRequest::id);
        ProductionOrder po = new ProductionOrder();
        po.setId(request.id());
        po.setOrderNumber(request.orderNumber());
        po.setProduct(product);
        po.setQuantityPlanned(request.quantityPlanned());
        po.setQuantityProduced(request.quantityProduced());
        po.setEndDate(request.endDate());
        po.setStatus(request.status());
        po.setWorkCenter(wc);
        return po;
    }

    public void toUpdateEntity(ProductionOrder order, ProductionOrderRequest request, Product product, WorkCenter wc) {
        Objects.requireNonNull(order, "ProductionOrder cannot be null");
        Objects.requireNonNull(request, "ProductionOrderRequest cannot be null");
        buildProductionOrderFromRequest(order, request,product, wc);
    }

    private ProductionOrder buildProductionOrderFromRequest(ProductionOrder p, ProductionOrderRequest request, Product product, WorkCenter wc) {
        p.setOrderNumber(request.orderNumber());
        p.setProduct(product);
        p.setQuantityPlanned(request.quantityPlanned());
        p.setQuantityProduced(request.quantityProduced());
        p.setEndDate(request.endDate());
        p.setStatus(request.status());
        p.setWorkCenter(wc);
        return p;
    }

    public ProductionOrderResponse toResponse(ProductionOrder o) {
        return new ProductionOrderResponse(o);
    }

    public List<ProductionOrderResponse> toResponseList(List<ProductionOrder> o) {
        return o.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
