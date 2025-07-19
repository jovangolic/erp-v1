package com.jovan.erp_v1.service;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.TrackingInfoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface ITrackingInfoService {

    TrackingInfoResponse create(TrackingInfoRequest request);
    TrackingInfoResponse update(Long id, TrackingInfoRequest request);
    void delete(Long id);
    TrackingInfoResponse findOneById(Long id);
    List<TrackingInfoResponse> findAll();
    TrackingInfoResponse findByTrackingNumber(String trackingNumber);
    TrackingInfoResponse findByShipmentId(Long shipmentId);
    List<TrackingInfoResponse> findByEstimatedDeliveryBetween(LocalDate start, LocalDate end);
    List<TrackingInfoResponse> findByCurrentLocationAndCurrentStatus(String location, ShipmentStatus status);
    List<TrackingInfoResponse> findByEstimatedDelivery(LocalDate date);
    List<TrackingInfoResponse> findAllByOrderByEstimatedDeliveryAsc();
    List<TrackingInfoResponse> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<TrackingInfoResponse> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<TrackingInfoResponse> findByUpdatedAtAfter(LocalDateTime since);
    
    //nove metode
  
    List<TrackingInfoResponse> findByPending();
    List<TrackingInfoResponse> findByShipped();
    List<TrackingInfoResponse> findByIn_Transit();
    List<TrackingInfoResponse> findByDelivered();
    List<TrackingInfoResponse> findByDelayed();
    List<TrackingInfoResponse> findByCancelled();
    List<TrackingInfoResponse> findByShipment_ShipmentDate(LocalDate shipmentDate);
    List<TrackingInfoResponse> findByShipment_ShipmentDateBetween(LocalDate shipmentDateStart, LocalDate shipmentDateEnd);
    List<TrackingInfoResponse> findByShipment_Provider_Id(Long providerId);
    List<TrackingInfoResponse> findByShipment_OutboundDelivery_Id( Long outboundDeliveryId);
    List<TrackingInfoResponse> findByShipment_OriginStorage_Id(Long originStorageId);
    List<TrackingInfoResponse> findByTrackingNumberAndCurrentLocation(String trackingNumber, String currentLocation);
    Boolean existsByTrackingNumber(String trackingNumber);
    List<TrackingInfoResponse> findByShipment_Provider_NameContainingIgnoreCase( String providerName);
    List<TrackingInfoResponse> findByShipment_Provider_ContactPhoneLikeIgnoreCase(String contactPhone);
    List<TrackingInfoResponse> findByShipment_Provider_EmailLikeIgnoreCase( String providerEmail);
    List<TrackingInfoResponse> findByShipment_Provider_Website( String providerWebsite);
    List<TrackingInfoResponse> findByShipment_OriginStorage_NameContainingIgnoreCase( String storageName);
    List<TrackingInfoResponse> findByShipment_OriginStorage_LocationContainingIgnoreCase(String storageLocation);
    List<TrackingInfoResponse> findByShipment_OriginStorage_Capacity( BigDecimal storageCapacity);
    List<TrackingInfoResponse> findByShipment_OriginStorage_CapacityGreaterThan(BigDecimal storageCapacity);
    List<TrackingInfoResponse> findByShipment_OriginStorage_CapacityLessThan( BigDecimal storageCapacity);
    List<TrackingInfoResponse> findByShipment_OriginStorage_Type( StorageType type);
    List<TrackingInfoResponse> findByShipment_OriginStorage_Status( StorageStatus status);
    List<TrackingInfoResponse> findByStorageTypeAndStatus(StorageType type, StorageStatus status);
    List<TrackingInfoResponse> findByTypeAndStatus(StorageType type, StorageStatus status);

}
