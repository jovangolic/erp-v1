package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.request.InboundDeliveryRequest;
import com.jovan.erp_v1.response.InboundDeliveryResponse;

public interface InterfejsInboundDeliveryService {

    InboundDeliveryResponse create(InboundDeliveryRequest request);

    InboundDeliveryResponse update(Long id, InboundDeliveryRequest request);

    void delete(Long id);

    InboundDeliveryResponse findByOneId(Long id);

    List<InboundDeliveryResponse> findAll();

    List<InboundDeliveryResponse> findByStatus(DeliveryStatus status);

    List<InboundDeliveryResponse> findBySupplyId(Long supplyId);

    List<InboundDeliveryResponse> findByDeliveryDateBetween(LocalDate from, LocalDate to);

    // Bulk metode
    List<InboundDeliveryResponse> createAll(List<InboundDeliveryRequest> requests);

    void deleteAllByIds(List<Long> ids);
}
