package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.ShipmentRequest;
import com.jovan.erp_v1.response.ShipmentResponse;

public interface IShipmentService {

    ShipmentResponse create(ShipmentRequest request);
    ShipmentResponse update(Long id, ShipmentRequest request);
    void delete(Long id);
    ShipmentResponse findByOneId(Long id);
    List<ShipmentResponse> findAll();
    List<ShipmentResponse> findByStatus(ShipmentStatus status);
    List<ShipmentResponse> findByShipmentDateBetween(LocalDate from, LocalDate to);
    List<ShipmentResponse> findByProviderId(Long providerId);
    List<ShipmentResponse> findByProvider_NameContainingIgnoreCase(String name);
    ShipmentResponse findByOutboundDeliveryId(Long outboundId);
    List<ShipmentResponse> findByTrackingInfo_CurrentStatus(ShipmentStatus status);
    List<ShipmentResponse> findByTrackingInfoId(Long trackingInfoId);
    List<ShipmentResponse> findByOriginStorageId(Long storageId);
    List<ShipmentResponse> findByOriginStorage_Name(String name);
    List<ShipmentResponse> findByOriginStorage_Location(String location);
    List<ShipmentResponse> findByOriginStorage_Type(StorageType type);
    List<ShipmentResponse> findByOriginStorageIdAndStatus(Long storageId, ShipmentStatus status);
    List<ShipmentResponse> searchByOriginStorageName( String name);
    List<ShipmentResponse> findByTrackingInfo_CurrentLocationContainingIgnoreCase(String location);
    List<ShipmentResponse> findOverdueDelayedShipments();
    //nove metode
    List<ShipmentResponse> findByTrackingInfo_TrackingNumber(String trackingNumber);
    List<ShipmentResponse> findByTrackingInfo_EstimatedDelivery(LocalDate estimatedDelivery);
    List<ShipmentResponse> findByTrackingInfo_EstimatedDeliveryBetween(LocalDate estimatedDeliveryStart,LocalDate estimatedDeliveryEnd);
    List<ShipmentResponse> findByProvider_EmailLikeIgnoreCase( String email);
    List<ShipmentResponse> findByProvider_WebsiteContainingIgnoreCase(String website);
    List<ShipmentResponse> findByProvider_PhoneNumberLikeIgnoreCase( String phoneNumber);
    List<ShipmentResponse> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate);
    List<ShipmentResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate deliveryDateStart, LocalDate deliveryDateEnd);
    List<ShipmentResponse> findByOutboundDelivery_Status(DeliveryStatus status);
    List<ShipmentResponse> findByOutboundDelivery_Buyer_Id( Long buyerId);
    //isporuke koje kasne ali nisu odlozene Delayed
    List<ShipmentResponse> findLateShipments();
    //ubuduce isporuke
    List<ShipmentResponse> findShipmentsDueSoon( LocalDate futureDate);
    List<ShipmentResponse> findByTrackingInfoIsNull();
    List<ShipmentResponse> findByOutboundDelivery_StatusAndOutboundDelivery_DeliveryDateBetween(DeliveryStatus status, LocalDate from, LocalDate to);
    List<ShipmentResponse> findByOriginStorageIdAndTrackingInfo_EstimatedDeliveryBetween(Long storageId, LocalDate from, LocalDate to);
    List<ShipmentResponse> findByTrackingInfo_CurrentStatusAndTrackingInfo_CurrentLocationContainingIgnoreCase(ShipmentStatus status, String location);
    List<ShipmentResponse> findByBuyerNameContainingIgnoreCase(String buyerName);
    List<ShipmentResponse> findRecentlyDeliveredShipments(LocalDateTime fromDate);
    List<ShipmentResponse> findCancelledShipments();
    List<ShipmentResponse> findCancelledShipmentsBetweenDates( LocalDate from,  LocalDate to);
    List<ShipmentResponse> findDelayedShipments();
    List<ShipmentResponse> findInTransitShipments();
    List<ShipmentResponse> findDeliveredShipments();
    List<ShipmentResponse> findShipmentsByFixedStatus(ShipmentStatus status);
    List<ShipmentResponse> findPendingDeliveries();
    List<ShipmentResponse> findInTransitDeliveries();
    List<ShipmentResponse> findDeliveredDeliveries();
    List<ShipmentResponse> findCancelledDeliveries();
    List<ShipmentResponse> findInTransitShipmentsWithInTransitDelivery();
}
