package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.request.DeliveryItemInboundRequest;
import com.jovan.erp_v1.request.DeliveryItemOutboundRequest;
import com.jovan.erp_v1.response.DeliveryItemInboundResponse;
import com.jovan.erp_v1.response.DeliveryItemOutboundResponse;

@Component
public class DeliveryItemMapper  {

    public DeliveryItem toInEntity(DeliveryItemInboundRequest request, Product product) {
    	Objects.requireNonNull(request, "DeliveryItemInboundRequest must not be null");
    	Objects.requireNonNull(product, "Product must not be null");
    	if(request.id() != null) {
    		throw new ValidationException("DeliveryItem ID must be null, while being created");
    	}
        DeliveryItem item = new DeliveryItem();
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setOutboundDelivery(null);
        return item;
    }

    public DeliveryItem toOutEntity(DeliveryItemOutboundRequest request, Product product) {
    	Objects.requireNonNull(request, "DeliveryItemOutboundRequest must not be null");
    	Objects.requireNonNull(product, "Product must not be null");
    	if(request.id() != null) {
    		throw new ValidationException("DeliveryItem ID must be null, while being created");
    	}
        DeliveryItem item = new DeliveryItem();
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setInboundDelivery(null);
        return item;
    }

    public DeliveryItemInboundResponse toInResponse(DeliveryItem item) {
    	Objects.requireNonNull(item, "DeliveryItem must not be null");
        DeliveryItemInboundResponse response = new DeliveryItemInboundResponse();
        response.setId(item.getId());
        response.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
        response.setQuantity(item.getQuantity());
        response.setInboundDeliveryId(item.getInboundDelivery() != null ? item.getInboundDelivery().getId() : null);
        return response;
    }

    public DeliveryItemOutboundResponse toOutResponse(DeliveryItem item) {
    	Objects.requireNonNull(item, "DeliveryItem must not be null");
        DeliveryItemOutboundResponse response = new DeliveryItemOutboundResponse();
        response.setId(item.getId());
        response.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
        response.setQuantity(item.getQuantity());
        response.setOutboundDeliveryId(item.getOutboundDelivery() != null ? item.getOutboundDelivery().getId() : null);
        return response;
    }

    public List<DeliveryItemInboundResponse> toInResponseList(List<DeliveryItem> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::toInResponse)
                .collect(Collectors.toList());
    }

    public List<DeliveryItemOutboundResponse> toOutResponseList(List<DeliveryItem> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::toOutResponse)
                .collect(Collectors.toList());
    }
}
