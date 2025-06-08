package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.mapper.DeliveryItemMapper;
import com.jovan.erp_v1.mapper.OutboundDeliveryMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.request.OutboundDeliveryRequest;
import com.jovan.erp_v1.response.OutboundDeliveryResponse;

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
        OutboundDelivery delivery = outboundDeliveryMapper.toEntity(request);
        OutboundDelivery saved = outboundDeliveryRepository.save(delivery);
        return new OutboundDeliveryResponse(saved);
    }

    @Transactional
    @Override
    public OutboundDeliveryResponse update(Long id, OutboundDeliveryRequest request) {
        OutboundDelivery delivery = outboundDeliveryRepository.findById(id)
                .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found " + id));
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setStatus(request.status());
        Buyer buyer = buyerRepository.findById(request.buyerId())
                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found " + id));
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
        return outboundDeliveryRepository.findByStatus(status).stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> findByBuyerId(Long buyerId) {
        return outboundDeliveryRepository.findByBuyerId(buyerId).stream()
                .map(outboundDeliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDeliveryResponse> findByDeliveryDateBetween(LocalDate from, LocalDate to) {
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

}
