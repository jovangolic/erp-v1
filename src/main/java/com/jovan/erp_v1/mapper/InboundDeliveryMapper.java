package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.InboundDelivery;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.DeliveryItemInboundRequest;
import com.jovan.erp_v1.request.DeliveryItemRequest;
import com.jovan.erp_v1.request.InboundDeliveryRequest;
import com.jovan.erp_v1.response.InboundDeliveryResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class InboundDeliveryMapper extends AbstractMapper<InboundDeliveryRequest> {
	
    private final DeliveryItemMapper deliveryItemMapper;

    public InboundDelivery toInboundEntity(InboundDeliveryRequest request,Supply supply, Map<Long, Product> productMap) {
    	Objects.requireNonNull(request,"InboundDeliveryRequest must not be null");
    	validateIdForCreate(request, InboundDeliveryRequest::id);
        InboundDelivery delivery = new InboundDelivery();
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setSupply(supply);
        delivery.setStatus(request.status());
        if (request.itemRequest().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Item request list contains null elements");
        } 
        List<DeliveryItem> items = request.itemRequest().stream()
                .map(itemRequest -> {
                    Product product = productMap.get(itemRequest.productId());
                    if (product == null) {
                        throw new ProductNotFoundException("Product not found: " + itemRequest.productId());
                    }
                    DeliveryItem item = deliveryItemMapper.toInEntity(itemRequest, product);
                    item.setInboundDelivery(delivery);
                    item.setOutboundDelivery(null);
                    return item;
                })
                .collect(Collectors.toList());

            delivery.setItems(items);
            return delivery;
    }

    public InboundDeliveryResponse toResponse(InboundDelivery delivery) {
    	Objects.requireNonNull(delivery, "InboundDelivery must not be null");
        return new InboundDeliveryResponse(delivery);
    }
    
    public List<InboundDeliveryResponse> toResponseList(List<InboundDelivery> d){
    	if(d == null || d.isEmpty()) {
    		return Collections.emptyList();
    	}
    	return d.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
