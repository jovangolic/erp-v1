package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.exception.DeliveryItemErrorException;
import com.jovan.erp_v1.exception.InboundDeliveryErrorException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
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
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryItemService implements IDeliveryItemService {

    private final DeliveryItemRepository deliveryItemRepository;
    private final ProductRepository productRepository;
    private final InboundDeliveryRepository inboundDeliveryRepository;
    private final OutboundDeliveryRepository outboundDeliveryRepository;

    @Transactional
    @Override
    public DeliveryItemResponse create(DeliveryItemRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found " + request.productId()));
        InboundDelivery inbound = inboundDeliveryRepository.findById(request.inboundDeliveryId())
                .orElseThrow(() -> new InboundDeliveryErrorException(
                        "InboundDelivery not found" + request.inboundDeliveryId()));
        OutboundDelivery outbound = outboundDeliveryRepository.findById(request.outboundDeliveryId())
                .orElseThrow(() -> new OutboundDeliveryErrorException(
                        "OutboundDelivery not found " + request.outboundDeliveryId()));
        validateQuantity(request.quantity());
        DeliveryItem item = DeliveryItem.builder()
                .product(product)
                .inboundDelivery(inbound)
                .outboundDelivery(outbound)
                .quantity(request.quantity())
                .build();
        DeliveryItem saved = deliveryItemRepository.save(item);
        return new DeliveryItemResponse(saved);
    }

    @Transactional
    @Override
    public DeliveryItemResponse update(Long id, DeliveryItemRequest request) {
        DeliveryItem item = deliveryItemRepository.findById(id)
                .orElseThrow(() -> new DeliveryItemErrorException("DeliveryItem not found " + id));
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found " + request.productId()));
        InboundDelivery inbound = inboundDeliveryRepository.findById(request.inboundDeliveryId())
                .orElseThrow(() -> new InboundDeliveryErrorException(
                        "InboundDelivery not found" + request.inboundDeliveryId()));
        OutboundDelivery outbound = outboundDeliveryRepository.findById(request.outboundDeliveryId())
                .orElseThrow(() -> new OutboundDeliveryErrorException(
                        "OutboundDelivery not found " + request.outboundDeliveryId()));
        validateQuantity(request.quantity());
        item.setQuantity(request.quantity());
        item.setInboundDelivery(inbound);
        item.setOutboundDelivery(outbound);
        item.setProduct(product);
        DeliveryItem updated = deliveryItemRepository.save(item);
        return new DeliveryItemResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!deliveryItemRepository.existsById(id)) {
            throw new DeliveryItemErrorException("DeliveryItem not found " + id);
        }
        deliveryItemRepository.deleteById(id);
    }

    @Override
    public DeliveryItemResponse findById(Long id) {
        DeliveryItem item = deliveryItemRepository.findById(id)
                .orElseThrow(() -> new DeliveryItemErrorException("DeliveryItem not found " + id));
        return new DeliveryItemResponse(item);
    }

    @Override
    public List<DeliveryItemResponse> findAll() {
        return deliveryItemRepository.findAll().stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantity(BigDecimal quantity) {
    	validateQuantity(quantity);
        return deliveryItemRepository.findByQuantity(quantity).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantityGreaterThan(BigDecimal quantity) {
    	validateQuantity(quantity);
        return deliveryItemRepository.findByQuantityGreaterThan(quantity).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantityLessThan(BigDecimal quantity) {
    	validateQuantity(quantity);
        return deliveryItemRepository.findByQuantityLessThan(quantity).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
        return deliveryItemRepository.findByOutboundDelivery_DeliveryDateBetween(start, end).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
        return deliveryItemRepository.findByOutboundDelivery_DeliveryDateBetween(start, end).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryItemResponse findByProduct(Long productId) {
    	validateProductId(productId);
        DeliveryItem item = deliveryItemRepository.findByProductId(productId);
        if (item == null) {
            throw new ProductNotFoundException("Product not found " + productId);
        }
        return new DeliveryItemResponse(item);
    }

    @Override
    public List<DeliveryItemResponse> findByInboundDeliveryId(Long inboundId) {
    	validateInboundDeliveryId(inboundId);
        return deliveryItemRepository.findByInboundDeliveryId(inboundId).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByOutboundDeliveryId(Long outboundId) {
    	validateOutboundDeliveryId(outboundId);
        return deliveryItemRepository.findByOutboundDeliveryId(outboundId).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByProduct_Name(String name) {
        return deliveryItemRepository.findByProduct_Name(name).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateQuantity(BigDecimal quantity) {
    	if(quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new IllegalArgumentException("Quantity mora biti pozitivan broj");
    	}
    }
    
    private void validateProductId(Long productId) {
		if(productId == null) {
	    		throw new NoSuchProductException("Product ID "+productId+" ne postoji");
	    	}
	}
    
    private void validateOutboundDeliveryId(Long outboundDeliveryId) {
		if(outboundDeliveryId == null) {
	    		throw new OutboundDeliveryErrorException("OutboundDelivery ID "+outboundDeliveryId+" ne postoji");
	    	}
	}
    
    private void validateInboundDeliveryId(Long inboundDeliveryId) {
		if(inboundDeliveryId == null) {
	    		throw new InboundDeliveryErrorException("InboundDelivery ID "+inboundDeliveryId+" ne postoji");
	    	}
	}
}
