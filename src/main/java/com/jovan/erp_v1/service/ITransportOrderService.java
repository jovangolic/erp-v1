package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.request.TransportOrderRequest;
import com.jovan.erp_v1.response.TransportOrderResponse;

public interface ITransportOrderService {

    TransportOrderResponse create(TransportOrderRequest request);
    TransportOrderResponse update(Long id, TransportOrderRequest request);
    void delete(Long id);
    TransportOrderResponse findOne(Long id);
    List<TransportOrderResponse> findAll();
    TransportOrderResponse findByVehicle_Model(String model);
    TransportOrderResponse findByDriver_FirstNameContainingIgnoreCaseAndDriver_LastNameContainingIgnoreCase(String firstName, String lastName);
    TransportOrderResponse findByVehicleId(Long vehicleId);
    TransportOrderResponse findByDriverId(Long driversId);
    List<TransportOrderResponse> findByStatus(TransportStatus status);
    List<TransportOrderResponse> findByOutboundDelivery_Id(Long outboundDeliveryId);
    List<TransportOrderResponse> findByOutboundDelivery_Status(DeliveryStatus status);
    List<TransportOrderResponse> findByScheduledDateBetween(LocalDate from, LocalDate to);
    
    //nove metode
    List<TransportOrderResponse> findByScheduledDate(LocalDate scheduleDate);
    List<TransportOrderResponse> findByPending();
    List<TransportOrderResponse> findByOn_The_Way();
    List<TransportOrderResponse> findByCompleted();
    List<TransportOrderResponse> findByFailed();
    List<TransportOrderResponse> findByVehicle_Status(VehicleStatus status);
    List<TransportOrderResponse> findByVehicle_Available();
    List<TransportOrderResponse> findByVehicle_In_Use();
    List<TransportOrderResponse> findByVehicle_Under_Maintenance();
    List<TransportOrderResponse> findByVehicle_Out_Of_Service();
    List<TransportOrderResponse> findByVehicle_Reserved();
    List<TransportOrderResponse> findByVehicleAndDriver( String vehicleModel, String driverFirstName, String driverLastName);
    List<TransportOrderResponse> findByOutboundDelivery_Pending();
    List<TransportOrderResponse> findByOutboundDelivery_In_Transit();
    List<TransportOrderResponse> findByOutboundDelivery_Delivered();
    List<TransportOrderResponse> findByOutboundDelivery_Cancelled();
    List<TransportOrderResponse> findByOutboundDelivery_DeliveryDate(LocalDate deliveryDate);
    List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateBetween(LocalDate deliveryDateStart, LocalDate deliveryDateEnd);
    List<TransportOrderResponse> findByOutboundDelivery_Buyer_Id( Long buyerId); 
    List<TransportOrderResponse> findByOutboundDelivery_Buyer_CompanyNameContainingIgnoreCase( String buyerCompanyName);
    List<TransportOrderResponse> findByOutboundDelivery_Buyer_PhoneNumberLikeIgnoreCase( String phoneNumber);
    List<TransportOrderResponse> findByOutboundDelivery_Buyer_EmailLikeIgnoreCase( String email);
    List<TransportOrderResponse> findByOutboundDelivery_Buyer_Address( String buyerAddrres);
    List<TransportOrderResponse> findByOutboundDelivery_Buyer_PibLikeIgnoreCase( String buyerPib);
    List<TransportOrderResponse> findDeliveryItemsByTransportOrderId( Long transportOrderId);
    List<TransportOrderResponse> findAllWithDeliveryItems();
    List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateAfter(LocalDate deliveryAfter);
    List<TransportOrderResponse> findByOutboundDelivery_DeliveryDateBefore(LocalDate deliveryBefore);
    TransportOrderResponse findByVehicle_RegistrationNumber(String registrationNumber);
    Boolean existsByVehicle_RegistrationNumber(String registrationNumber);
    List<TransportOrderResponse> findByStatus_AndVehicle_Status( TransportStatus transportStatus, VehicleStatus vehicleStatus);
    List<TransportOrderResponse> findByStatusIn(List<TransportStatus> statuses);
    List<TransportOrderResponse> findByVehicle_StatusIn(List<VehicleStatus> statuses);
    List<TransportOrderResponse> findByVehicleAndStatusIn(Vehicle vehicle, List<TransportStatus> statuses);
    List<TransportOrderResponse> findWithInactiveVehicles();
    List<TransportOrderResponse> findByScheduledDateAfter(LocalDate scheduledDateAfter);
    List<TransportOrderResponse> findByScheduledDateBefore(LocalDate scheduledDateBefore);
}
