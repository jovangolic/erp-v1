package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jovan.erp_v1.exception.DeliveryItemErrorException;
import com.jovan.erp_v1.exception.InboundDeliveryErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
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
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
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
    	List<DeliveryItem> items = deliveryItemRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List of DeliveryItem is empty");
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantity(BigDecimal quantity) {
    	validateQuantity(quantity);
    	List<DeliveryItem> items = deliveryItemRepository.findByQuantity(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with quantity  %s, is found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantityGreaterThan(BigDecimal quantity) {
    	validateQuantity(quantity);
    	List<DeliveryItem> items = deliveryItemRepository.findByQuantityGreaterThan(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with quantity greater than %s, is found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByQuantityLessThan(BigDecimal quantity) {
    	validateBigDecimalNonNegative(quantity);
    	List<DeliveryItem> items = deliveryItemRepository.findByQuantityLessThan(quantity);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with quantity less than %s, is found", quantity);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<DeliveryItem> items = deliveryItemRepository.findByInboundDelivery_DeliveryDateBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No DeliveryItem with inbound-delivery date between %s and %s, is found",
    				start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end) {
    	DateValidator.validateRange(start, end);
    	List<DeliveryItem> items = deliveryItemRepository.findByOutboundDelivery_DeliveryDateBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No DeliveryItem with outbound-delivery date between %s and %s, is found",
    				start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
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
    	List<DeliveryItem> items = deliveryItemRepository.findByInboundDeliveryId(inboundId);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with inbound-delivery id %d, is found", inboundId);
    		throw new NoDataFoundException(msg);
    	}
        return deliveryItemRepository.findByInboundDeliveryId(inboundId).stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByOutboundDeliveryId(Long outboundId) {
    	validateOutboundDeliveryId(outboundId);
    	List<DeliveryItem> items = deliveryItemRepository.findByOutboundDeliveryId(outboundId);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with outbound-delivery id %d, is found", outboundId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryItemResponse> findByProduct_Name(String name) {
    	validateString(name);
    	List<DeliveryItem> items = deliveryItemRepository.findByProduct_Name(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No DeliveryItem with product name %s is found", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DeliveryItemResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
    
    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
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
