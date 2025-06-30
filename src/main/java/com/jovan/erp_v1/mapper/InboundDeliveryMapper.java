package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.InboundDelivery;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.InboundDeliveryRequest;
import com.jovan.erp_v1.response.InboundDeliveryResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InboundDeliveryMapper {

    private final SupplyRepository supplyRepository;
    private final DeliveryItemMapper deliveryItemMapper;

    public InboundDelivery toInboundEntity(InboundDeliveryRequest request) {
    	Objects.requireNonNull(request,"InboundDeliveryRequest must not be null");
        InboundDelivery delivery = new InboundDelivery();
        delivery.setDeliveryDate(request.deliveryDate());
        Supply supply = supplyRepository.findById(request.supplyId())
                .orElseThrow(() -> new SupplyNotFoundException("Supply not found " + request.supplyId()));
        delivery.setSupply(supply);
        delivery.setStatus(request.status());
        if (request.itemRequest().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Item request list contains null elements");
        }
        List<DeliveryItem> items = request.itemRequest().stream()
                .map(deliveryItemInboundRequest -> {
                    DeliveryItem item = deliveryItemMapper.toInEntity(deliveryItemInboundRequest);
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
