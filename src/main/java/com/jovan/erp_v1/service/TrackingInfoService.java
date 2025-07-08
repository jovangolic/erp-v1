package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.exception.ShipmentNotFoundException;
import com.jovan.erp_v1.exception.TrackingInfoErrorException;
import com.jovan.erp_v1.mapper.TrackingInfoMapper;
import com.jovan.erp_v1.model.TrackingInfo;
import com.jovan.erp_v1.repository.TrackingInfoRepository;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.TrackingInfoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackingInfoService implements ITrackingInfoService {

    private final TrackingInfoRepository trackingInfoRepository;
    private final TrackingInfoMapper infoMapper;

    @Transactional
    @Override
    public TrackingInfoResponse create(TrackingInfoRequest request) {
        TrackingInfo info = infoMapper.toEntity(request);
        TrackingInfo saved = trackingInfoRepository.save(info);
        return infoMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public TrackingInfoResponse update(Long id, TrackingInfoRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        TrackingInfo info = trackingInfoRepository.findById(id)
                .orElseThrow(() -> new TrackingInfoErrorException("TrackingInfo not found " + id));
        infoMapper.updateEntity(info, request);
        TrackingInfo update = trackingInfoRepository.save(info);
        return infoMapper.toResponse(update);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!trackingInfoRepository.existsById(id)) {
            throw new TrackingInfoErrorException("TrackingInbfo not found " + id);
        }
        trackingInfoRepository.deleteById(id);
    }

    @Override
    public TrackingInfoResponse findOneById(Long id) {
        TrackingInfo info = trackingInfoRepository.findById(id)
                .orElseThrow(() -> new TrackingInfoErrorException("TrackingInfo not found " + id));
        return new TrackingInfoResponse(info);
    }

    @Override
    public List<TrackingInfoResponse> findAll() {
        return trackingInfoRepository.findAll().stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TrackingInfoResponse findByTrackingNumber(String trackingNumber) {
        TrackingInfo info = trackingInfoRepository.findByTrackingNumber(trackingNumber);
        if (info == null) {
            throw new TrackingInfoErrorException("Tracking info not found for tracking number " + trackingNumber);
        }
        return infoMapper.toResponse(info);
    }

    @Override
    public TrackingInfoResponse findByShipmentId(Long shipmentId) {
        TrackingInfo info = trackingInfoRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found " + shipmentId));
        return infoMapper.toResponse(info);
    }

    @Override
    public List<TrackingInfoResponse> findByEstimatedDeliveryBetween(LocalDate start, LocalDate end) {
        return trackingInfoRepository.findByEstimatedDeliveryBetween(start, end).stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByCurrentLocationAndCurrentStatus(String location, ShipmentStatus status) {
        return trackingInfoRepository.findByCurrentLocationAndCurrentStatus(location, status).stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByEstimatedDelivery(LocalDate date) {
        return trackingInfoRepository.findByEstimatedDelivery(date).stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findAllByOrderByEstimatedDeliveryAsc() {
        return trackingInfoRepository.findAllByOrderByEstimatedDeliveryAsc().stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to) {
        return trackingInfoRepository.findByCreatedAtBetween(from, to).stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to) {
        return trackingInfoRepository.findByUpdatedAtBetween(from, to).stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackingInfoResponse> findByUpdatedAtAfter(LocalDateTime since) {
        return trackingInfoRepository.findByUpdatedAtAfter(since).stream()
                .map(TrackingInfoResponse::new)
                .collect(Collectors.toList());
    }
}
