package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.model.TrackingInfo;

@Repository
public interface TrackingInfoRepository extends JpaRepository<TrackingInfo, Long> {

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
}
