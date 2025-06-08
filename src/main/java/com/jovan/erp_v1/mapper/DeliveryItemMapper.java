package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.request.DeliveryItemInboundRequest;
import com.jovan.erp_v1.request.DeliveryItemOutboundRequest;
import com.jovan.erp_v1.response.DeliveryItemInboundResponse;
import com.jovan.erp_v1.response.DeliveryItemOutboundResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryItemMapper {

    private final ProductRepository productRepository;

    public DeliveryItem toInEntity(DeliveryItemInboundRequest request) {
        DeliveryItem item = new DeliveryItem();
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NoSuchProductException("Product not found"));
        item.setProduct(product);
        item.setQuantity(request.quantity());
        // Ne setuj inboundDelivery ovde!
        item.setOutboundDelivery(null);
        return item;
    }

    public DeliveryItem toOutEntity(DeliveryItemOutboundRequest request) {
        DeliveryItem item = new DeliveryItem();
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NoSuchProductException("Product not found"));
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setInboundDelivery(null);
        return item;
    }

    public DeliveryItemInboundResponse toInResponse(DeliveryItem item) {
        DeliveryItemInboundResponse response = new DeliveryItemInboundResponse();
        response.setId(item.getId());
        response.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
        response.setQuantity(item.getQuantity());
        response.setInboundDeliveryId(item.getInboundDelivery() != null ? item.getInboundDelivery().getId() : null);
        return response;
    }

    public DeliveryItemOutboundResponse toOutResponse(DeliveryItem item) {
        DeliveryItemOutboundResponse response = new DeliveryItemOutboundResponse();
        response.setId(item.getId());
        response.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
        response.setQuantity(item.getQuantity());
        response.setOutboundDeliveryId(item.getOutboundDelivery() != null ? item.getOutboundDelivery().getId() : null);
        return response;
    }

    public List<DeliveryItemInboundResponse> toInResponseList(List<DeliveryItem> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::toInResponse)
                .collect(Collectors.toList());
    }

    public List<DeliveryItemOutboundResponse> toOutResponseList(List<DeliveryItem> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::toOutResponse)
                .collect(Collectors.toList());
    }
}
