package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.request.TransportOrderRequest;
import com.jovan.erp_v1.response.TransportOrderResponse;

public interface ITransportOrderService {

    TransportOrderResponse create(TransportOrderRequest request);

    TransportOrderResponse update(Long id, TransportOrderRequest request);

    void delete(Long id);

    TransportOrderResponse findOne(Long id);

    List<TransportOrderResponse> findAll();

    TransportOrderResponse findByVehicle_Model(String model);

    TransportOrderResponse findByDriver_Name(String name);

    TransportOrderResponse findByVehicleId(Long vehicleId);

    TransportOrderResponse findByDriverId(Long driversId);

    List<TransportOrderResponse> findByStatus(TransportStatus status);

    List<TransportOrderResponse> findByOutboundDelivery_Id(Long outboundDeliveryId);

    List<TransportOrderResponse> findByOutboundDelivery_Status(DeliveryStatus status);

    List<TransportOrderResponse> findByScheduledDateBetween(LocalDate from, LocalDate to);
}
