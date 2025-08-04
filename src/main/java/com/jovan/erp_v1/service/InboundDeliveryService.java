package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.jovan.erp_v1.exception.InboundDeliveryErrorException;
import com.jovan.erp_v1.exception.NoSuchProductException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.DeliveryItemMapper;
import com.jovan.erp_v1.mapper.InboundDeliveryMapper;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.InboundDelivery;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.InboundDeliveryRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.request.DeliveryItemInboundRequest;
import com.jovan.erp_v1.request.InboundDeliveryRequest;
import com.jovan.erp_v1.response.InboundDeliveryResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InboundDeliveryService implements InterfejsInboundDeliveryService {

    private final InboundDeliveryRepository inboundDeliveryRepository;
    private final InboundDeliveryMapper inboundDeliveryMapper;
    private final DeliveryItemMapper deliveryItemMapper;
    private final SupplyRepository supplyRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public InboundDeliveryResponse create(InboundDeliveryRequest request) {
    	DateValidator.validateNotNull(request.deliveryDate(), "Datum ne sme biti null");
    	Supply supply = validateSupplyId(request.supplyId());
    	Set<Long> productIds = request.itemRequest().stream()
    	        .map(DeliveryItemInboundRequest::productId)
    	        .collect(Collectors.toSet());
    	List<Product> products = productRepository.findAllById(productIds);
    	Map<Long, Product> productMap = products.stream()
    	        .collect(Collectors.toMap(Product::getId, Function.identity()));
    	validateDeliveryStatus(request.status());
    	validateInboundDeliveryItems(request);
        InboundDelivery delivery = inboundDeliveryMapper.toInboundEntity(request,supply,productMap);
        InboundDelivery saved = inboundDeliveryRepository.save(delivery);
        return new InboundDeliveryResponse(saved);
    }

    @Transactional
    @Override
    public InboundDeliveryResponse update(Long id, InboundDeliveryRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
    	validateInboundDeliveryItems(request);
        InboundDelivery delivery = inboundDeliveryRepository.findById(id)
                .orElseThrow(() -> new InboundDeliveryErrorException("InboundDelivery not found " + id));
        DateValidator.validateNotNull(request.deliveryDate(), "Datum ne sme biti null");
    	validateSupplyId(request.supplyId());
    	validateDeliveryStatus(request.status());
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setStatus(request.status());
        Supply supply = supplyRepository.findById(request.supplyId())
                .orElseThrow(() -> new SupplyNotFoundException("Supply not found " + request.supplyId()));
        delivery.setSupply(supply);
        delivery.getItems().clear();
        List<DeliveryItem> items = mapInboundDeliveryItems(request.itemRequest(), delivery);
        delivery.getItems().addAll(items);
        InboundDelivery saved = inboundDeliveryRepository.save(delivery);
        return new InboundDeliveryResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!inboundDeliveryRepository.existsById(id)) {
            throw new InboundDeliveryErrorException("InboundDelivery not found " + id);
        }
        inboundDeliveryRepository.deleteById(id);
    }

    @Override
    public InboundDeliveryResponse findByOneId(Long id) {
        InboundDelivery delivery = inboundDeliveryRepository.findById(id)
                .orElseThrow(() -> new InboundDeliveryErrorException("InboundDelivery not found " + id));
        return new InboundDeliveryResponse(delivery);
    }

    @Override
    public List<InboundDeliveryResponse> findAll() {
        return inboundDeliveryRepository.findAll().stream()
                .map(inboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDeliveryResponse> findByStatus(DeliveryStatus status) {
    	validateDeliveryStatus(status);
        return inboundDeliveryRepository.findByStatus(status).stream()
                .map(inboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDeliveryResponse> findBySupplyId(Long supplyId) {
    	validateSupplyId(supplyId);
        return inboundDeliveryRepository.findBySupplyId(supplyId).stream()
                .map(inboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDeliveryResponse> findByDeliveryDateBetween(LocalDate from, LocalDate to) {
    	DateValidator.validateRange(from, to);
        return inboundDeliveryRepository.findByDeliveryDateBetween(from, to).stream()
                .map(inboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDeliveryResponse> createAll(List<InboundDeliveryRequest> requests) {
    	if(requests == null || requests.isEmpty()) {
    		return Collections.emptyList();
    	}
    	Set<Long> supplyIds = requests.stream()
    	        .map(InboundDeliveryRequest::supplyId)
    	        .collect(Collectors.toSet());
    	Map<Long, Supply> supplyMap = supplyRepository.findAllById(supplyIds).stream()
    	        .collect(Collectors.toMap(Supply::getId, Function.identity()));
    	Set<Long> productIds = requests.stream()
    	        .flatMap(req -> req.itemRequest().stream())
    	        .map(DeliveryItemInboundRequest::productId)
    	        .collect(Collectors.toSet());
    	Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
    	        .collect(Collectors.toMap(Product::getId, Function.identity()));
    	List<InboundDelivery> deliveries = requests.stream()
    	        .map(req -> {
    	            Supply supply = supplyMap.get(req.supplyId());
    	            if (supply == null) {
    	                throw new SupplyNotFoundException("Supply not found: " + req.supplyId());
    	            }
    	            return inboundDeliveryMapper.toInboundEntity(req, supply, productMap);
    	        })
    	        .collect(Collectors.toList());
    	 List<InboundDelivery> saved = inboundDeliveryRepository.saveAll(deliveries);
    	 return saved.stream()
    	        .map(inboundDeliveryMapper::toResponse)
    	        .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        inboundDeliveryRepository.deleteAllById(ids);
    }
    
    private void validateDeliveryStatus(DeliveryStatus status) {
    	if(status == null) {
    		throw new IllegalArgumentException("Status za DeliveryStatus nije pronadjen");
    	}
    }
    
    private Supply validateSupplyId(Long supplyId) {
    	if(supplyId == null) {
    		throw new IllegalArgumentException("Supply Id ne sme biti null.");
    	}
    	return supplyRepository.findById(supplyId).orElseThrow(() -> new ValidationException("Supply not found with id "+supplyId));
    }
    
    private List<DeliveryItem> mapInboundDeliveryItems(List<DeliveryItemInboundRequest> itemRequests, InboundDelivery delivery) {
    	if (itemRequests == null || itemRequests.isEmpty()) {
    	    return Collections.emptyList();
    	}
    	Set<Long> productIds = itemRequests.stream()
    	        .map(DeliveryItemInboundRequest::productId)
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
                DeliveryItem item = deliveryItemMapper.toInEntity(itemReq,product);
                item.setInboundDelivery(delivery);
                return item;
            })
            .collect(Collectors.toList());
    }
    
    private void validateInboundDeliveryItems(InboundDeliveryRequest requests) {
    	List<DeliveryItemInboundRequest> req = requests.itemRequest();
    	if(req == null || req.isEmpty()) {
    		throw new IllegalArgumentException("Request lista ne sme biti prazna");
    	}
    	for(DeliveryItemInboundRequest item: req) {
    		if(item.productId() == null) {
    			throw new NoSuchProductException("Product ID ne sme biti null");
    		}
    		if(item == null || item.quantity().compareTo(BigDecimal.ZERO) <= 0) {
    			throw new IllegalArgumentException("Kolicina mora biti veca od nule");
    		}
    		if(item.inboundDeliveryId() == null) {
    			throw new InboundDeliveryErrorException("InboundDelivery ID ne sme biti null");
    		}
    	}
    	//provera duplikata
    	Set<Long> productIds = new HashSet<Long>();
    	for(DeliveryItemInboundRequest item: req) {
    		if(!productIds.add(item.productId())) {
    			throw new IllegalArgumentException("Duplikat pronadjen za proizvod ID: " + item.productId());
    		}
    	}
    }
    
}
