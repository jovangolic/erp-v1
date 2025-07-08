package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.ProductNotFoundException;
import com.jovan.erp_v1.mapper.DeliveryItemMapper;
import com.jovan.erp_v1.mapper.OutboundDeliveryMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
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
    private BuyerRepository buyerRepository;
    private final DeliveryItemMapper deliveryItemMapper;

    @Transactional
    @Override
    public OutboundDeliveryResponse create(OutboundDeliveryRequest request) {
    	DateValidator.validateNotNull(request.deliveryDate(), "Delivery Date");
        if(request.buyerId() == null) {
        	throw new BuyerNotFoundException("Buyer ID must mot be null "+request.buyerId()); //ovde sam rucno proveravao, nisam mogao da koristim fetchBuyer metodu jer ona vraca
        }
        validateDeliveryStatus(request.status());
        validateDeliveryItems(request.itemRequest());
        OutboundDelivery delivery = outboundDeliveryMapper.toEntity(request);
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
        List<DeliveryItem> items = request.itemRequest().stream()
                .map(itemReq -> {
                    DeliveryItem item = deliveryItemMapper.toOutEntity(itemReq);
                    item.setOutboundDelivery(delivery);
                    return item;
                })
                .collect(Collectors.toList());
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
        return outboundDeliveryRepository.findAll().stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> findByStatus(DeliveryStatus status) {
    	validateDeliveryStatus(status);
        return outboundDeliveryRepository.findByStatus(status).stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> findByBuyerId(Long buyerId) {
    	Buyer buyer = fetchBuyer(buyerId);
        return outboundDeliveryRepository.findByBuyerId(buyer.getId()).stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> findByDeliveryDateBetween(LocalDate from, LocalDate to) {
    	DateValidator.validateRange(from, to);
        return outboundDeliveryRepository.findByDeliveryDateBetween(from, to).stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> createAll(List<OutboundDeliveryRequest> requests) {
        List<OutboundDelivery> deliveries = requests.stream()
                .map(outboundDeliveryMapper::toEntity)
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
