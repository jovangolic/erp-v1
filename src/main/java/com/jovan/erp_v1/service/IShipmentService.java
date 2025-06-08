package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.query.Param;
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

    List<ShipmentResponse> searchByOriginStorageName(@Param("name") String name);

    List<ShipmentResponse> findByTrackingInfo_CurrentLocationContainingIgnoreCase(String location);

    List<ShipmentResponse> findOverdueDelayedShipments();
}
