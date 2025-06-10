package com.jovan.erp_v1.service;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.TrackingInfoResponse;

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
}
