package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.InboundDeliveryErrorException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.InboundDelivery;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.repository.InboundDeliveryRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.request.DeliveryItemRequest;
import com.jovan.erp_v1.response.DeliveryItemResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryItemMapper {

    private final ProductRepository productRepository;
    private final InboundDeliveryRepository inboundDeliveryRepository;
    private final OutboundDeliveryRepository outboundDeliveryRepository;

    public DeliveryItem toEntity(DeliveryItemRequest request) {
        DeliveryItem item = new DeliveryItem();
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NoSuchProductException("Product not found"));
        item.setProduct(product);
        item.setQuantity(request.quantity());
        InboundDelivery inboundDelivery = inboundDeliveryRepository.findById(request.inboundDeliveryId())
                .orElseThrow(() -> new InboundDeliveryErrorException("InboundDelivery not found"));
        item.setInboundDelivery(inboundDelivery);
        OutboundDelivery outboundDelivery = outboundDeliveryRepository.findById(request.outboundDeliveryId())
                .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found"));
        item.setOutboundDelivery(outboundDelivery);
        return item;
    }

    public DeliveryItemResponse toResponse(DeliveryItem item) {
        DeliveryItemResponse response = new DeliveryItemResponse();
        response.setId(item.getId());
        response.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
        response.setQuantity(item.getQuantity());
        response.setInboundDeliveryId(item.getInboundDelivery() != null ? item.getInboundDelivery().getId() : null);
        response.setOutboundDeliveryId(item.getOutboundDelivery() != null ? item.getOutboundDelivery().getId() : null);
        return response;
    }

    public List<DeliveryItemResponse> toResponseList(List<DeliveryItem> items) {
        return items.stream().map(this::toResponse)
                .collect(Collectors.toList());
    }
}
