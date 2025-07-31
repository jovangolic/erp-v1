package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.request.OutboundDeliveryRequest;
import com.jovan.erp_v1.response.OutboundDeliveryResponse;

public interface IOutboundDeliveryService {

    OutboundDeliveryResponse create(OutboundDeliveryRequest request);
    OutboundDeliveryResponse update(Long id, OutboundDeliveryRequest request);
    void delete(Long id);
    OutboundDeliveryResponse findOneById(Long id);
    List<OutboundDeliveryResponse> findAll();
    List<OutboundDeliveryResponse> findByStatus(DeliveryStatus status);
    List<OutboundDeliveryResponse> findByBuyerId(Long buyerId);
    List<OutboundDeliveryResponse> findByDeliveryDateBetween(LocalDate from, LocalDate to);
    // Bulk operacije
    List<OutboundDeliveryResponse> createAll(List<OutboundDeliveryRequest> requests);
    void deleteAllByIds(List<Long> ids);
    
    //nove metode
    List<OutboundDeliveryResponse> findByBuyer_CompanyName(String companyName);
    List<OutboundDeliveryResponse> findByBuyer_Address(String address);
    List<OutboundDeliveryResponse> findByBuyer_ContactPerson(String contactPerson);
    List<OutboundDeliveryResponse> findByBuyer_EmailLikeIgnoreCase(String email);
    List<OutboundDeliveryResponse> findByBuyer_PhoneNumberLikeIgnoreCase(String phoneNumber);
    List<OutboundDeliveryResponse> findByDeliveryDate(LocalDate deliveryDate);
    List<OutboundDeliveryResponse> findByDeliveryDateAfter(LocalDate deliveryDate);
    List<OutboundDeliveryResponse> findByDeliveryDateBefore(LocalDate deliveryDate);
}
