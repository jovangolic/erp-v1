package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.request.DeliveryItemRequest;
import com.jovan.erp_v1.response.DeliveryItemResponse;

public interface IDeliveryItemService {

    DeliveryItemResponse create(DeliveryItemRequest request);

    DeliveryItemResponse update(Long id, DeliveryItemRequest request);

    void delete(Long id);

    DeliveryItemResponse findById(Long id);

    List<DeliveryItemResponse> findAll();

    List<DeliveryItemResponse> findByQuantity(BigDecimal quantity);

    List<DeliveryItemResponse> findByQuantityGreaterThan(BigDecimal quantity);

    List<DeliveryItemResponse> findByQuantityLessThan(BigDecimal quantity);

    List<DeliveryItemResponse> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);

    List<DeliveryItemResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);

    DeliveryItemResponse findByProduct(Long productId);

    List<DeliveryItemResponse> findByInboundDeliveryId(Long inboundId);

    List<DeliveryItemResponse> findByOutboundDeliveryId(Long outboundId);

    List<DeliveryItemResponse> findByProduct_Name(String name);
}
