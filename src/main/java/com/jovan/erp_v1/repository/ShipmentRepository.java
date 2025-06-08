package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByStatus(ShipmentStatus status);

    List<Shipment> findByShipmentDateBetween(LocalDate from, LocalDate to);

    List<Shipment> findByProvideId(Long providerId);

    List<Shipment> findByProvider_NameContainingIgnoreCase(String name);

    Shipment findByOutboundDeliveryId(Long outboundId);

    List<Shipment> findByTrackingInfo_CurrentStatus(ShipmentStatus status);

    List<Shipment> findByTrackingInfoId(Long trackingInfoId);

    List<Shipment> findByOriginStorageId(Long storageId);

    List<Shipment> findByOriginStorage_Name(String name);

    List<Shipment> findByOriginStorage_Location(String location);

    List<Shipment> findByOriginStorage_Type(StorageType type);

    List<Shipment> findByOriginStorageIdAndStatus(Long storageId, ShipmentStatus status);

    @Query("SELECT s FROM Shipment s WHERE LOWER(s.originStorage.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Shipment> searchByOriginStorageName(@Param("name") String name);

    List<Shipment> findByTrackingInfo_CurrentLocationContainingIgnoreCase(String location);

    @Query("SELECT s FROM Shipment s WHERE s.trackingInfo.currentStatus = 'DELAYED' AND s.trackingInfo.estimatedDelivery < CURRENT_DATE")
    List<Shipment> findOverdueDelayedShipments();
}
