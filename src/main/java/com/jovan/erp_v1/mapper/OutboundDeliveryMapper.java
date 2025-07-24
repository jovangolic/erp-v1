package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.request.OutboundDeliveryRequest;
import com.jovan.erp_v1.response.OutboundDeliveryResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboundDeliveryMapper {

    private final DeliveryItemMapper deliveryItemMapper;

    public OutboundDelivery toEntity(OutboundDeliveryRequest request, Buyer buyer, Map<Long, Product> productMap) {
        OutboundDelivery delivery = new OutboundDelivery();
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setBuyer(buyer);
        delivery.setStatus(request.status());
        List<DeliveryItem> items = request.itemRequest().stream()
                .map(itemRequest -> {
                    Product product = productMap.get(itemRequest.productId());
                    if (product == null) {
                        throw new ProductNotFoundException("Product not found: " + itemRequest.productId());
                    }
                    DeliveryItem item = deliveryItemMapper.toOutEntity(itemRequest, product);
                    item.setInboundDelivery(null);
                    item.setOutboundDelivery(delivery);
                    return item;
                })
                .collect(Collectors.toList());

            delivery.setItems(items);
            return delivery;
    }

    public OutboundDeliveryResponse toResponse(OutboundDelivery delivery) {
    	Objects.requireNonNull(delivery,"OutboundDelivery must not be null");
        return new OutboundDeliveryResponse(delivery);
    }
    
}
