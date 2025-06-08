package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.request.OutboundDeliveryRequest;
import com.jovan.erp_v1.response.OutboundDeliveryResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboundDeliveryMapper {

    private final DeliveryItemMapper deliveryItemMapper;
    private final BuyerRepository buyerRepository;

    public OutboundDelivery toEntity(OutboundDeliveryRequest request) {
        OutboundDelivery delivery = new OutboundDelivery();
        delivery.setDeliveryDate(request.deliveryDate());
        Buyer buyer = buyerRepository.findById(request.buyerId())
                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found: " + request.buyerId()));
        delivery.setBuyer(buyer);
        delivery.setStatus(request.status());
        List<DeliveryItem> items = request.itemRequest().stream()
                .map(deliveryItemRequest -> {
                    DeliveryItem item = deliveryItemMapper.toOutEntity(deliveryItemRequest);
                    item.setOutboundDelivery(delivery);
                    item.setInboundDelivery(null);
                    return item;
                })
                .collect(Collectors.toList());
        delivery.setItems(items);
        return delivery;
    }

    public OutboundDeliveryResponse toResponse(OutboundDelivery delivery) {
        return new OutboundDeliveryResponse(delivery);
    }

}
