package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.LogisticsProviderErrorException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.ShipmentNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.LogisticsProvider;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.TrackingInfo;
import com.jovan.erp_v1.repository.LogisticsProviderRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.ShipmentRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.ShipmentRequest;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.ShipmentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipmentService implements IShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final StorageRepository storageRepositor;
    private final LogisticsProviderRepository providerRepository;
    private OutboundDeliveryRepository deliveryRepository;

    @Transactional
    @Override
    public ShipmentResponse create(ShipmentRequest request) {
        Shipment ship = new Shipment();
        Storage storage = storageRepositor.findById(request.originStorageId())
                .orElseThrow(() -> new StorageNotFoundException("Storage not found"));
        LogisticsProvider provider = providerRepository.findById(request.providerId())
                .orElseThrow(() -> new LogisticsProviderErrorException("LogisticsProvider not found"));
        OutboundDelivery out = deliveryRepository.findById(request.outboundDeliveryId())
                .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found"));
        ship.setShipmentDate(request.shipmentDate());
        ship.setStatus(request.status());
        ship.setProvider(provider);
        ship.setOriginStorage(storage);
        ship.setOutboundDelivery(out);
        TrackingInfoRequest infoReq = request.trackingInfo();
        TrackingInfo info = new TrackingInfo();
        info.setTrackingNumber(infoReq.trackingNumber());
        info.setCurrentLocation(infoReq.currentLocation());
        info.setEstimatedDelivery(infoReq.estimatedDelivery());
        info.setCurrentStatus(infoReq.currentStatus());
        info.setShipment(ship);
        Shipment saved = shipmentRepository.save(ship);
        return new ShipmentResponse(saved);
    }

    @Transactional
    @Override
    public ShipmentResponse update(Long id, ShipmentRequest request) {
        Shipment ship = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found " + id));
        Storage storage = storageRepositor.findById(request.originStorageId())
                .orElseThrow(() -> new StorageNotFoundException("Storage not found"));
        LogisticsProvider provider = providerRepository.findById(request.providerId())
                .orElseThrow(() -> new LogisticsProviderErrorException("LogisticsProvider not found"));
        OutboundDelivery out = deliveryRepository.findById(request.outboundDeliveryId())
                .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found"));
        ship.setShipmentDate(request.shipmentDate());
        ship.setStatus(request.status());
        ship.setProvider(provider);
        ship.setOriginStorage(storage);
        ship.setOutboundDelivery(out);
        TrackingInfoRequest infoReq = request.trackingInfo();
        TrackingInfo info = new TrackingInfo();
        info.setTrackingNumber(infoReq.trackingNumber());
        info.setCurrentLocation(infoReq.currentLocation());
        info.setEstimatedDelivery(infoReq.estimatedDelivery());
        info.setCurrentStatus(infoReq.currentStatus());
        info.setShipment(ship);
        Shipment saved = shipmentRepository.save(ship);
        return new ShipmentResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!shipmentRepository.existsById(id)) {
            throw new ShipmentNotFoundException("Shipment not found " + id);
        }
        shipmentRepository.deleteById(id);
    }

    @Override
    public List<ShipmentResponse> findByStatus(ShipmentStatus status) {
        return shipmentRepository.findByStatus(status).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByShipmentDateBetween(LocalDate from, LocalDate to) {
        return shipmentRepository.findByShipmentDateBetween(from, to).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByProvideId(Long providerId) {
        return shipmentRepository.findByProvideId(providerId).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByProvider_NameContainingIgnoreCase(String name) {
        return shipmentRepository.findByProvider_NameContainingIgnoreCase(name).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ShipmentResponse findByOutboundDeliveryId(Long outboundId) {
        Shipment s = shipmentRepository.findByOutboundDeliveryId(outboundId);
        return new ShipmentResponse(s);
    }

    @Override
    public List<ShipmentResponse> findByTrackingInfo_CurrentStatus(ShipmentStatus status) {
        return shipmentRepository.findByTrackingInfo_CurrentStatus(status).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByTrackingInfoId(Long trackingInfoId) {
        return shipmentRepository.findByTrackingInfoId(trackingInfoId).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByOriginStorageId(Long storageId) {
        return shipmentRepository.findByOriginStorageId(storageId).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByOriginStorage_Name(String name) {
        return shipmentRepository.findByOriginStorage_Name(name).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByOriginStorage_Location(String location) {
        return shipmentRepository.findByOriginStorage_Location(location).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByOriginStorage_Type(StorageType type) {
        return shipmentRepository.findByOriginStorage_Type(type).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByOriginStorageIdAndStatus(Long storageId, ShipmentStatus status) {
        return shipmentRepository.findByOriginStorageIdAndStatus(storageId, status).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> searchByOriginStorageName(String name) {
        return shipmentRepository.searchByOriginStorageName(name).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findByTrackingInfo_CurrentLocationContainingIgnoreCase(String location) {
        return shipmentRepository.findByTrackingInfo_CurrentLocationContainingIgnoreCase(location).stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShipmentResponse> findOverdueDelayedShipments() {
        return shipmentRepository.findOverdueDelayedShipments().stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ShipmentResponse findByOneId(Long id) {
        Shipment s = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found " + id));
        return new ShipmentResponse(s);
    }

    @Override
    public List<ShipmentResponse> findAll() {
        return shipmentRepository.findAll().stream()
                .map(ShipmentResponse::new)
                .collect(Collectors.toList());
    }

}
