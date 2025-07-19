package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.TrackingInfo;

@Repository
public interface TrackingInfoRepository extends JpaRepository<TrackingInfo, Long>, JpaSpecificationExecutor<TrackingInfo> {

    List<TrackingInfo> findByCurrentStatus(ShipmentStatus scurrentStatus);
    List<TrackingInfo> findByCurrentLocation(String currentLocation);
    TrackingInfo findByTrackingNumber(String trackingNumber);
    List<TrackingInfo> findByEstimatedDeliveryBetween(LocalDate start, LocalDate end);
    List<TrackingInfo> findByCurrentLocationAndCurrentStatus(String location, ShipmentStatus status);
    Optional<TrackingInfo> findByShipmentId(Long shipmentId);
    List<TrackingInfo> findByEstimatedDelivery(LocalDate date);
    List<TrackingInfo> findAllByOrderByEstimatedDeliveryAsc();
    List<TrackingInfo> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<TrackingInfo> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<TrackingInfo> findByUpdatedAtAfter(LocalDateTime since);
    
    //nove metode
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.currentStatus 'PENDING'")
    List<TrackingInfo> findByPending();
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.currentStatus 'SHIPPED'")
    List<TrackingInfo> findByShipped();
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.currentStatus 'IN_TRANSIT'")
    List<TrackingInfo> findByIn_Transit();
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.currentStatus 'DELIVERED'")
    List<TrackingInfo> findByDelivered();
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.currentStatus 'DELAYED'")
    List<TrackingInfo> findByDelayed();
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.currentStatus 'CANCELLED'")
    List<TrackingInfo> findByCancelled();
    List<TrackingInfo> findByShipment_ShipmentDate(LocalDate shipmentDate);
    List<TrackingInfo> findByShipment_ShipmentDateBetween(LocalDate shipmentDateStart, LocalDate shipmentDateEnd);
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.shipment.provider.id = :providerId")
    List<TrackingInfo> findByShipment_Provider_Id(@Param("providerId") Long providerId);
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.shipment.outboundDelivery.id = :outboundDeliveryId")
    List<TrackingInfo> findByShipment_OutboundDelivery_Id(@Param("outboundDeliveryId") Long outboundDeliveryId);
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.shipment.originStorage.id = :originStorageId")
    List<TrackingInfo> findByShipment_OriginStorage_Id(@Param("originStorageId") Long originStorageId);
    List<TrackingInfo> findByTrackingNumberAndCurrentLocation(String trackingNumber, String currentLocation);
    Boolean existsByTrackingNumber(String trackingInfo);
    @Query("SELECT ti FROM TrackingInfo ti WHERE LOWER(ti.shipment.provider.name) LIKE LOWER (CONCAT('%', :providerName, '%'))")
    List<TrackingInfo> findByShipment_Provider_NameContainingIgnoreCase(@Param("providerName") String providerName);
    @Query("SELECT ti FROM TrackingInfo ti WHERE LOWER(ti.shipment.provider.contactPhone) LIKE LOWER(CONCAT('%', :contactPhone, '%'))")
    List<TrackingInfo> findByShipment_Provider_ContactPhoneLikeIgnoreCase(@Param("contactPhone") String contactPhone);
    @Query("SELECT ti FROM TrackingInfo ti WHERE LOWER(ti.shipment.provider.email) LIKE LOWER(CONCAT('%', :providerEmail, '%'))")
    List<TrackingInfo> findByShipment_Provider_EmailLikeIgnoreCase(@Param("providerEmail") String providerEmail);
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.shipment.provider.website = :providerWebsite")
    List<TrackingInfo> findByShipment_Provider_Website(@Param("providerWebsite") String providerWebsite);
    @Query("SELECT ti FROM TrackingInfo ti WHERE LOWER(ti.shipment.originStorage.name) LIKE LOWER (CONCAT('%', :storageName, '%'))")
    List<TrackingInfo> findByShipment_OriginStorage_NameContainingIgnoreCase(@Param("storageName") String storageName);
    @Query("SELECT ti FROM TrackingInfo ti WHERE LOWER(ti.shipment.originStorage.location) LIKE LOWER (CONCAT('%', :storageLocation, '%'))")
    List<TrackingInfo> findByShipment_OriginStorage_LocationContainingIgnoreCase(@Param("storageLocation") String storageLocation);
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.shipment.originStorage.capacity = :storageCapacity")
    List<TrackingInfo> findByShipment_OriginStorage_Capacity(@Param("storageCapacity") BigDecimal storageCapacity);
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.shipment.originStorage.capacity >= :storageCapacity")
    List<TrackingInfo> findByShipment_OriginStorage_CapacityGreaterThan(@Param("storageCapacity") BigDecimal storageCapacity);
    @Query("SELECT ti FROM TrackingInfo ti WHERE ti.shipment.originStorage.capacity <= :storageCapacity")
    List<TrackingInfo> findByShipment_OriginStorage_CapacityLessThan(@Param("storageCapacity") BigDecimal storageCapacity);
    
    List<TrackingInfo> findByShipment_OriginStorage_Type(StorageType type);

    List<TrackingInfo> findByShipment_OriginStorage_Status(StorageStatus status);
}
