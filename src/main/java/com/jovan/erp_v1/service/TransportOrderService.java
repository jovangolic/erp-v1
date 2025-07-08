package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.exception.DriverErrorException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.TransportOrderErrorException;
import com.jovan.erp_v1.exception.VehicleErrorException;
import com.jovan.erp_v1.mapper.TransportOrderMapper;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.TransportOrder;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.repository.DriversRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.TransportOrderRepository;
import com.jovan.erp_v1.repository.VehicleRepository;
import com.jovan.erp_v1.request.TransportOrderRequest;
import com.jovan.erp_v1.response.TransportOrderResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransportOrderService implements ITransportOrderService {

    private final TransportOrderRepository transportOrderRepository;
    private final TransportOrderMapper transportOrderMapper;
    private final VehicleRepository vehicleRepository;
    private final DriversRepository driversRepository;
    private final OutboundDeliveryRepository outRepo;

    @Transactional
    @Override
    public TransportOrderResponse create(TransportOrderRequest request) {
        TransportOrder to = transportOrderMapper.toEntity(request);
        TransportOrder saved = transportOrderRepository.save(to);
        return transportOrderMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public TransportOrderResponse update(Long id, TransportOrderRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        TransportOrder to = transportOrderRepository.findById(id)
                .orElseThrow(() -> new TransportOrderErrorException("TransportOrder not found " + id));
        to.setScheduledDate(request.scheduledDate());
        Vehicle v = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new VehicleErrorException("Vehicle not found"));
        Driver d = driversRepository.findById(request.driversId())
                .orElseThrow(() -> new DriverErrorException("Driver not found"));
        OutboundDelivery delivery = outRepo.findById(request.outboundDeliveryId())
                .orElseThrow(() -> new OutboundDeliveryErrorException("OutboundDelivery not found"));
        to.setDriver(d);
        to.setVehicle(v);
        to.setOutboundDelivery(delivery);
        to.setStatus(request.status());
        TransportOrder updated = transportOrderRepository.save(to);
        return transportOrderMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!transportOrderRepository.existsById(id)) {
            throw new TransportOrderErrorException("TransportOrder not found " + id);
        }
        transportOrderRepository.deleteById(id);
    }

    @Override
    public TransportOrderResponse findOne(Long id) {
        TransportOrder to = transportOrderRepository.findById(id)
                .orElseThrow(() -> new TransportOrderErrorException("TransportOrder not found " + id));
        return new TransportOrderResponse(to);
    }

    @Override
    public List<TransportOrderResponse> findAll() {
        return transportOrderRepository.findAll().stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TransportOrderResponse findByVehicle_Model(String model) {
        TransportOrder to = transportOrderRepository.findByVehicle_Model(model)
                .orElseThrow(() -> new VehicleErrorException("Vehicle model not found"));
        return null;
    }

    @Override
    public TransportOrderResponse findByDriver_Name(String name) {
        TransportOrder to = transportOrderRepository.findByDriver_Name(name)
                .orElseThrow(() -> new DriverErrorException("Driver's name not found"));
        return new TransportOrderResponse(to);
    }

    @Override
    public TransportOrderResponse findByVehicleId(Long vehicleId) {
        TransportOrder to = transportOrderRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleErrorException("Vehicle not found"));
        return new TransportOrderResponse(to);

    }

    @Override
    public TransportOrderResponse findByDriverId(Long driversId) {
        TransportOrder to = transportOrderRepository.findById(driversId)
                .orElseThrow(() -> new DriverErrorException("Driver not found"));
        return new TransportOrderResponse(to);
    }

    @Override
    public List<TransportOrderResponse> findByStatus(TransportStatus status) {
        return transportOrderRepository.findByStatus(status).stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransportOrderResponse> findByOutboundDelivery_Id(Long outboundDeliveryId) {
        return transportOrderRepository.findByOutboundDelivery_Id(outboundDeliveryId).stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransportOrderResponse> findByOutboundDelivery_Status(DeliveryStatus status) {
        return transportOrderRepository.findByOutboundDelivery_Status(status).stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransportOrderResponse> findByScheduledDateBetween(LocalDate from, LocalDate to) {
        return transportOrderRepository.findByScheduledDateBetween(from, to).stream()
                .map(TransportOrderResponse::new)
                .collect(Collectors.toList());
    }
}
