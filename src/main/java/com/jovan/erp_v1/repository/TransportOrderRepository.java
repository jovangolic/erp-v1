package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.model.TransportOrder;

@Repository
public interface TransportOrderRepository extends JpaRepository<TransportOrder, Long> {

    Optional<TransportOrder> findByVehicle_Model(String model);

    Optional<TransportOrder> findByDriver_Name(String name);

    Optional<TransportOrder> findByVehicleId(Long vehicleId);

    Optional<TransportOrder> findByDriverId(Long driversId);

    List<TransportOrder> findByStatus(TransportStatus status);

    List<TransportOrder> findByOutboundDelivery_Id(Long outboundDeliveryId);

    List<TransportOrder> findByOutboundDelivery_Status(DeliveryStatus status);

    List<TransportOrder> findByScheduledDateBetween(LocalDate from, LocalDate to);
}
