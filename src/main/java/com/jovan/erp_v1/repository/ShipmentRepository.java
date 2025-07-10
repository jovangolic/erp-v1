package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByStatus(ShipmentStatus status);
    List<Shipment> findByShipmentDateBetween(LocalDate from, LocalDate to);
    List<Shipment> findByProviderId(Long providerId);
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
    //nove metode
    List<Shipment> findByTrackingInfo_TrackingNumber(String trackingNumber);
    List<Shipment> findByTrackingInfo_EstimatedDelivery(LocalDate estimatedDelivery);
    List<Shipment> findByTrackingInfo_EstimatedDeliveryBetween(LocalDate estimatedDeliveryStart,LocalDate estimatedDeliveryEnd);
    @Query("SELECT sh FROM Shipment sh WHERE LOWER(sh.provider.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Shipment> findByProvider_EmailLikeIgnoreCase(@Param("email") String email);
    List<Shipment> findByProvider_WebsiteContainingIgnoreCase(String website);
    @Query("SELECT sh FROM Shipment sh WHERE LOWER(sh.provider.phoneNumber) LIKE LOWER(CONCAT('%' :phoneNumber '%'))")
    List<Shipment> findByProvider_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
    List<Shipment> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate);
    List<Shipment> findByOutboundDelivery_DeliveryDateBetween(LocalDate deliveryDateStart, LocalDate deliveryDateEnd);
    List<Shipment> findByOutboundDelivery_Status(DeliveryStatus status);
    @Query("SELECT sh FROM Shipment sh WHERE sh.outboundDelivery.buyer.id = :buyerId")
    List<Shipment> findByOutboundDelivery_Buyer_Id(@Param("buyerId") Long buyerId);
    //isporuke koje kasne ali nisu odlozene Delayed
    @Query("SELECT s FROM Shipment s WHERE s.trackingInfo.estimatedDelivery < CURRENT_DATE AND s.trackingInfo.currentStatus NOT IN ('DELIVERED', 'CANCELLED')")
    List<Shipment> findLateShipments();
    //ubuduce isporuke
    @Query("SELECT s FROM Shipment s WHERE s.trackingInfo.estimatedDelivery BETWEEN CURRENT_DATE AND :futureDate")
    List<Shipment> findShipmentsDueSoon(@Param("futureDate") LocalDate futureDate);
    List<Shipment> findByTrackingInfoIsNull();
    List<Shipment> findByOutboundDelivery_StatusAndOutboundDelivery_DeliveryDateBetween(DeliveryStatus status, LocalDate from, LocalDate to);
    List<Shipment> findByOriginStorageIdAndTrackingInfo_EstimatedDeliveryBetween(Long storageId, LocalDate from, LocalDate to);
    List<Shipment> findByTrackingInfo_CurrentStatusAndTrackingInfo_CurrentLocationContainingIgnoreCase(ShipmentStatus status, String location);
    @Query("SELECT s FROM Shipment s WHERE LOWER(s.outboundDelivery.buyer.name) LIKE LOWER(CONCAT('%', :buyerName, '%'))")
    List<Shipment> findByBuyerNameContainingIgnoreCase(@Param("buyerName") String buyerName);
    @Query("SELECT s FROM Shipment s WHERE s.status = 'DELIVERED' AND s.trackingInfo.updatedAt >= :fromDate")
    List<Shipment> findRecentlyDeliveredShipments(@Param("fromDate") LocalDateTime fromDate);
    @Query("SELECT s FROM Shipment s WHERE s.status = 'CANCELLED'")
    List<Shipment> findCancelledShipments();
    @Query("SELECT s FROM Shipment s WHERE s.status = 'CANCELLED' AND s.shipmentDate BETWEEN :from AND :to")
    List<Shipment> findCancelledShipmentsBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);
    @Query("SELECT sh FROM Shipment sh WHERE sh.status = 'DELAYED'")
    List<Shipment> findDelayedShipments();
    @Query("SELECT sh FROM Shipment sh WHERE sh.status = 'IN_TRANSIT'")
    List<Shipment> findInTransitShipments();
    @Query("SELECT sh FROM Shipment sh WHERE sh.status = 'DELIVERED'")
    List<Shipment> findDeliveredShipments();
    @Query("SELECT sh FROM Shipment sh WHERE sh.status = :status")
    List<Shipment> findShipmentsByFixedStatus(@Param("status") ShipmentStatus status);
    @Query("SELECT s FROM Shipment s WHERE s.outboundDelivery.status = 'PENDING'")
    List<Shipment> findPendingDeliveries();
    @Query("SELECT s FROM Shipment s WHERE s.outboundDelivery.status = 'IN_TRANSIT'")
    List<Shipment> findInTransitDeliveries();
    @Query("SELECT s FROM Shipment s WHERE s.outboundDelivery.status = 'DELIVERED'")
    List<Shipment> findDeliveredDeliveries();
    @Query("SELECT s FROM Shipment s WHERE s.outboundDelivery.status = 'CANCELLED'")
    List<Shipment> findCancelledDeliveries();
    @Query("SELECT s FROM Shipment s WHERE s.status = 'IN_TRANSIT' AND s.outboundDelivery.status = 'IN_TRANSIT'")
    List<Shipment> findInTransitShipmentsWithInTransitDelivery();
    
    //boolean
    boolean existsByTrackingInfo_TrackingNumber(String trackingNumber);
    boolean existsByTrackingInfo_CurrentLocation(String currentLocation);
    boolean existsByProvider_Email(String email);
    boolean existsByProvider_Website(String website);
    boolean existsByProvider_ContactPhone(String contactPhone);
    boolean existsByProvider_PhoneNumber(String phoneNumber);
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.status = 'DELIVERED' AND s.shipmentDate >= :fromDate")
    boolean existsRecentlyDeliveredShipments(@Param("fromDate") LocalDateTime fromDate);
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.status = 'CANCELLED'")
    boolean existsCancelledShipments();
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.status = 'DELAYED'")
    boolean existsDelayedShipments();
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.status = 'IN_TRANSIT'")
    boolean existsInTransitShipments();
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.status = 'DELIVERED'")
    boolean existsDeliveredShipments();
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.status = :status")
    boolean existsShipmentsByStatus(@Param("status") ShipmentStatus status);
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.outboundDelivery.status = 'PENDING'")
    boolean existsPendingDeliveries();
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.outboundDelivery.status = 'IN_TRANSIT'")
    boolean existsInTransitDeliveries();
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.outboundDelivery.status = 'DELIVERED'")
    boolean existsDeliveredDeliveries();
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.outboundDelivery.status = 'CANCELLED'")
    boolean existsCancelledDeliveries();
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.status = 'IN_TRANSIT' AND s.outboundDelivery.status = 'IN_TRANSIT'")
    boolean existsInTransitShipmentsWithInTransitDelivery();
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
    	       "FROM Shipment s WHERE s.trackingInfo.estimatedDelivery < :today AND s.trackingInfo.deliveryDate IS NULL")
    boolean existsLateShipments(@Param("today") LocalDate today);
    @Query("SELECT COUNT(s) > 0 FROM Shipment s WHERE s.trackingInfo.estimatedDelivery < CURRENT_DATE AND s.deliveryStatus = 'DELAYED'")
    boolean existsOverdueDelayedShipments();
}
