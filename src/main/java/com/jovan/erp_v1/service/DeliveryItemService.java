package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.exception.DeliveryItemErrorException;
import com.jovan.erp_v1.exception.InboundDeliveryErrorException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.mapper.DeliveryItemMapper;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.InboundDelivery;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.repository.DeliveryItemRepository;
import com.jovan.erp_v1.repository.InboundDeliveryRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.request.DeliveryItemRequest;
import com.jovan.erp_v1.response.DeliveryItemResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryItemService implements IDeliveryItemService {

    private final DeliveryItemRepository itemRepository;
    private final OutboundDeliveryRepository outboundDeliveryRepository;
    private final InboundDeliveryRepository inboundDeliveryRepository;
    private final ProductRepository productRepository;
    private final DeliveryItemMapper deliveryItemMapper;

    @Transactional
    @Override
    public DeliveryItemResponse create(DeliveryItemRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NoSuchProductException("Product not found" + request.productId()));
        InboundDelivery inboundDelivery = null;
        if (request.inboundDeliveryId() != null) {
            inboundDelivery = inboundDeliveryRepository.findById(request.inboundDeliveryId())
                    .orElseThrow(() -> new InboundDeliveryErrorException("InboundDelivery not found"));
        }
        OutboundDelivery outboundDelivery = null;
        if (request.outboundDeliveryId() != null) {
            outboundDelivery = outboundDeliveryRepository.findById(request.outboundDeliveryId())
                    .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found"));
        }
        DeliveryItem item = DeliveryItem.builder()
                .product(product)
                .quantity(request.quantity())
                .inboundDelivery(inboundDelivery)
                .outboundDelivery(outboundDelivery)
                .build();
        return deliveryItemMapper.toResponse(itemRepository.save(item));
    }

    @Transactional
    @Override
    public DeliveryItemResponse update(Long id, DeliveryItemRequest request) {
        DeliveryItem item = itemRepository.findById(id)
                .orElseThrow(() -> new DeliveryItemErrorException("DeliveryItem not found" + id));
        item.setQuantity(request.quantity());
        InboundDelivery inboundDelivery = null;
        if (request.inboundDeliveryId() != null) {
            inboundDelivery = inboundDeliveryRepository.findById(request.inboundDeliveryId())
                    .orElseThrow(() -> new InboundDeliveryErrorException("InboundDelivery not found"));
        }
        item.setInboundDelivery(inboundDelivery);
        OutboundDelivery outboundDelivery = null;
        if (request.outboundDeliveryId() != null) {
            outboundDelivery = outboundDeliveryRepository.findById(request.outboundDeliveryId())
                    .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found"));
        }
        item.setOutboundDelivery(outboundDelivery);
        Product product = null;
        if (request.productId() != null) {
            product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new NoSuchProductException("Product not found"));
        }
        item.setProduct(product);
        return deliveryItemMapper.toResponse(itemRepository.save(item));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new DeliveryItemErrorException("Delivery-Item not found " + id);
        }
        itemRepository.deleteById(id);
    }

    @Override
    public DeliveryItemResponse findById(Long id) {
        DeliveryItem item = itemRepository.findById(id)
                .orElseThrow(() -> new DeliveryItemErrorException("Delivery-Item not found" + id));
        return new DeliveryItemResponse(item);
    }

    @Override
    public List<DeliveryItemResponse> findAll() {
        return itemRepository.findAll().stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantity(Double quantity) {
        return itemRepository.findByQuantity(quantity).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantityGreaterThan(Double quantity) {
        return itemRepository.findByQuantity(quantity).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantityLessThan(Double quantity) {
        return itemRepository.findByQuantityLessThan(quantity).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end) {
        return itemRepository.findByInboundDelivery_DeliveryDateBetween(start, end).stream()
                .filter(item -> item.getInboundDelivery() != null &&
                        !item.getInboundDelivery().getDeliveryDate().isBefore(start) &&
                        !item.getInboundDelivery().getDeliveryDate().isAfter(end))
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end) {
        return itemRepository.findByOutboundDelivery_DeliveryDateBetween(start, end).stream()
                .filter(item -> item.getOutboundDelivery() != null &&
                        !item.getOutboundDelivery().getDeliveryDate().isBefore(start) &&
                        !item.getOutboundDelivery().getDeliveryDate().isAfter(end))
                .map(deliveryItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByProduct_Name(String name) {
        return itemRepository.findByProduct_Name(name).stream()
                .map(deliveryItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByInboundDeliveryId(Long inboundId) {
        return itemRepository.findByInboundDeliveryId(inboundId).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByOutboundDeliveryId(Long outboundId) {
        return itemRepository.findByOutboundDeliveryId(outboundId).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryItemResponse findByProduct(Long productId) {
        DeliveryItem item = itemRepository.findByProductId(productId);
        return deliveryItemMapper.toResponse(item);
    }

}
