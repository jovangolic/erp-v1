package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.DeliveryItemMapper;
import com.jovan.erp_v1.mapper.OutboundDeliveryMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.request.DeliveryItemOutboundRequest;
import com.jovan.erp_v1.request.OutboundDeliveryRequest;
import com.jovan.erp_v1.response.OutboundDeliveryResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboundDeliveryService implements IOutboundDeliveryService {

    private OutboundDeliveryRepository outboundDeliveryRepository;
    private OutboundDeliveryMapper outboundDeliveryMapper;
    private final BuyerRepository buyerRepository;
    private final DeliveryItemMapper deliveryItemMapper;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public OutboundDeliveryResponse create(OutboundDeliveryRequest request) {
    	DateValidator.validateNotNull(request.deliveryDate(), "Delivery Date");
        if(request.buyerId() == null) {
        	throw new BuyerNotFoundException("Buyer ID must mot be null "+request.buyerId()); //ovde sam rucno proveravao, nisam mogao da koristim fetchBuyer metodu jer ona vraca
        }
        validateDeliveryStatus(request.status());
        validateDeliveryItems(request.itemRequest());
        Buyer buyer = fetchBuyer(request.buyerId());
        Set<Long> productIds = request.itemRequest().stream()
    	        .map(DeliveryItemOutboundRequest::productId)
    	        .collect(Collectors.toSet());
        List<Product> products = productRepository.findAllById(productIds);
    	Map<Long, Product> productMap = products.stream()
    	        .collect(Collectors.toMap(Product::getId, Function.identity()));
        OutboundDelivery delivery = outboundDeliveryMapper.toEntity(request,buyer,productMap);
        OutboundDelivery saved = outboundDeliveryRepository.save(delivery);
        return new OutboundDeliveryResponse(saved);
    }

    @Transactional
    @Override
    public OutboundDeliveryResponse update(Long id, OutboundDeliveryRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        OutboundDelivery delivery = outboundDeliveryRepository.findById(id)
                .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found " + id));
        DateValidator.validateNotNull(request.deliveryDate(), "Delivery Date");
        Buyer buyer = fetchBuyer(request.buyerId());
        validateDeliveryStatus(request.status());
        validateDeliveryItems(request.itemRequest());
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setStatus(request.status());
        delivery.setBuyer(buyer);
        delivery.getItems().clear();
        List<DeliveryItem> items = mapOutboundDeliveryItems(request.itemRequest(), delivery);
        delivery.getItems().addAll(items);
        OutboundDelivery saved = outboundDeliveryRepository.save(delivery);
        return new OutboundDeliveryResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!outboundDeliveryRepository.existsById(id)) {
            throw new OutboundDeliveryErrorException("OutboundDelivery not found " + id);
        }
        outboundDeliveryRepository.deleteById(id);
    }

    @Override
    public OutboundDeliveryResponse findOneById(Long id) {
        OutboundDelivery delivery = outboundDeliveryRepository.findById(id)
                .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found " + id));
        return new OutboundDeliveryResponse(delivery);
    }

    @Override
    public List<OutboundDeliveryResponse> findAll() {
    	List<OutboundDelivery> items = outboundDeliveryRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("OutboundDelivery list is empty");
    	}
        return items.stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> findByStatus(DeliveryStatus status) {
    	validateDeliveryStatus(status);
    	List<OutboundDelivery> items = outboundDeliveryRepository.findByStatus(status);
    	if(items.isEmpty()) {
    		String msg = String.format("No OutboundDelivery for status %s is found", status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> findByBuyerId(Long buyerId) {
    	fetchBuyer(buyerId);
    	List<OutboundDelivery> items = outboundDeliveryRepository.findByBuyerId(buyerId);
    	if(items.isEmpty()) {
    		String msg = String.format("No OutboundDelivery for buyer-id %d is found", buyerId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> findByDeliveryDateBetween(LocalDate from, LocalDate to) {
    	DateValidator.validateRange(from, to);
    	List<OutboundDelivery> items = outboundDeliveryRepository.findByDeliveryDateBetween(from, to);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    		String msg = String.format("No OutboundDelivery for delivery date between %s and %s is found", 
    				from.format(formatter),to.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> createAll(List<OutboundDeliveryRequest> requests) {
    	if (requests == null || requests.isEmpty()) {
            return Collections.emptyList();
        }
    	Set<Long> buyerIds = requests.stream()
    			.map(OutboundDeliveryRequest::buyerId)
    			.collect(Collectors.toSet());
    	Map<Long, Buyer> buyerMap = buyerRepository.findAllById(buyerIds).stream()
    			.collect(Collectors.toMap(Buyer::getId, Function.identity()));
    	Set<Long> productIds = requests.stream()
    	        .flatMap(req -> req.itemRequest().stream())
    	        .map(DeliveryItemOutboundRequest::productId)
    	        .collect(Collectors.toSet());

    	Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
    	        .collect(Collectors.toMap(Product::getId, Function.identity()));
        List<OutboundDelivery> deliveries = requests.stream()
                .map(req -> {
                	Buyer buyer = buyerMap.get(req.buyerId());
                	if(buyer == null) {
                		throw new ValidationException("Buyer not found "+req.buyerId());
                	}
                	return outboundDeliveryMapper.toEntity(req, buyer, productMap);
                })
                .collect(Collectors.toList());
        List<OutboundDelivery> saved = outboundDeliveryRepository.saveAll(deliveries);

        return saved.stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        outboundDeliveryRepository.deleteAllById(ids);
    }
    
    private List<DeliveryItem> mapOutboundDeliveryItems(List<DeliveryItemOutboundRequest> itemRequests, OutboundDelivery delivery) {
    	if (itemRequests == null || itemRequests.isEmpty()) {
    	    return Collections.emptyList();
    	}
    	Set<Long> productIds = itemRequests.stream()
    	        .map(DeliveryItemOutboundRequest::productId)
    	        .collect(Collectors.toSet());
    	Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
    	        .collect(Collectors.toMap(Product::getId, Function.identity()));
    	if (productMap.size() != productIds.size()) {
    	    Set<Long> notFound = new HashSet<>(productIds);
    	    notFound.removeAll(productMap.keySet());
    	    throw new ProductNotFoundException("Products not found: " + notFound);
    	}
        return itemRequests.stream()
            .map(itemReq -> {
            	Product product = productMap.get(itemReq.productId());
            	if (product == null) {
                    throw new ProductNotFoundException("Product not found: " + itemReq.productId());
                }
                DeliveryItem item = deliveryItemMapper.toOutEntity(itemReq,product);
                item.setOutboundDelivery(delivery);
                return item;
            })
            .collect(Collectors.toList());
    }
    
    private Buyer fetchBuyer(Long buyerId) {
    	if(buyerId == null) {
    		throw new BuyerNotFoundException("Buyer ID must not be null");
    	}
    	return buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with ID: "+buyerId));
    }
    
    private void validateDeliveryStatus(DeliveryStatus status) {
    	if(status == null) {
    		throw new IllegalArgumentException("DeliveryStatus status must not be null");
    	}
    }
    
    private void validateDeliveryItems(List<DeliveryItemOutboundRequest> items) {
    	if(items == null || items.isEmpty()) {
    		throw new IllegalArgumentException("Items must not be null nor empty");
    	}
    	for(DeliveryItemOutboundRequest dv: items) {
    		if(dv.productId() == null) {
    			throw new ProductNotFoundException("Product ID must not be null");
    		}
    		if(dv.quantity() == null || dv.quantity().compareTo(BigDecimal.ZERO) <= 0) {
    			throw new IllegalArgumentException("Quantity must be positive");
    		}
    	}
    }

}
